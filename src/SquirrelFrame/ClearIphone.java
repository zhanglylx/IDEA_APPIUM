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
    private String clearPackageName;
    public static final String CLEAR_CACHE ="清理缓存";
    public static final String CLEAR_FILE = "清理文件";
    public static final String CLEAR_ALL = "清理全部";
    public ClearIphone(String packageName,String clearPackageName)  {
        if (packageName == null) throw new IllegalArgumentException("packageName为空");
        this.packageName = packageName;
        this.clearPackageName =clearPackageName;
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
                rm.add ("sdcard/.hide_freebook/");
                break;
            case HomePage.ZHIBO:
                code = "com.chineseall.youzi";
                rm.add ("sdcard/.hide_live/");
                rm.add ("sdcard/.hide_freebook/");
                break;
            default:
                throw new IllegalArgumentException("未找到包名:" + packageName);
        }
        if(this.clearPackageName.equals(CLEAR_ALL) ||
                this.clearPackageName.equals(CLEAR_FILE)) {
            if(this.clearPackageName.equals(CLEAR_ALL) ) {
                if (Arrays.toString(Adb.operationAdb("uninstall " + code)).contains("errdevices")) return;
                if (checkIphoneAppPackageExist(code)) {
                    TooltipUtil.errTooltip("包卸载失败了，具体原因您查看下日志，然后自己删吧");

                }
            }
            for (String r : rm) {
                if (Arrays.toString(Adb.operationAdb("shell rm -r " + r)).contains("Is a directory")) {
                    TooltipUtil.errTooltip("本地目录删除失败了，具体原因您查看下日志，然后自己删吧");
                }
            }
            TooltipUtil.generalTooltip(packageName+":完成");
        }else{
            if(!Arrays.toString(
                    Adb.operationAdb("shell pm clear "+code)).contains("Success")
                    ){
                TooltipUtil.errTooltip("应用缓存删除失败了，具体原因您查看下日志，然后自己删吧");
                return;
            }
            TooltipUtil.generalTooltip(packageName+":完成");
        }


    }

    /**
     * 检查手机中是否存在指定的包
     * @return
     */
    public boolean checkIphoneAppPackageExist(String packageName){
        boolean exist = false;
        for(String p : Adb.operationAdb("shell pm list package")){
            if(p.contains(packageName)){
                exist=true;
                break;
            }
        }
        return exist;
    }



}
