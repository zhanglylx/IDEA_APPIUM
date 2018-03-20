package Monkey;

import AppiumMethod.Tooltip;
import org.apache.commons.lang.StringUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Crash {
    /**
     * 读取txt文件的内容
     *
     * @param file
     *            想要读取的文件对象
     */
    public static void FilterErrorLog(File file) {
        StringBuilder result = new StringBuilder(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date()));
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
            String msg;
            int i = 0;
            boolean crash = false;
            boolean count = false;
            while ((msg = br.readLine()) != null) {// 使用readLine方法，一次读一行
                if (!"".equals(msg)) {
                    if (msg.toLowerCase().contains("CRASH".toLowerCase())) {
                        crash = true;
                        i = 0;
                        System.out.println("发现崩溃");
                        result.append(System.lineSeparator());
                        result.append(StringUtils.repeat("=", 50));
                        count = true;
                    }
                    if (crash) {
                        if (i < 10 || msg.contains("at")) {
                            result.append(System.lineSeparator());
                            result.append(msg);
                            i++;
                        } else {
                            crash = false;
                        }
                    }
                }
            }
            br.close();
            BufferedWriter sc = new BufferedWriter(
                    new FileWriter(System.getProperty("user.dir") + File.separator + "errLong"+
                            new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-sss").format(new Date())+".txt"));
            sc.write(result.toString());
            sc.flush();
            sc.close();
            if (count) {
                Tooltip.finishHint("错误log打印完毕：" + FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + "errLong.txt");
            }else{
                Tooltip.finishHint("没有发现崩溃信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startCrash() {
        int[] pi = new int[0];
        for (String s : RunMonkey.adb("shell ps ")) {
            if (s.toLowerCase().contains("monkey")) {
                String[] str = s.split(" ");
                f: for (String st : str) {
                    if (st.matches("\\d+")) {
                        pi = Arrays.copyOf(pi, pi.length + 1);
                        pi[pi.length - 1] = Integer.parseInt(st);
                        break f;
                    }
                }
            }
        }
        for (int i : pi) {
            RunMonkey.adb("shell  kill " + i);
        }
        System.out.println("开始拉取手机文件");
        try {
                Process pro = Runtime.getRuntime()
                        .exec("cmd /c adb pull /sdcard/monkeyLog.txt " + System.getProperty("user.dir"));
                BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                String msg;
                boolean succeed = false;
                while ((msg = br.readLine()) != null) {
                    System.out.println(msg);
                    if (msg.contains("100%")) {
                        succeed = true;
                        break;
                    }
                }
                if(!succeed){
                Tooltip.errHint("拉取monkeyLog失败");
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logAnalysis.FilterErrorLog(new File(System.getProperty("user.dir") + File.separator + "monkeyLog.txt"));
    }
}
