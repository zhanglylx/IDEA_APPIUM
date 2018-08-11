package Monkey;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.*;

import AppTest.Devices;
import AppiumMethod.Config;
import AppiumMethod.DevicesInfo;
import AppiumMethod.Tooltip;

/**
 * 作者:张连宇
 * 第一版:执行log，筛选log中的崩溃信息
 * 第二版:增加单独筛选手机中的log崩溃信息公告
 * 第三版:增加筛选ANRlog功能，修改bug(关闭log请点击确定按钮)提示文案不正确，增加通过线程获取手机实时log功能并筛选ANR并将与包名一致的log信息保存到本地，修改时间:2018:1:31
 */
public class RunMonkey {
    private static ConfigMonkey cf;
    private static int[] pid = new int[0];
    private static boolean stop = false;
    private int p = 1;

    private RunMonkey() {
        cf = ConfigMonkey.getConfig("RunMonkey");
        startMonkey();
        setPid();
    }
    /**
     * 启动Monkey
     */
    private void startMonkey() {
        Thread s = new Thread(new Runnable() {
            public void run() {
                if (ConfigMonkey.devicesName == null) {
                    Tooltip.errHint("RunMonkey_startMonkey获取devicesName为空");
                }
                System.out.println("开始执行monkey");
//                 –pct-touch 30.0   –-pct-motion 25.0 –-pct-nav 20.0 –-pct-majornav 15.0 -–pct-appswitch 5.0  –-pct-anyevent 5.0 –-pct-trackball 0.0 –-pct-syskeys 0.0
//             adbs("-s "+ConfigMonkey.devicesName+" shell monkey  -p " + cf.getPackageName()
//                        +  ""+
//                         " --monitor-native-crashes " +
//                        " --ignore-crashes --ignore-timeouts --ignore-native-crashes " +""+
//                        " -v -v -v " + "--throttle "
//                        + cf.getEventsTime() + " "
//                        + cf.getRunCount() + " –-pct-touch 30.0 –-pct-motion 25.0 " +
//                     "–-pct-nav 20.0 –-pct-majornav 15.0 -–pct-appswitch 5.0 " +
//                     " –-pct-anyevent 5.0 –-pct-trackball 0.0 –-pct-syskeys 0.0  ");
                //以下为发生崩溃即停止
                adbs("-s "+ConfigMonkey.devicesName+" shell monkey  -p " + cf.getPackageName()
                        + " -v -v -v " + "--throttle "
                        + cf.getEventsTime() + " "
                        + cf.getRunCount() + " –-pct-touch 30.0 –-pct-motion 25.0 " +
                        "–-pct-nav 20.0 –-pct-majornav 15.0 -–pct-appswitch 5.0 " +
                        " –-pct-anyevent 5.0 –-pct-trackball 0.0 –-pct-syskeys 0.0  ");
                analyze();

            }
        });
        s.start();

    }

    /*
     *分析monkeyLog
     */
    private void analyze() {
        if (!RunMonkey.stop) {
            RunMonkey.stop = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //拉取手机文件到电脑
//            pullMonkeyLog("/sdcard/monkeyLog.txt");
            logAnalysis.FilterErrorLog(new File(System.getProperty("user.dir") + File.separator + "monkeyLog.txt"),
                    cf.getDate());
        }
    }

