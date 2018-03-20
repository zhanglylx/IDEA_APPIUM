package 免费电子书AD;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class countMap {
	public static LinkedHashMap<String, Integer> mapAdd(LinkedHashMap<String, Integer> map, String id) {
		if (map.containsKey(id)) {
			map.put(id, map.get(id) + 1);
		} else {
			map.put(id, 1);
		}
		return map;
	}
	//将打印出来的数据格式化
	public static String form(String s){
		if(s.toCharArray().length>30){
			return s;
		}
		StringBuffer str = new StringBuffer(s);
		int i = 30-s.length();
		for(int j=0;j<i;j++){
			str.append("-");
		}
		return str.toString();
	}
	//时间
	public static String date(){
		Date dts = new Date();
		SimpleDateFormat matter1s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
		return matter1s.format(dts);
	}
	public static void list (String exposureRequest,String exposureResponse){
		Point list = Point.getPoint();
		list.add("上传曝光请求:" + exposureRequest);				
		list.add("上传曝光响应:" + exposureResponse);
		list.add("结束:" + countMap.date());
	}
	public static void getList(String requestData,String response){
		Point list = Point.getPoint();
		list.add("请求：" + requestData);
		list.add("响应：" + response);
	}
}
