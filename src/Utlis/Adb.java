package Utlis;

import SquirrelFrame.HomePage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;

public class Adb {
    /**
     * 执行adb命令
     *
     * @return 获取到adb返回内容的字符数组
     */
    public static String devices;
    private static String[] runAdb(String code) {
        String[] str = new String[0];
        try {
            String dev = " ";
            if(devices!=null)dev=" -s "+devices+" ";
//            Process pro = Runtime.getRuntime().exec(AppiumMethod.Config.ADB_PUTH +dev+ code);
            Process pro = Runtime.getRuntime().exec("platform-tools"+ File.separator+"adb.exe" +dev+ code);
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
    private static String[] devicesInfo(){
        String [] devicesArr = new String[0];
        for(String s : runAdb("devices -l")){
            if(s.toUpperCase().contains("device".toUpperCase()) && s.contains("model")){
                devicesArr = Arrays.copyOf(devicesArr,devicesArr.length+1);
                devicesArr[devicesArr.length-1] = s;
            }
        }
        return devicesArr;
    }

    /**
     * 执行adb命令
     * @param code
     * @return
     */
    public static String[] operationAdb(String code) {
        //检查是否连接设备
        if(!checkDevices()) return null;
        return runAdb(code);
    }
    /**
     * 检查设备
     */
    public  static boolean checkDevices(){
        String [] deivcesInfo = devicesInfo();
        if(deivcesInfo.length==0){
            TooltipUtil.errTooltip("请至少将一台设备连接到电脑");
            return false;
        }
        if(devices==null){
            setDevices();
        }else{
            boolean dev = false;
            for(String s : deivcesInfo ){
                if(s.contains(devices))dev = true;
            }
            if(!dev){
                TooltipUtil.errTooltip("当前设备未找到，请重新选择设备");
                setDevices();
            }
        }
        return true;
    }
    /**
     * 指定设备
     */
    public static void setDevices(){
        setDevices(devicesInfo());
    }
    /**
     * 指定设备
     * @param deivcesInfo
     */
    public static void  setDevices(String[] deivcesInfo){
        if(deivcesInfo.length==0){
            if(HomePage.textArea!=null)HomePage.textArea.setText("没有连接设备");
        }else if(deivcesInfo.length==1){
            devices=
                    (deivcesInfo[0].substring(0,deivcesInfo[0].indexOf(" "))).trim();
            if(HomePage.textArea!=null)HomePage.textArea.setText(
                    deivcesInfo[0].substring(deivcesInfo[0].indexOf("model")+6,
                            deivcesInfo[0].lastIndexOf("device")
                    ));
        }else{
            String de = TooltipUtil.listSelectTooltip("发现"+deivcesInfo.length+"设备,请选择一个设备",deivcesInfo);
            devices=
                    (de.substring(0,de.indexOf(" "))).trim();
            if(HomePage.textArea!=null)HomePage.textArea.setText(
                    de.substring(de.indexOf("model")+6,
                            de.lastIndexOf("device"))
            );
        }
    }
}
