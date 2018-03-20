package AppiumMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 获取设备的详情信息及adb环境 单利模式
 *
 * @author Administrator
 *
 */
public class  DevicesInfo {
    private String devicesName;
    private String devicesVersion;
    private static DevicesInfo devicesInfo;

    private DevicesInfo() {
        System.out.println("开始执行DevicesInfo");
        // 检查设备是否链接
        checkDevicesExist();
        // 检查app是否存在包
        checkPackage();
    }

    public static DevicesInfo getDevicesInfo() {
        if (devicesInfo == null) {
            devicesInfo = new DevicesInfo();
        }
        return devicesInfo;
    }

    /**
     * 检查设备是否连接，并将设备名称添加给对象
     */
    private void checkDevicesExist() {
        System.out.println("开始获取设备名称....");
        int j = 0;
        String[] adb = adb("devices");

        err(adb, "devices");
        for (String s : adb) {

            s = s.toLowerCase();
            if (s.endsWith("device")) {
                this.devicesName = s.substring(0, s.indexOf("device")).trim();
                j++;
            }
        }
        if (j == 0) {
            Tooltip.errHint("没有找到设备");
        } else if (j > 1) {
            Tooltip.errHint("请只连接一台设备");
        } else {
            if (j != 1) {
                Tooltip.errHint("检查设备时，发生未知错误", adb);
            } else {
                System.out.println("获取到的设备名称:" + this.devicesName);
            }
        }
        // 获取设备版本
        getDvicesVersion();

    }

    public static  void err(String[] adb, String errName) {
        if(errName ==null){
            errName = "errName没有传入任何参数";
        }
        if ( adb.length == 0) {
            Tooltip.errHint("获取adb(" + errName + ")时发生未知错误", adb);
        }
    }

    /*
     * 获取设备版本
     */
    private void getDvicesVersion() {
        System.out.println("开始获取设备系统版本....");
        String[] adb = adb("shell getprop ro.build.version.release");
        err(adb, "shell getprop ro.build.version.release");
        if (adb.length > 2) {
            Tooltip.errHint("获取系统版本时发生未知错误", adb);
        }
        this.devicesVersion = adb[0].trim();
        System.out.println("获取到的设备系统版本:" + this.devicesVersion);
    }

    /**
     * 执行adb命令
     *
     * @return 获取到adb返回内容的字符数组
     */
    public static String[] adb(String code) {
        String[] str = new String[0];
        try {
            installAPPPackage.isPuth(Config.ADB_PUTH);
            System.out.println(Config.ADB_PUTH + " " + code);
            Process pro = Runtime.getRuntime().exec(Config.ADB_PUTH + " " + code);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String msg = null;
            while ((msg = br.readLine()) != null) {
                str = Arrays.copyOf(str, str.length + 1);
                str[str.length - 1] = msg;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 检查被测包是否安装在手机中
     *
     * @return
     */
    private void checkPackage() {
        boolean b = false;
        for (String s : adb(" shell pm  list package ")) {
            if(Config.APP_PACKAGE==null)break;
            if (s.contains(Config.APP_PACKAGE)) {
                b = true;
            }
        }
        if (Config.PHONE_REPLACE_PACKAGE) {
            // 卸载包
            installAPPPackage.uninstallPackge(Config.APP_PACKAGE);
            // 安装包
            installAPPPackage.installPackage(installAPPPackage.findPackge(), Config.APP_PACKAGE);
        } else {
            if (!b) {
                Tooltip.errHint("手机中没有安装包：" + Config.APP_PACKAGE );
            }
        }
    }

    public String getDevicesName() {
        return devicesName;
    }

    public String getDevicesVersion() {
        return devicesVersion;
    }
}
