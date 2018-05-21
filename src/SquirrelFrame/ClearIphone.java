package SquirrelFrame;

import Utlis.Adb;
import Utlis.TooltipUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 清理手机环境
 */
public class ClearIphone {
    private String packageName;

    public ClearIphone(String packageName)  {
        if (packageName == null) throw new IllegalArgumentException("packageName为空");
        this.packageName = packageName;
        try {
            clear();
        } catch (IllegalArgumentException E) {
            E.printStackTrace();
            TooltipUtil.errTooltip("出现了一个未知错误，请重试");
            return;
        }

    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * 清楚包和本地文件夹目录
     */
    public void clear() {
        String code = "";
        List<String> rm = new ArrayList<String>() ;
        switch (packageName) {
            case HomePage.ZWSC:
                code = "com.chineseall.singlebook";
                rm.add("sdcard/.chineseall");
                rm.add("sdcard/ChineseallReader");
                break;
            case HomePage.CXB:
                code = "com.mianfeia.book";
                rm.add("sdcard/FreeBook/");
                rm.add ("sdcard/.freebook");
                rm.add ("sdcard/.cxb");
                break;
            case HomePage.ZHIBO:
                code = "com.chineseall.youzi";
                rm.add ("sdcard/.hide_freebook/");
                rm.add ("sdcard/.hide_live/");
                break;
            default:
                throw new IllegalArgumentException("未找到包名:" + packageName);
        }
        if(Arrays.toString(Adb.adb("uninstall "+code)).contains("errdevices"))return;
        if(checkIphoneAppPackageExist(code)){
            TooltipUtil.errTooltip("包卸载失败了，具体原因您查看下日志，然后自己删吧");
        }
        for (String r : rm){
            if(Arrays.toString(Adb.adb("shell rm -r "+r)).contains("Is a directory") ){
                TooltipUtil.errTooltip("本地目录删除失败了，具体原因您查看下日志，然后自己删吧");
            }
        }
        TooltipUtil.generalTooltip(packageName+":完成");

    }

    /**
     * 检查手机中是否存在指定的包
     * @return
     */
    public boolean checkIphoneAppPackageExist(String packageName){
        boolean exist = false;
        for(String p : Adb.adb("shell pm list package")){
            if(p.contains(packageName)){
                exist=true;
                break;
            }
        }
        return exist;
    }



}
