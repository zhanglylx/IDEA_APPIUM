package AppTest;

import AppiumMethod.Config;

import javax.swing.*;

/**
 * 用于获取需要执行的包
 */

public class GetRunAppName {
    public static int numDeBug=1;
    public static int numDIY = 1;
    public static void RunApp() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            ((Throwable) e).printStackTrace();
        }
        Object[] obj2 = {"免费电子书", "中文书城", "直播", "免费追书", "免费畅读小说","免费阅读器","追书小说大全","通过appiumConfig获取"};
        String s = (String) JOptionPane.showInputDialog(null, "请选择需要执行的包:\n", "appium", JOptionPane.PLAIN_MESSAGE, new ImageIcon("icon.png"), obj2, 0);
        switch (s) {
            case "中文书城":
                RunTest.addList("执行中文书城");
                Config.APP_PACKAGE = "com.chineseall.singlebook";
                Config.APP_ACTIVITY = "com.chineseall.reader.ui.FlashActivity";
                break;
            case "免费电子书":
                RunTest.addList("执行免费电子书");
                Config.APP_PACKAGE = "com.mianfeia.book";
                Config.APP_ACTIVITY = "com.chineseall.reader.ui.FlashActivity";
                break;
            case "直播":
                RunTest.addList("执行直播");
                Config.APP_PACKAGE = "com.chineseall.youzi";
                Config.APP_ACTIVITY = ".activity.SplashActivity";
                break;
            case "免费追书":
                RunTest.addList("执行免费追书");
                Config.APP_PACKAGE = "com.mianfeizs.book";
                Config.APP_ACTIVITY = "com.chineseall.reader.ui.FlashActivity";
                break;
            case "免费畅读小说":
                RunTest.addList("执行免费畅读小说");
                Config.APP_PACKAGE = "com.mfcdxiaoshuo.book";
                Config.APP_ACTIVITY = "com.chineseall.reader.ui.FlashActivity";
                break;
            case "追书小说大全":
                RunTest.addList("执行追书小说大全");
                Config.APP_PACKAGE = "zsxiaoshuodq.book";
                Config.APP_ACTIVITY = "com.chineseall.reader.ui.FlashActivity";
                break;
            case "免费阅读器":
                RunTest.addList("执行免费阅读器");
                Config.APP_PACKAGE = "com.mfyueduqi.book";
                Config.APP_ACTIVITY = "com.chineseall.reader.ui.FlashActivity";
                break;
            default:
                Config.getAppiumConfig();
        }
//        GetRunAppName.numDeBug =JOptionPane.showConfirmDialog(null,
//                "是否执行调试模式", "调试?",JOptionPane.YES_NO_OPTION);
//        GetRunAppName.numDIY=JOptionPane.showConfirmDialog(null,
//                "是否执行自定义模式", "自定义？",JOptionPane.YES_NO_OPTION);

    }

}
