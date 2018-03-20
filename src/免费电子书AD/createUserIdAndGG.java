package 免费电子书AD;

import java.util.Arrays;

public class createUserIdAndGG {
	public static int[] userId(){
		int[] userId = new int[0];
		//给用户id数组赋值
		for (int j = 1; j <= config.userNumber; j++) {
			userId = Arrays.copyOf(userId, userId.length + 1);
			userId[j-1] = getRandNum();
		}
		return userId;	
	}
	public static int[] GG(){
		int[] GG = new int[0];
		// 给广告位赋值
		int i=0;
		for (int j = 1; j <= config.GGNumber; j++) {
			if(j==19 || (j>20 && j<26)
					|| j==41 || j==42 || j==35 || j==51 || j==16 ||j==9
					|| j==20 || j==53){
				continue;
			}
			GG = Arrays.copyOf(GG, GG.length + 1);
			GG[i] = j;
			i++;
		}
		return GG;
	}
	public static int getRandNum() {
		return (int) ((Math.random() * 9 + 1) * 100000);
	}
}
