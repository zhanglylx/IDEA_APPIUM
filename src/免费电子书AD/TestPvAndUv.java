package 免费电子书AD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.sf.json.JSONObject;

public class TestPvAndUv {
	private LinkedHashMap<String, Integer> mapDj;
	// 用户访问的广告位和id
	private LinkedHashMap<String, Integer> requestCurrId;
	// 记录用户访问的广告
	private LinkedHashMap<String, Integer> userGG;
	private LinkedHashMap<String, LinkedHashMap<String, Integer>> list;
	LinkedHashMap<String, Integer> ss;
	private JSONObject json;// 接收请求
	private int userIdData;
	private String name;
	private String nameData;
	public TestPvAndUv() {
		list = new LinkedHashMap<String, LinkedHashMap<String, Integer>>();
		requestCurrId = new LinkedHashMap<String, Integer>();
		userGG = new LinkedHashMap<String, Integer> ();
		mapDj = new LinkedHashMap<String, Integer> ();
	    ss = new LinkedHashMap<String, Integer> ();
		ss.put("", 1);
	}
	public void setJson(String response,int userIdData){
		this.userIdData = userIdData;
		json = JSONObject.fromObject(response);
		name = json.getString("advId")+":"+
				json.get("adname").toString()+			
				json.getString("id");
		if(!list.containsKey(name)){
			list.put(name, new LinkedHashMap<String, Integer>());
		}
		recordRequestCurrId();	
		addRequestPv();
		addUserGG();
		
	}
	
	public void addRequestPv(){
		 LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		map = list.get(name);
		map = addMapCount(map,"请求pv");
		map = addMapCount(map,"曝光pv");	
		map = addRequestUv(map,"请求UV");
		map = addRequestUv(map,"曝光UV");
		list.put(name, map);
	}
	
	public void addDj(){
		 LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		map = list.get(name);
		map = addMapCount(map,"点击PV");
		map = addDjUv(map,"点击UV");
		list.put(name, map);
		addDjMap();

	}
	public LinkedHashMap<String, Integer> addRequestUv(LinkedHashMap<String, Integer> map,String putName){
		if(!userGG.containsKey(name+userIdData)){
			map = addMapCount(map,putName);
		}
		return map;
	}
	
	public LinkedHashMap<String, Integer> addDjUv(LinkedHashMap<String, Integer> map,String putName){
		if(!mapDj.containsKey(name+userIdData)){
			map = addMapCount(map,putName);
		}
		return map;
	}
	
	
	
	public void addDjMap(){
		mapDj.put(name+userIdData, Integer.parseInt(json.getString("id").toString()));
	}
	public void addUserGG(){
		userGG.put(name+userIdData, Integer.parseInt(json.getString("id").toString()));
	}
	/**
	 * 统计次数
	 * @param map
	 * @param GGID
	 * @return
	 */
	public  LinkedHashMap<String, Integer> addMapCount(LinkedHashMap<String, Integer> map,String GGID){
		if(map.containsKey(GGID)){
			map.put(GGID, map.get(GGID)+1);
		}else{
			map.put(GGID, 1);
		}
		return map;
	}

	/**
	 * 记录用户上一次请求的广告id
	 */
	public void recordRequestCurrId(){
			requestCurrId.put(nameData,Integer.parseInt(json.get("id").toString()));
	}
	/**
	 * 获取用户上一次请求的广告id
	 * @return 存在返回id，不存在返回-1
	 */
	public int getRequestCurrId(){
		if(requestCurrId.containsKey(nameData)){
			return requestCurrId.get(nameData);
		}
		return -1;
	}
	
	/**
	 * 打印log
	 */
	public void print(){
		 LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
//		 TreeMap<String, LinkedHashMap<String, Integer>>	listMap = new TreeMap<String, LinkedHashMap<String, Integer>>(
//					   new Comparator<String>() {
//		                    public int compare(String obj1, String obj2) {
//		                    	obj1 = obj1.substring(0,obj1.indexOf(":")).substring(3);
//		                    	obj2 = obj2.substring(0,obj2.indexOf(":")).substring(3);
//		                        int a = Integer.parseInt(obj1);
//		                        int b = Integer.parseInt(obj2);
//		                        return a - b;
//		                    }
//		                });
//		 listMap.putAll(list);
		 ArrayList<String> listMap = new ArrayList<String>();
		for(Entry<String, LinkedHashMap<String, Integer>> ent : list.entrySet()){
			map = ent.getValue();
			listMap.add(ent.getKey()+"请求PV为:"+map.get("请求pv")+
					";请求UV为:"+map.get("请求UV")+";曝光pv为:"+map.get("曝光pv")+
					";曝光UV:"+map.get("曝光UV")+";点击pv:"+map.get("点击PV")+
					";点击UV:"+map.get("点击UV"));
		}
		Collections.sort(listMap, new Comparator<String>() {
            public int compare(String obj1, String obj2) {
            	obj1 = obj1.substring(0,obj1.indexOf(":")).substring(3);
                	obj2 = obj2.substring(0,obj2.indexOf(":")).substring(3);
                    int a = Integer.parseInt(obj1);
                    int b = Integer.parseInt(obj2);
                    return a - b;
                }
            });
		Iterator<String> it =  listMap.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	public void setUserGG(String name){
		this.nameData = name;
	}
}
