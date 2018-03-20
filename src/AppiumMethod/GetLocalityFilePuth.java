package AppiumMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetLocalityFilePuth {
    /**
     * 自动搜索adb文件地址
     * @return
     */
    public static String getPuth(String fileName) {
        String msg = null;
        String s = ("decfghijklmnopqrstuvwxyz");
        s=s.toUpperCase();
        char[] ch = s.toCharArray();
        try {
            String regStr= "^.:\\"+File.separator+".*("+fileName+")$";
            Pattern pattern = Pattern.compile(regStr);

            for(int i=0;i<ch.length;i++){
                System.out.println("正在寻找"+fileName+"配置路径,当前查找盘符:"+ch[i]);
                Process pro = Runtime.getRuntime().exec("cmd /c dir/s/a/b "+ch[i]+":\\"+fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                while ((msg = br.readLine()) != null) {
                    Matcher matcher = pattern.matcher(msg);
                    if(matcher.find()){
                        System.out.println("获取到的"+fileName+"文件路径:"+msg);
                        //检查文件路径是否合法
                        installAPPPackage.isPuth(msg);
                        return msg;
                    }

                }
            }
            Tooltip.errHint("没有找到"+fileName+"文件路径");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
