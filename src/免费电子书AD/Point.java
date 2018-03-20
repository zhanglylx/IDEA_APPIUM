package 免费电子书AD;

import java.util.ArrayList;
import java.util.List;
/**
 * 单利模式
 * @author Administrator
 *
 */
public class Point {
	private List<String> list;
	private static Point point;
	private Point(){
		list = new ArrayList<String>();
	}
	public static Point getPoint(){
		if(point == null){
			point = new Point();
		}
		return point;
	}
	public List<String> getList() {
		return list;
	}
	
	public void add(String list) {
		this.list.add(list);
	}
	public void clear(){
		this.list.clear();
	}
	public  void printlnList(){
		for (String s : this.list) {
			System.out.println(s);
		}
	}
	
}
