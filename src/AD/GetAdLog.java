package AD;

import AppiumMethod.DevicesInfo;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class GetAdLog {
    //记录时间
    public static int time = 0;
    //广告id
    public static String adId = null;
    //关闭线程
    public static volatile int stop = 0;
    //确定线程关闭
    public static volatile boolean affirmThread = true;

    /**
     * 更新广告位
     */
    public static void   updateAdId(){
        //更换id线程
        Thread ad = new Thread(() -> {
            while (true) {
                String str = JOptionPane.showInputDialog(null, "请输入ID，只需要输入ID数字：\n", "广告位ID", JOptionPane.PLAIN_MESSAGE);
                if (str == null) break;
                try {
                    Integer.parseInt(str);
                    if (!("GG-" + Integer.parseInt(str) + ".txt").equals(GetAdLog.adId)) {
                        GetAdLog.adId = "GG-" + Integer.parseInt(str) + ".txt";
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "输入的ID不正确,请输入数字:" + str, "错误提示", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ad.start();
    }
    public static void main(String[] args) throws InterruptedException {
        updateAdId();
        String[] GG;
        int n = 0;
        String date = date();
        System.out.println(date);
        int GGlen = 0;
        while (true) {
            if (adId == null) {
                System.out.println("未输入ID:" + adId);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            } else {
                break;
            }
        }
        //用于关闭线程
         boolean booleanTheard = false;
        //判断是否切换广告ID
        String adB="";
        while (true) {
            //判断是否切换广告ID
            String adA=adId;
            //时间线程
            Thread t = new Thread(() -> {
                boolean dateB = true;
                long date1 = 0;
                System.out.println("开启计时");
                affirmThread = false;
                while (true) {
                    if (dateB) {
                        date1 = System.currentTimeMillis() + (5 * 1000);
                        dateB = false;
                    }
                    if (System.currentTimeMillis() >= date1) {
                        time += 5;
                        System.out.println(time + " 秒");
                        dateB = true;
                    }
                    if (stop==1){
                        time += 5;
                        System.out.println(time + " 秒");
                        System.out.println("关闭计时");
                        affirmThread = true;
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            //判断是否更换了广告位
            if (!adA.equals(adB) ) {
                GGlen = 0;
                adB = adA;
            }
            boolean bl = false;
            GG = DevicesInfo.adb(" shell cat /sdcard/FreeBook/ad/" + date + "/" + adId);
            if (GG.length < 1) continue;
            if (GG.length > GGlen) {
                if(booleanTheard){
                   stop = 1;
                   t.interrupt();
                    booleanTheard =false;
                    Thread.sleep(200);
                    while (true){
                        if (affirmThread) break;
                    }
                }
                for (int i = GGlen; i < GG.length; i++) {
                    if (GG[i].equals("")) continue;
                    System.out.println(GG[i]);
                    if ((GG[i].contains("返回广告") && !GG[i].contains("服务端返回广告"))
                            || GG[i].contains("服务端返回广告---没有广告") ||
                            GG[i].contains("请求失败")) bl = true;
                }
                if(bl){
                    time = 0;
                    stop = 0;
                    t.start();
                    booleanTheard = true;
                }
                GGlen = GG.length;
            }
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
                Thread.sleep(1500);
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
