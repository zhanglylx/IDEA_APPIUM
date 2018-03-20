package AppiumMethod;

import java.util.Scanner;

public class InstallAppiumApk {
    private boolean settings = false;
    private boolean unicode_ime_apk = false;
    private boolean unlock_apk = false;

    public InstallAppiumApk() {
        System.out.println("开始执行InstallAppiumApk");
        // 检查手机中是否安装了Appium包
        checkAppiumPackage();
        install();
    }

    /**
     * 安装包
     */
    private void install() {
        if (!settings) {
            System.out.println("没有检测settings安装在手机中，开始安装--");
            DevicesInfo.adb("install -r " + GetLocalityFilePuth.getPuth("settings_apk-debug.apk"));
        }
        if (!unicode_ime_apk) {
            System.out.println("没有检测UnicodeIME安装在手机中，开始安装--");
            DevicesInfo.adb("install -r " + GetLocalityFilePuth.getPuth("UnicodeIME-debug.apk"));
        }
        if (!unlock_apk) {
            System.out.println("没有检测unlock_apk安装在手机中，开始安装--");
            DevicesInfo.adb("install -r " + GetLocalityFilePuth.getPuth("unlock_apk-debug.apk"));
        }
        checkPackage();
    }

    /**
     * 检查包是否安装成功
     */
    private void checkPackage() {
        checkAppiumPackage();
        if (!this.unicode_ime_apk) {
            errInstallCheck("unicode_ime_apk");
        }
        if (!this.unlock_apk) {
            errInstallCheck("unlock_apk");
        }
        if (!this.settings) {
            errInstallCheck("settings");
        }
    }



    @SuppressWarnings("resource")
    public static void errInstallCheck(String name){
        System.out.println("没有检测到"+name+"是否安装成功,请手动检测，继续运行请按任意键，停止运行请输入:0");
        if(new Scanner(System.in).nextLine().equals("0")){
            System.exit(0);
        }
    }
    /**
     * 检查appium需要的包是否安装在手机中
     */
    private void checkAppiumPackage() {
        for (String s : DevicesInfo.adb(" shell pm  list package -3")) {
            if (s.contains("io.appium.android.ime")) {
                this.unicode_ime_apk = true;
            } else if (s.contains("io.appium.unlock")) {
                this.unlock_apk = true;
            } else if (s.contains("io.appium.settings")) {
                this.settings = true;
            }
        }
    }
}
