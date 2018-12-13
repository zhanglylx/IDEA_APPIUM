package AppiumMethod;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import AppTest.Devices;

import javax.swing.*;

/**
 * 录制视频，会先检查手机是否支持录制，录制视频为180秒，录制视频时会先删除手机中的视频，在开始录制，录制后会检测视频
 * 文件是否创建成功，如果不成功重试2次后停止
 * @author Administrator
 *
 */
public class ScreenRecord extends Thread {
    private String caseName;
    private static boolean succeed = false;
    public ScreenRecord(String caseName) {
        System.out.println("开始执行screenRecord");
        this.caseName = caseName;
    }
    public void startRecord(ScreenRecord sc){
        if(checkVoidSupport()){
            sc.start();
        }
    }
    public static String[] adb(String code) {
        String[] str = new String[0];
        try {
            Devices.installAPPPackage.isPuth(Config.ADB_PUTH);
            System.out.println(Config.ADB_PUTH + " " + code);
            Process pro = Runtime.getRuntime().exec(Config.ADB_PUTH + " " + code);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
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

    private boolean checkVoidSupport(){
        boolean check = false;
        String[] screenrecord = adb("shell screenrecord");
        for(String s :screenrecord){
            if(s.contains("see --help")){
                check = true;
            }
        }
        if(check){
            return true;
        }else{
            System.out.println("设备不支持视频录制"+Arrays.toString(screenrecord));
            return false;
        }

    }
    /**
     * 录制视频 如果视频文件创建失败，会重试2次
     */
    public void run() {
        deleteCaseVoidFile();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        Process pro = null;
        try {
            // 重试次数
            int i = 0;
            pro = Runtime.getRuntime()
                    .exec(Config.ADB_PUTH +" shell screenrecord /sdcard/" + caseName + ".mp4");
            while (true) {
                Thread.sleep(1000);
                if (checkScreenMp4File()) {
                    System.out.println("创建成功");
                    ScreenRecord.succeed = true;
                    break;
                } else if (i > 1) {
                    System.out.println(caseName + "视频文件创建失败");
                    break;
                }
                pro = Runtime.getRuntime().exec(
                        Config.ADB_PUTH +" shell screenrecord /sdcard/" + caseName + ".mp4");
                i++;
            }
            pro.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            pro.destroy();
            System.out.println("关闭\"" + caseName + "\"录制视频线程");
        }

    }



    /**
     * 检查视频文件是否创建成功
     *
     * @return
     */
    private boolean checkScreenMp4File() {
        try {
            Process pro = Runtime.getRuntime().exec(Config.ADB_PUTH +" shell ls /sdcard/");
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String msg = null;
            if (caseName == null) {
                return false;
            }
            while ((msg = br.readLine()) != null) {
                if ((caseName + ".mp4").equals(msg)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 删除手机中的case视频文件
     */
    private void deleteCaseVoidFile() {
        try {
            while (true) {
                Runtime.getRuntime()
                        .exec(Config.ADB_PUTH +" shell rm -r /sdcard/" + caseName + ".mp4");
                if(!checkScreenMp4File()){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public  static void pullMonkeyLog(String puth) {
        System.out.println("开始拉取手机文件");
        try {
            Process pro = Runtime.getRuntime()
                    .exec("cmd /c operationAdb  pull "+puth+" " + System.getProperty("user.dir"));
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String msg;
            boolean pull = false;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
                if (msg.contains("100%")) {
                    pull = true;
                }
            }
            if (pull) {
            } else {
                Tooltip.errHint("monkeyLog拉取失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ScreenRecord sc = new ScreenRecord("测试视频");
        sc.startRecord(sc);
        System.out.println("请等待");
        while(true) {
            System.out.println(ScreenRecord.succeed );
            if(ScreenRecord.succeed ) {
                Object[] options = {"取消", "录制结束"}; // 自定义按钮上的文字
                int m = JOptionPane.showOptionDialog(null, "开始录制视频", "标题", JOptionPane.YES_NO_OPTION,
                        JOptionPane.CANCEL_OPTION, null, options, options[1]);
                if (m == 1) {
                    Thread.sleep(1500);
                } else {
                    System.exit(0);
                }
                break;
            }
        }
        sc.interrupt();
        sc.join();
        pullMonkeyLog("/sdcard/test.mp4");
        OpenFileDirectory.selectedFile(System.getProperty("user.dir")+ File.separator+"test.mp4");

    }
}
