package AppiumMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileSystemView;

public class installAPPPackage {
    /**
     * 卸载包
     */
    public static void uninstallPackge(String appPackage){
        System.out.println("开始卸载包:"+appPackage);
        String[] ad = DevicesInfo.adb("uninstall "+appPackage);
        for(String s : ad){
            System.out.println("卸载包手机返回log:"+s);
            if(s.toLowerCase().contains("Success".toLowerCase())){
                System.out.println(appPackage+" 卸载成功");
                return;
            }
            if(s.toLowerCase().contains("not installed".toLowerCase())||
                    s.toLowerCase().contains("DELETE_FAILED_INTERNAL_ERROR".toLowerCase())){
                System.out.println("没有检测到卸载是否成功，有可能是客户端没有安装包:"+appPackage);
                return;
            }

        }
        Tooltip.errHint(Config.APP_PACKAGE+"卸载失败！",ad);
    }
    /**
     * 安装包
     */
    public static void installPackage(String packagePuth,String packageName){
        System.out.println("开始安装app包:"+packagePuth);
        isPuth(packagePuth);
        String[] ad = DevicesInfo.adb("install -r  "+packagePuth);

        for(String s : ad){
            System.out.println("安装包手机返回log:"+s);
            if(s.toLowerCase().contains("Success".toLowerCase())){
                System.out.println(Config.APP_PACKAGE+"安装成功");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                };
                return;
            }
        }
        for(String s : DevicesInfo.adb(" shell pm  list package ")){
            if(s.toLowerCase().contains(packageName.toLowerCase())){
                System.out.println(Config.APP_PACKAGE+"安装成功");
                return ;
            }
        }
        Tooltip.errHint(Config.APP_PACKAGE+"安装失败！",ad);
    }
    /**
     * 查找电脑本地桌面的apk包
     */
    public static  String findPackge(){
        String msg = null;
        String s = FileSystemView
                .getFileSystemView().getHomeDirectory().getPath();
        try {
            String regStr= "^.:\\"+File.separator+".*\\"+File.separator+".+("+ Config.APP_FILE_NAME+")$";
            Pattern pattern = Pattern.compile(regStr);
            System.out.println("正在寻找"+s+File.separator+ Config.APP_PACKAGE);
            Process pro = Runtime.getRuntime().exec("cmd /c dir/s/a/b "+s+File.separator+ Config.APP_FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            while ((msg = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(msg);
                if(matcher.find()){
                    System.out.println("获取到的"+ Config.APP_FILE_NAME+"文件路径:"+msg);
                    return msg;
                }
            }
            Tooltip.errHint("没有找到"+ Config.APP_FILE_NAME+"文件路径,系统退出");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 检查文件路径是否合法
     */
    public static void isPuth(String puth){
        if(puth == null){
            Tooltip.errHint("文件路径不合法，空的:"+puth);
        }
        Pattern pattern = Pattern.compile("^[A-Z]{1}:[\\\\A-Za-z_0-9-\\.]*(\\\\[A-Za-z_0-9-]*)(\\.apk|\\.exe)$");
        Matcher matcher = pattern.matcher(puth);
        if(!matcher.find() || isChinese(puth)){
            Tooltip.errHint("文件路径或名称不合法，可能存在中文，特殊字符，请使用英文:"+puth);
        }
    }
    /**
     * 判断是否包含中文字符
     * @param strName
     * @return
     */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
}
