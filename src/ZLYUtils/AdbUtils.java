package ZLYUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class AdbUtils {
    /**
     * 执行adb命令
     *
     * @return 获取到adb返回内容的字符数组
     */
    public static String devices;
    private static int killNetStatAdb;
    public static long errTime = 0;
    static {
        killNetStatAdb = -1;
    }

    public static String[] runAdb(String code) {
        String[] str = new String[0];
        try {
            String dev = " ";
            if (devices != null) dev = " -s " + devices + " ";
//            Process pro = Runtime.getRuntime().exec(AppiumMethod.Config.ADB_PUTH +dev+ code);
            Process pro = Runtime.getRuntime().exec("platform-tools" + File.separator + "adb.exe" + dev + code);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream(), Charset.forName("utf-8")));
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
     * 杀死占用5037端口应用
     */
    public static void killNetStatAdb() {
        try {
            String pid = "";
            if (WindosUtils.isPortUsing("127.0.0.1", 5037)) {
                int p = -1;
                if ((p = WindosUtils.selectNetstatPid(5037)) != -1) {
                    pid = WindosUtils.getPIDName(p);
                    if (pid == null || pid.contains("adb.exe")) return;
                }
            } else {
                return;
            }
            if (killNetStatAdb != -1) {
                TooltipUtil.errTooltip("您的系统adb端口5037被占用，连接手机功能宕机\n" +
                        "占用的程序:" + pid);
                return;
            }
            String finalPid = pid;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    WindosUtils.closeProcess(finalPid);
                }
            });
            t.start();
            operationAdb("");
            killNetStatAdb++;
            killNetStatAdb();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        {

        }
    }


    /**
     * 查看设备相信信息
     *
     * @return
     */
    private static String[] devicesInfo() {
        String[] devicesArr = new String[0];
        for (String s : runAdb("devices -l")) {
            if ((s.toUpperCase().contains("device".toUpperCase()) && s.contains("model")) ||
                    s.contains("unauthorized") || s.contains("connecting")) {
                devicesArr = Arrays.copyOf(devicesArr, devicesArr.length + 1);
                devicesArr[devicesArr.length - 1] = s;
            }
        }
        return devicesArr;
    }
    /**
     * 查看设备相信信息
     *
     * @return
     */
    private static String[] checkdevicesInfo() {
        String[] devicesArr = new String[0];
        for (String s : runAdb("devices ")) {
            if ((s.toUpperCase().contains("device".toUpperCase()) && s.contains("model")) ||
                    s.contains("unauthorized") || s.contains("connecting")) {
                devicesArr = Arrays.copyOf(devicesArr, devicesArr.length + 1);
                devicesArr[devicesArr.length - 1] = s;
            }
        }
        return devicesArr;
    }
    /**
     * 执行adb命令
     *
     * @param code
     * @return
     */
    public static String[] operationAdb(String code) {
        //检查是否连接设备
        if (!checkDevices()) return null;
        return runAdb(code);
    }

    /**
     * 检查设备
     */
    public static boolean checkDevices() {
        String[] deivcesInfo = checkdevicesInfo();
        for (int i = 0; i < 2; i++) {
            if (deivcesInfo.length == 0 && i == 1  && errTime <System.currentTimeMillis()) {
                TooltipUtil.errTooltip("请至少将一台设备连接到电脑");
                errTime = System.currentTimeMillis()+(10*1000);
                return false;
            }
            deivcesInfo = devicesInfo();
        }
        if (devices == null) {
        } else {
            boolean dev = false;
            for (String s : deivcesInfo) {
                if (s.contains(devices)) dev = true;
            }
            if (!dev) {
                if(errTime <System.currentTimeMillis()){
                    TooltipUtil.errTooltip("当前设备未找到，请重新选择设备");
                    errTime = System.currentTimeMillis()+(10*1000);
                }
            }
        }
        return true;
    }




}
//