package Squirrel;

import SquirrelFrame.Pane;

import javax.swing.*;

public class testTools {
    public static void invokingTestFrame(String testFrame, JDialog jdialog){
        switch (testFrame){
            case getADLog:
                new GetADLog(testFrame, jdialog);
                break;
            case leaveBug:
                new LeaveBug(leaveBug,jdialog);
                break;
            case ZhiBoTools:
                new ZhiBo(ZhiBoTools,jdialog);
                break;
        }
    }
    public static final String getADLog = "获取广告日志";
    public static final String leaveBug = "版本遗留bug";
    public static final String ZhiBoTools = "直播工具";
    public static String[] testTools  = new String[]{getADLog, leaveBug,ZhiBoTools};;
}
