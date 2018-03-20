package 免费电子书AD;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import net.sf.json.JSONObject;

/**
 * 测试广告展现顺序逻辑 测试前需要在后台配置单日广告总量数 在redis中清空缓存
 * getAd请求的广告为写死3位置
 * 用户为单用户
 * 注意：log的显示顺序为执行次数日志，每一次的请求响应，<<>>中的内容为检查不正确项，统计次数
 * 脚本运行机制：
 * 第一步：
 * 		配置环境，发送请求getAd  拼接userId和广告位id
 * 第二步：
 * 		判断响应是否为空，如果为空继续下一次循环
 * 		接收请求的getAd响应，取出id，advId，adId拼接在发送上报请求中
 * 第三步：
 * 		判断上传的响应，如果返回0，给id+1
 * 第四步：
 * 		根据后台配置的数据判断id的总数是否与后台限制相同
 * 第五步：
 * 		打印log
 * @author Administrator
 *
 */
public class 广告展现顺序逻辑 {
	//单日总单击两  type 1为点击  0为曝光
	final static int type = 1;
	//统计用户获取广告出现的次数
	static LinkedHashMap<String, Integer> userMap = new LinkedHashMap<String, Integer>();
	//获取广告出现的次数
	static LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();	
	static LinkedHashMap<String, Integer> requestDataMap = new LinkedHashMap<String, Integer>();
	static LinkedHashMap<String, ArrayList<Integer>> userOrderMap = new LinkedHashMap<String, ArrayList<Integer>>();	
	public static void main(String[] args) {
		
		//弹出提示框处理是否要中途退出
		MedialCadence.runningStatus();
		Point list = Point.getPoint();
		int[] userId = createUserIdAndGG.userId();
		// 统计执行次数
		int i = 1;
		// 请求的广告位
		int[] GG = createUserIdAndGG.GG();
		// 执行过程中的广告位数组索引
		int ggArrayIndex = 0;
		//执行过程中的用户数组索引
		int uesrArrayIndex = 0;
		//广告展现逻辑顺序索引
		int GGIndex = 0;
		//广告id
		int id=0;
		
		try {
			
			while (true) {
				// 判断是否达到循环次数
				if (i > config.executeCount) {
					break;
				}
				// 检测是否提前停止程序
				if (MedialCadence.getMedialCadence().getI()!=0) {
					break;
				}
//				// 跳过插屏广告位
//				if (ggArrayIndex == 8 || ggArrayIndex == 25 || ggArrayIndex == 16 || ggArrayIndex == 52) {
//					ggArrayIndex += 1;
//					continue;
//				}
				list.add("第" + i + "次测试--------------------------------------" + countMap.date());
				if(GGIndex == config.GGOrder.length){GGIndex=0;}
				ggArrayIndex = ArrayIndex.index(GG, ggArrayIndex);
				// 判断用户达到最大数后为GG从0开始
				uesrArrayIndex = ArrayIndex.index(userId, uesrArrayIndex);
				int userIdData = userId[uesrArrayIndex];
				//第一步
				//拼接请求
				String requestData = requestUrl.getAdRequest(GG[ggArrayIndex], userIdData);
				//判断用户广告位上一次是否有获取到的广告，如果存在，将广告id赋值给currId
				if(requestDataMap.get((userIdData+"GG-"+GG[ggArrayIndex]))!=null){
					requestData ="cnid=1062&umeng=test_test&version=4.0.0&vercode=64&imei=865736034224322&imsi=&uid=19355701&packname=com.mianfeia.book&oscode=25&model=MIX+2&other=a&vcode=64&channelId=1062&mac=a3d76e202891719e398052c0a29e272e&platform=android&appname=cxb&brand=Xiaomi&"
							+ "advId=GG-" + GG[ggArrayIndex] + "&userId=" + userIdData + "&" + "currId="+requestDataMap.get((userIdData+"GG-"+GG[ggArrayIndex]))+"&net=WIFI";
				}
				// 发送请求广告
				String response = get.sendGet(config.url, requestData);
				ggArrayIndex++;
				uesrArrayIndex++;
				//记录log
				countMap.getList(requestData, response);
				// 检查返回的是否是一个json格式
				check.checkJson(response);
				JSONObject jsonObj = JSONObject.fromObject(response);// 接收请求
				
				if (!check.checkId(jsonObj, response, i)) {i++;continue;}
				//第二步
				//记录当前用户上一次获取广告的id
				id = Integer.parseInt(jsonObj.get("id").toString());
				requestDataMap.put(userIdData+jsonObj.get("advId").toString(), id);
				/*
				 * 判断响应顺序是否正确
				 */
				String in = userIdData+"*-*"+jsonObj.get("advId").toString();
				ArrayList<Integer> ls = new ArrayList<Integer>();
				if(userOrderMap.get(in)== null){
					ls.add(id);
					userOrderMap.put(in, ls);
				}else{
				ls = userOrderMap.get(in);
				ls.add(id);
				userOrderMap.put(in, ls);
				}
//				if(config.GGOrder[GGIndex] != id){
//					requestUrl.errorPopupWindow("响应顺序不正确,GGOrder:"+config.GGOrder[GGIndex]+";响应id："+id);
//				}
				GGIndex++;
				String exposureRequest = requestUrl.exposureRequest(jsonObj, type, id, userIdData);
				String exposureResponse = get.sendGet(config.superior, exposureRequest);
				//将请求和响应添加到list
				countMap.list(exposureRequest, exposureResponse);
				//第三步：
				// 判断响应的json格式是否正确，不正确跳出循环
				check.checkJson(exposureResponse);
				// 处理上传曝光响应数据
				JSONObject js = JSONObject.fromObject(exposureResponse);
				if (js.get("code") == null) {
					requestUrl.errorPopupWindow("errorPopupWindow返回的code为空");
				} else {
					// 将id记录到map
					userMap = countMap.mapAdd(userMap, userIdData+"*"+jsonObj.get("advId") +" "+jsonObj.get("adname")+ ":" + id);
					map = countMap.mapAdd(map, jsonObj.get("advId") +" "+jsonObj.get("adname")+ ":" + id);
				}
				// 判断上报是否成功
				check.checkCode(Integer.parseInt(js.get("code").toString()));		
				System.out.println("第" + i + "次执行完毕");
				i++;
			}	
		} catch (Exception e) {
			e.printStackTrace();
			requestUrl.errorPopupWindow("出现了未知错误:");
			
		} finally {
			//第五步：
			list.printlnList();
			list.clear();
			// 检查广告次数是否超过限制
			System.out.println("《《《《《《《《检查不正确的内容《《《《《《《《");
			//第四步
			list.clear();
			for (Entry<String, Integer> ent : userMap.entrySet()) {
				list.add(countMap.form(ent.getKey() + "共出现:" )+ent.getValue() + "次");
				String s = ent.getKey().substring(ent.getKey().indexOf("*")+1, ent.getKey().length());
				list.add(countMap.form(s + "总量共出现:" )+map.get(s) + "次");
				list.add("---------------------------");
			}
			//将用户的key保存到userKey中
			ArrayList<String> userLsKey = new ArrayList<String>();
			for(Entry<String,ArrayList<Integer>> ent : userOrderMap.entrySet()){
				userLsKey.add(ent.getKey());
			}
			/**
			 * 判断逻辑：通过在config配置的广告位对应的顺序
			 * 将记录中的用户广告位中的广告出现顺序去匹配
			 * 如：
			 * 一个用户访问同一个广告位，同一个广告位配置了3个广告
			 * {110,220,330,110,220,330,110,220,330}
			 * config中的广告id
			 * {110,220,330}
			 * 通过循环将用户第一次的和config中的第一次匹配
			 * 当用户达到第4次时，将config从110继续开始匹配
			 * 以此类推
			 */
			//进行判断
			for(int j=0;j<userLsKey.size();j++){
				ArrayList<Integer> userLs = new ArrayList<Integer>();
				ArrayList<Integer> configLs = new ArrayList<Integer>();
				//将用户的每一个广告位的数据保存到userLs中			
				userLs = userOrderMap.get(userLsKey.get(j));
				//将用户的key取出并去掉用户key
				String userStr = listUser(userLsKey.get(j));
				//将config中的广告ID取出赋值给configLs
				configLs = config.userOrderMap.get(userStr);
				//用于记录config中的list的下标索引
				int configLsIndex = 0;
				for(int s = 0 ; s<userLs.size();s++){
					if(configLs == null){
						continue;
					}

					if(Integer.parseInt(userLs.get(s).toString())  != 
							Integer.parseInt((configLs.get(configLsIndex)).toString())){
						System.out.println(("顺序检查不正确："+userLsKey.get(j)+":"
								+userLs.get(s) +"config:"+configLs.get(configLsIndex)));
						System.out.println("userLs："+userLs);
						System.out.println("configLs："+configLs);
					}
					configLsIndex++;
					if(configLsIndex==configLs.size()){
						configLsIndex=0;
					}
				}
			}	
			System.out.println("》》》》》》》》》》》》》》》》");
			list.printlnList();
		}
		System.out.println("测试完毕");
	}
	//将用户的key进行过滤，用于获取config中的key
	public static String listUser(String userLs){
		return userLs.substring(userLs.indexOf("*-*")+3, userLs.length());
	}
}
