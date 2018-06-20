package 免费电子书AD;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 制造线上数据
 */
public class getDataOnline {
    private static String url = "https://ad.cread.com/getAd";

    public static void main(String[] args) throws IOException {
        int[] userIdData = createUserIdAndGG.userId();
        int[] GGData = new int[]{1,30,3,31};
        int GGDataIndex=0;
        PrintWriter pw=null;
        for(int i=0;i<userIdData.length;i++){
            if(GGDataIndex==GGData.length)GGDataIndex=0;
            String data = "1460&umeng=test_test&version=4.0.2&" +
                    "vercode=64&imei=865736034224322&imsi=&" +
                    "uid=" +userIdData[i]+
                    "&packname=com.mianfeia.book&oscode=25&" +
                    "model=MIX+2&other=a&vcode=64&channelId=1206&" +
                    "mac=a3d76e202891719e398052c0a29e272e&platform=android&" +
                    "appname=cxb&brand=Xiaomi&" +
                    "advId=GG-" +GGData[GGDataIndex]+
                    "&userId=" +userIdData[i]+
                    "&currId=-1&net=WIFI";
            GGDataIndex++;
           String response = get.sendGet(url,data);
            System.out.println(response);
//           if(response==null || (response.contains("{}")&&!response.contains("adname"))){
//               System.out.println(response+"       "+data);
//           }
            if(pw==null)pw = new PrintWriter(new FileWriter("OnlineData.txt"));
            pw.println(data);
            System.out.println(i);
        }
        pw.flush();
        pw.close();
        }

}
