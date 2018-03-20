package AD;

import AppiumMethod.DevicesInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GetAdLog {
    public static void main(String[] args) {
        String ad = "GG-30.txt";
        String[] GGAD = new String[0];
        String[] GG;
        int n = 0;
        String date = date();
        int GGlen = 0;
        while (true) {
            boolean bl = false;
            GG = DevicesInfo.adb(" shell cat /sdcard/FreeBook/ad/" + date + "/" + ad);
            if(GG.length<1)continue;
            if(GG.length>GGlen){
                for(int i=GGlen;i<GG.length;i++){
                    System.out.println(GG[i]);
                    if ((GG[i].contains("返回广告") && !GG[i].contains("服务端返回广告"))
                        || GG[i].contains("服务端返回广告---没有广告")) bl = true;
                }
                GGlen=GG.length;
            }

//            boolean nl = false;
//            fr:
//            for (String s : GG) {
//                if(s.contains("GG"))nl=true;
//                for (String k : GGAD) {
//                    if (s.equals(k)) {
//                        continue fr;
//                    }
//                }
//                String r = GG[GG.length-1]
//                if ((s.contains("返回广告") && !s.contains("服务端返回广告"))
//                        || s.contains("服务端返回广告---没有广告")) bl = true;
//                System.out.println(s);
//
//            }
//            if(nl)GGAD = GG;
            if (bl) {
                System.out.println();
                switch (n) {
                    case (0):
                        System.out.println("=========");
                        n = 1;
                        break;
                    case (1):
                        System.out.println("==================");
                        n = 2;
                        break;
                    case (2):
                        System.out.println("====================================");
                        n = 3;
                        break;
                    case (3):
                        System.out.println("======================================================");
                        n = 0;
                        break;
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String date() {
        for (String s : DevicesInfo.adb(" shell date")) {
            if (s.matches(".+\\d{2}:\\d{2}:\\d{2}.+")) {
                String dateString = s;
                SimpleDateFormat sfEnd = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sfStart = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.ENGLISH);
                try {
                    return sfEnd.format(sfStart.parse(dateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
