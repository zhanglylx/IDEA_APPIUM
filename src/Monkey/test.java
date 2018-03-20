package Monkey;

import AppiumMethod.Config;
import AppiumMethod.installAPPPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        try {
            installAPPPackage.isPuth(Config.ADB_PUTH);
            Process pro = Runtime.getRuntime().exec(Config.ADB_PUTH + " logcat  >/sdcard/moyLog.txt" );
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String msg = null;
            ArrayList<String> ls = new ArrayList<String>();
            int ANRNum = -1;
            String packName = ConfigMonkey.getConfig("Test").getPackageName();
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
