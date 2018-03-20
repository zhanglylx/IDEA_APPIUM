package Monkey;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import AppiumMethod.Tooltip;

public class logAnalysis {
    /**
     * 读取txt文件的内容
     *
     * @param file 想要读取的文件对象
     */
    public static void FilterErrorLog(File file) {

        FilterErrorLog(file, "执行的pullLog，寻找不到启动时间;");
    }

    public static void FilterErrorLog(File file, String start) {
        ConfigMonkey.setIphoneDate();
        StringBuilder result = new StringBuilder("开始时间：" + start + "     结束时间：" +
              ConfigMonkey.date );
        result.append(System.lineSeparator());
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
            String msg;
            int i = 0;
            boolean crash = false;
            boolean count = false;
            int ANRNum = -1;
            String packName = ConfigMonkey.getConfig("logAnalysis").getPackageName();
            ArrayList<String> ls = new ArrayList<String>();
            while ((msg = br.readLine()) != null) {// 使用readLine方法，一次读一行
                ls.add(msg);
                if (!"".equals(msg)) {
                    if (msg.toLowerCase().contains("CRASH".toLowerCase())
                            && msg.toLowerCase().contains(packName)) {
                        crash = true;
                        i = 0;
                        System.out.println("MonkeyLog文件中发现崩溃");
                        result.append(System.lineSeparator());
                        result.append(StringUtils.repeat("=", 150));
                        result.append( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date()));
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
                    //记录ANR崩溃
                    /**
                     * 记录log中ANR位置前50行和后50行
                     */
                    if (msg.toLowerCase().contains("ANR".toLowerCase())
                            && msg.toLowerCase().contains(packName)) {
                        System.out.println("MonkeyLog文件中发现ANR");
                        count = true;
                        result.append(System.lineSeparator());
                        result.append(StringUtils.repeat("=", 150));
                        result.append( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date()));
                        if (ls.size() - 50 < 1) {
                            for (String s : ls) {
                                result.append(System.lineSeparator());
                                result.append(s);
                            }
                        } else {
                            ANRNum = 0;
                            for (int j = ls.size() - 50; j < ls.size(); j++) {
                                result.append(System.lineSeparator());
                                result.append(ls.get(j));
                            }

                        }
                    }
                    if (ANRNum > -1) {
                        result.append(System.lineSeparator());
                        result.append(msg);
                        ANRNum++;
                    }
                    //将ANR置换为初始值
                    if (ANRNum > 50) ANRNum = -1;
                }
                //清空ls
                if (ls.size() > 10000 && ANRNum == -1) {
                    ls.clear();
                }
            }
            br.close();
            if (count || GetPhoneLog.result.size() > 0) {
                String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-sss").format(new Date());
                BufferedWriter sc = new BufferedWriter(
                        new FileWriter(System.getProperty("user.dir") + File.separator + "errLong" +
                                date + ".txt"));
                sc.write(result.toString());
                if (GetPhoneLog.result.size() > 0 )
                    sc.write(GetPhoneLog.result.toString());
                sc.flush();
                sc.close();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("cmd /c explorer /select, " + System.getProperty("user.dir") + File.separator + "errLong" +
                        date + ".txt");

                Tooltip.finishHint("错误log打印完毕：" + System.getProperty("user.dir") + File.separator + "errLong" +
                        date + ".txt");

            } else {
                Tooltip.finishHint("没有发现错误信息");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FilterErrorLog(new File(System.getProperty("user.dir") + "\\runTimeLog.txt"), "执行的pullLog，寻找不到启动时间;");
    }
}