    /**
     * 拉取log到电脑
     */
    public  void pullMonkeyLog(String puth) {
        System.out.println("开始拉取手机文件");
        try {
            if (ConfigMonkey.devicesName == null) {
                Tooltip.errHint("RunMonkey_pullMonkeyLog获取devicesName为空");
            }
            Process pro = Runtime.getRuntime()
                    .exec("cmd /c operationAdb -s " + ConfigMonkey.devicesName + " pull "+puth+" " + System.getProperty("user.dir"));
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String msg;
            boolean deleteMonkeyLog = deleteMonkeyLog();
            boolean pull = false;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
                if (msg.contains("100%")) {
                    pull = true;
                }
            }
            boolean isMonkeyLog = isMonkeyLog();
            if ((deleteMonkeyLog && isMonkeyLog) || pull) {
            } else {
                printRunTimeLog();
                Tooltip.errHint("monkeyLog拉取失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isMonkeyLog() {
        File file = new File(System.getProperty("user.dir") + File.separator + "monkeyLog.txt");
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    /**
     * 删除本地目录下的monkeyLog
     */
    private boolean deleteMonkeyLog() {
        File file = new File(System.getProperty("user.dir") + File.separator + "monkeyLog.txt");
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                System.out.println("本地文件的monkeyLog删除失败");
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 打印运行时的log
     */
    private void printRunTimeLog() {
        BufferedWriter sc = null;
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-sss").format(new Date());

            if (GetPhoneLog.result.size() > 0 ) {
                sc = new BufferedWriter(
                        new FileWriter(System.getProperty("user.dir") + File.separator + "RunErrLong" +
                                date + ".txt"));
                sc.write("因monkeyLog拉取失败，所以打印了运行时的错误log\n");
                sc.write(GetPhoneLog.result.toString());
                sc.flush();
                sc.close();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("cmd /c explorer /select, " + System.getProperty("user.dir") + File.separator + "RunErrLong" +
                        date + ".txt");
                Tooltip.finishHint("因monkeyLog拉取失败，所以打印了运行时的错误log：" + System.getProperty("user.dir") + File.separator + "RunErrLong" +
                        date + ".txt");
            }
//            } else {
//                TooltipUtil.finishHint("拉取monkeyLog失败了，可能因为手机中断或者其他原因，所以执行了筛选运行时的log信息中，没有发现ANR");
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭monkey
     */
    private void killMoneky() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                GetPhoneLog gt = new GetPhoneLog();
                gt.getLog();
            }
        });
        t.start();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (p != 0) p = Tooltip.userSelection("关闭Monkey请点击\"是\"按钮");
            if (pidMonkey().length != 0) {
                setPid();
            }
            if (p == 0) {
                for (int i : RunMonkey.pid) {
                    adb("shell  kill " + i);
                }
                break;
            }
        }
        t.interrupt();
        analyze();
    }

    /**
     * 设置pid
     */
    public void setPid() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    RunMonkey.pid = pidMonkey();
                    if (RunMonkey.pid.length != 0) {
                        break;
                    }
                }
                killMoneky();
            }
        });
        t.start();

    }

    private static int[] pidMonkey() {
        int[] pi = new int[0];
        for (String s : adb("shell ps ")) {
            if (s.toLowerCase().contains("monkey")) {
                String[] str = s.split(" ");
                f:
                for (String st : str) {
                    if (st.matches("\\d+")) {
                        pi = Arrays.copyOf(pi, pi.length + 1);
                        pi[pi.length - 1] = Integer.parseInt(st);
                        break f;
                    }
                }
            }
        }
        return pi;
    }

    public static void adbs(String code) {
        String[] str = new String[0];
        try {
            Devices.installAPPPackage.isPuth(Config.ADB_PUTH);
            Process pro = Runtime.getRuntime().exec(Config.ADB_PUTH + " " + code);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String msg;
            File file = new File(System.getProperty("user.dir") + "\\monkeyLog.txt");
            if(file.exists()){
                file.delete();
            }else{
                file.createNewFile();
            }
            StringBuffer bf;
            FileOutputStream fos = new FileOutputStream(file, true);
            while ((msg = br.readLine()) != null) {
                bf= new StringBuffer();
                bf.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date()));
                bf.append("   ");
                bf.append(msg);
                bf.append("\n");
                fos.write(bf.toString().getBytes("utf-8"));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 执行adb命令
     *
     * @param code code
     * @return String[]
     */
    public static String[] adb(String code) {
        String[] str = new String[0];
        try {
            Devices.installAPPPackage.isPuth(Config.ADB_PUTH);
            Process pro = Runtime.getRuntime().exec(Config.ADB_PUTH + " " + code);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String msg;
            while ((msg = br.readLine()) != null) {
                if (!"".equals(msg)) {
                    str = Arrays.copyOf(str, str.length + 1);
                    str[str.length - 1] = msg;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 检查设备是否连接
     */
    public static void checkDevicesExist() {
        int j = 0;
        String[] adb = DevicesInfo.adb("devices");
        DevicesInfo.err(adb, "devices");
        for (String s : adb) {
            s = s.toLowerCase();
            if (s.endsWith("device")) {
                j++;
            }
        }
        if (j == 0) {
            Tooltip.errHint("没有找到设备", adb);
        } else if (j > 1) {
            Tooltip.errHint("请只连接一台设备", adb);
        } else {
            if (j != 1) {
                Tooltip.errHint("检查设备时，发生未知错误", adb);
            }

        }
    }

    public static void main(String[] args) {

        new Tooltip();
        System.out.println("脚本版本：v1.3");
        Object[] options = {"runMonkey", "stop_MK_And_Get_MKLog", "stop"};  //自定义按钮上的文字
        int m = JOptionPane.showOptionDialog(null, "请选择需要执行的功能", "monkey执行程序", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (m == 1) {
            checkDevicesExist();
            Crash.startCrash();
        } else if (m == 0) {
            JOptionPane.showMessageDialog(null, "请亲把耳机子插上，否则手机会发出叫声");
            new RunMonkey();
        }
    }
}
