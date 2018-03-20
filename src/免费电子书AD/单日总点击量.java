package 免费电子书AD;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import net.sf.json.JSONObject;

/**
 * 测试单日总点击量 测试前需要在后台配置单日广告总量数 在redis中清空缓存
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
public class 单日总点击量 {
	//单日总单击两  type 1为点击  0为曝光
	final static int type = 1;
	//统计广告出现的次数
	static LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
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
		// 发送请求广告
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
				// 判断广告位达到最大数后为GG从0开始
				ggArrayIndex = ArrayIndex.index(GG, ggArrayIndex);
				// 判断用户达到最大数后为GG从0开始
				uesrArrayIndex = ArrayIndex.index(userId, uesrArrayIndex);
				int userIdData = userId[uesrArrayIndex];
				//第一步
				//拼接请求
				String requestData = requestUrl.getAdRequest(GG[ggArrayIndex], userIdData);
				// 发送请求广告
				String response = get.sendGet(config.url, requestData);
				ggArrayIndex++;
				uesrArrayIndex++;
				//记录log
				countMap.getList(requestData, response);
				// 检查返回的是否是一个json格式
				check.checkJson(response);
				JSONObject jsonObj = JSONObject.fromObject(response);// 接收请求

				//检查id是否为空
				if (!check.checkId(jsonObj, response, i)) {i++;continue;}
				//第二步
				int id = Integer.parseInt(jsonObj.get("id").toString());
				// 发送上报请求
				/**
				 * type 0 曝光 1上报点击
				 */
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
					break;
				} else {
					// 将id记录到map
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
			for (Entry<String, Integer> ent : map.entrySet()) {
				list.add(countMap.form(ent.getKey() + "共出现:" )+ent.getValue() + "次");
				if (ent.getValue() != config.dayGGdjzl) {

					System.out.println("检查的次数不正确：" + ent.getKey() + "次数：" + ent.getValue());
				}
			}
			System.out.println("》》》》》》》》》》》》》》》》");
			list.printlnList();
		}
		System.out.println("测试完毕");
	}
}
