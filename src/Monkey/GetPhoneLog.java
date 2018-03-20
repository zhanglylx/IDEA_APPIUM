package Monkey;

import AppiumMethod.Config;
import AppiumMethod.Tooltip;
import AppiumMethod.installAPPPackage;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GetPhoneLog {
    public static ArrayList<String> result = new ArrayList<String>();

    public GetPhoneLog() {

    }

    public void getLog() {
        try {
            if (ConfigMonkey.devicesName == null) {
                Tooltip.errHint("GetPhoneLog_getLog获取devicesName为空");
            }
            installAPPPackage.isPuth(Config.ADB_PUTH);
            Process pro = Runtime.getRuntime().exec(Config.ADB_PUTH + " -s " + ConfigMonkey.devicesName + " logcat");
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String msg = null;
            ArrayList<String> ls = new ArrayList<String>();
            int ANRNum = -1;
            String packName = ConfigMonkey.getConfig("GetPhoneLog").getPackageName();
            System.out.println("同时执行runTimeLog抓取ANR");
            while ((msg = br.readLine()) != null) {
                if (!checkDate(msg)) continue;
                ls.add(msg);
                if (msg.contains("ANR".toLowerCase())
                        && msg.contains(packName)) {
                    System.out.println(msg);
                    System.out.println("抓取手机运行log发现ANR");
                    result.add(System.lineSeparator());
                    result.add(StringUtils.repeat("=", 150));
                    result.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date()) + System.lineSeparator());
                    result.add("抓取手机运行log发现的ANR");
                    if (ls.size() - 50 < 1) {
                        for (String s : ls) {
                            result.add(System.lineSeparator());
                            result.add(s);
                        }
                    } else {
                        ANRNum = 0;
                        for (int j = ls.size() - 50; j < ls.size(); j++) {
                            result.add(System.lineSeparator());
                            result.add(ls.get(j));
                        }

                    }
                }
                if (ANRNum > -1) {
                    result.add(System.lineSeparator());
                    result.add(msg);
                    ANRNum++;
                }
                //将ANR置换为初始值
                if (ANRNum > 50) ANRNum = -1;
                if (ls.size() > 5000 && ANRNum == -1) {
                    System.out.println("runTimeLog抓取ANR正在执行");
                    ls.clear();
                }
                if (!msg.equals("") && msg.contains(ConfigMonkey.getConfig("GetPhoneLog").getPackageName()))
                    method3(System.getProperty("user.dir") + "\\runTimeLog.txt", msg);

            }
            //清空ls

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void method3(String fileName, String content) {
        try {
// 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
// 文件长度，字节数
            long fileLength = randomFile.length();
// 将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content + "\r\n");
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDate(String msg) {

        Long ms;
        try {
            msg = msg.substring(0,msg.indexOf(".")+4);
           ms= Long.parseLong(replaceDate(msg));
        }catch (Exception  e){
            return false;
        }
        Long date = Long.parseLong(replaceDate(ConfigMonkey.getDate()));
        if (ms > date) {
            return true;
        }
        return false;
    }

    private String replaceDate(String msg) {
        msg = msg.replace(" ", "");
        msg = msg.replace("-", "");
        msg = msg.replace(":", "");
        msg = msg.replace(".", "");
        return msg;
    }
}
