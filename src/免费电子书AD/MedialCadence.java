package 免费电子书AD;

import javax.swing.JOptionPane;

/**
 * 单利模式
 * 用于终端程序
 * @author Administrator
 *
 */
public class MedialCadence {
	private static MedialCadence medialCadence;
	private  int i=0;
	private MedialCadence(){
	}
	public  static MedialCadence getMedialCadence(){
		if(medialCadence == null){
			medialCadence =  new MedialCadence();
		}
		return medialCadence;
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public static void runningStatus() {
		// int a =0;
		Thread t = new Thread(new Runnable() {
			public void run() {
				// 代码块
				while (true) {
					if (JOptionPane.showConfirmDialog(null, "中途关闭请点击是", "提示，", JOptionPane.YES_NO_OPTION) == 0) {
						getMedialCadence().setI(1);
						break;
					}
				}
			}
		});
		t.start();
	}
	
}
