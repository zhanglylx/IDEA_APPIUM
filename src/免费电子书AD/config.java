package 免费电子书AD;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class config {
	//请求广告URL
	final static String url = "http://test.cread.com:8088/getAd";
	//上传曝光URL
	final static String superior = "http://test.cread.com:8088/pvuvreport";
	//循环次数限制
	final static int executeCount =5000000;
	//版本
	final static String VERSION = "4.0.1";
	//渠道
	final static String CNID = "1062";
	//访问人数
	final static int userNumber = 10000;
	//广告位
	final static int GGNumber = 68;
	//服务器单日广告曝光总量
	final static int dayGGbgzl = 30;
	//单日用户广告曝光总量
	final static int dayUserGGbg = 10;
	//单日用户广告点击总量
	final static int dayUserGGdj = 10;
	//单日广告点击总量
	final static int dayGGdjzl = 10;
	//广告展现顺序逻辑广告ID顺序
	final static int[] GGOrder = {1778,2001,1661};
	
	static LinkedHashMap<String, ArrayList<Integer>> userOrderMap =setMap(); 
	
	public static LinkedHashMap<String, ArrayList<Integer>> setMap(){
		LinkedHashMap<String, ArrayList<Integer>> 	map=new LinkedHashMap<String, ArrayList<Integer>>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1778);
		list.add(2001);
		list.add(1661);
		map.put("GG-3", list);
		list = new ArrayList<Integer>();
		list.add(1761);
		list.add(2039);
		map.put("GG-1", list);
		list = new ArrayList<Integer>();
		list.add(1998);
		list.add(2069);
		list.add(1757);
		map.put("GG-6", list);
		return map;
	}
}
