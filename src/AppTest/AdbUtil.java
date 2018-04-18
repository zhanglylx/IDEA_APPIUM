package AppTest;

import AppiumMethod.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class AdbUtil {
    /**
     * 执行adb命令
     *
     * @return 获取到adb返回内容的字符数组
     */
    public static String[] adb(String code) {
        String[] str = new String[0];
        try {
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
       Logs.saveLog("AdbUtil", "adb:" + Arrays.toString(str));
        RunTest.addList(  Config.ADB_PUTH + " " + code+"  adb:" + Arrays.toString(str));
        return str;
    }

}
