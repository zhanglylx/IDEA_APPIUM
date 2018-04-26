package AppiumMethod;


import AppTest.Devices;

import java.io.*;
import java.util.ArrayList;
import javax.swing.filechooser.FileSystemView;

public class Config {
    static {
        ObjectInput ois = null;
        ObjectOutputStream ous = null;

        try {
            if (new File("adb_puth.dat").exists()) {
                ois = new ObjectInputStream(new FileInputStream("adb_puth.dat"));
                Config.ADB_PUTH = (String) ois.readObject();
            }
            if (null == Config.ADB_PUTH || !(new File(Config.ADB_PUTH).exists())) {
                Config.ADB_PUTH = GetLocalityFilePuth.getPuth("adb.exe");
                ous = new ObjectOutputStream(new FileOutputStream("adb_puth.dat"));
                ous.writeObject(Config.ADB_PUTH);
                ous.flush();
            }

            Devices.installAPPPackage.isPuth(Config.ADB_PUTH);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ous != null) {
                try {
                    ous.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("本地adb路径：" + Config.ADB_PUTH);
        }
    }

    // adb.exe文件地址,不用动，已经配置好，自动搜索，如果不需要请填写路径
//    public static String ADB_PUTH = GetLocalityFilePuth.getPuth("adb.exe");
    public static String ADB_PUTH;
    // app文件包名
    public static String APP_PACKAGE;
    // app启动Activity
    public static String APP_ACTIVITY;
    // 截图是否替换
    public static boolean SCREEN_SHOTS_REPLACE = false;
    // app在本地的名称，用于安装app，请将文件放置在桌面或子文件夹中
    public static final String APP_FILE_NAME = "1063.apk";
    // 是否覆盖手机中的包
    public static final boolean PHONE_REPLACE_PACKAGE = false;
    // 保存截图地址,默认地址是桌面log文件夹
    public static final String IMAGES_B = FileSystemView.getFileSystemView().getHomeDirectory() + File.separator
            + "log";
    public static ArrayList<String> appiumConfig;

    public static void getAppiumConfig() {
        File file = new File(System.getProperty("user.dir") + "\\appiumConfig.txt");

        if (!file.isFile() || !file.exists()) {
            appiumConifg();
            BufferedWriter fos = null;
            try {
                fos = new BufferedWriter(new FileWriter(file));
                for (String str : appiumConfig) {
                    fos.write(str);
                }
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
                Tooltip.errHint("创建appiumConfig文件发生异常，创建失败");
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Runtime runtime = Runtime.getRuntime();
                try {
//                    runtime.exec("cmd /c explorer /select, " + file.getPath());
                    runtime.exec("cmd /c " + file.getPath());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } // 打开转换完成目录
                Tooltip.finishHint("appiumConfig创建成功，请填写完毕后重新运行程序");
            }
        } else {
            BufferedReader br = null;
            try {
                br = new BufferedReader(
                        new FileReader(file));
                String msg = null;
                while ((msg = br.readLine()) != null) {
                    String[] str = msg.split("=");
                    switch (str[0].trim()) {
                        case "packageName":
                            APP_PACKAGE = str[1].trim();
                            break;
                        case "Activity":
                            APP_ACTIVITY = str[1].trim();
                            break;
                        case "Is the screenshot replaced，true/false":
                            if ("true".equals(str[1].trim().toLowerCase())) SCREEN_SHOTS_REPLACE = true;
                            break;
                    }
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (Devices.installAPPPackage.isChinese(APP_FILE_NAME) || !APP_FILE_NAME.matches("[0-9A-Za-z-()_]+(\\.apk)")) {
            print(APP_FILE_NAME);
        }
        if (Devices.installAPPPackage.isChinese(IMAGES_B)) {
            print(IMAGES_B);
        }
    }

    public static void appiumConifg() {
        appiumConfig = new ArrayList<String>();
        appiumConfig.add("packageName = ");
        appiumConfig.add("\n");
        appiumConfig.add("Activity = ");
//        appiumConfig.add("\n");
//        appiumConfig.add("Is the screenshot replaced，true/false = ");
        appiumConfig.add("\n");
        appiumConfig.add("截图保存地址:");
        appiumConfig.add(IMAGES_B);
    }


    private static void print(String errName) {
        Tooltip.errHint(errName + "名字不合法， 包含中文或者特殊字符");
    }
}
