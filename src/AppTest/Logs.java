package AppTest;

import AppiumMethod.Config;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {
    public static String logs = "";
    public static String logsErr = "";
    public static String dateName;

    static {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        dateName = sdf.format(date);
    }

    public static void recordLogs(String name, boolean state) {
        if (state) {
            Logs.logs += name + ":成功\n";
        } else {
            Logs.logsErr += name + "：失败\n";
        }
    }

    public static void prrLogs() {
        System.out.println("成功的用例:" + Logs.logs);
        System.out.println("=========================" +
                "===================================");
        System.out.println("失败的用例：" + Logs.logsErr);
    }

    public static void saveLog(String name, String text) {
        File file = new File(Config.IMAGES_B + File.separator + name);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(
                    new FileWriter(
                            file.getPath() + File.separator + Logs.dateName + ".txt", true));
            pw.println(Devices.caseNameStatic+": "+text);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (pw != null) {
            pw.close();
        }
    }

}
