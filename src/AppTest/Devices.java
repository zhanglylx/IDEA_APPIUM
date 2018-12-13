package AppTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AppiumMethod.Config;
import AppiumMethod.Tooltip;
import CXBCase.CXBConfig;
import ZLYUtils.AdbUtils;
import ZLYUtils.JavaUtils;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;
import AppiumMethod.DevicesInfo;
import AppiumMethod.ScreenshotsOperation;

import javax.swing.filechooser.FileSystemView;

/**
 * 单利模式 初始化运行环境
 *
 * @author Administrator
 */

public class Devices {
    private ScreenshotsOperation so;
    private static AndroidDriver<WebElement> driver;
    private static Devices di;
    private String caseName;
    private DesiredCapabilities cap;
    private int width;
    private int height;
    //厂家名称
    private String devicesBrand;
    private DevicesInfo info;
    public static String caseNameStatic;
    private int iphoneVersion;
    private By androidErr;   //三星有时会弹出已连接的设备无法访问，直接点击确定按钮

    private Devices(String caseName) {
        this.androidErr = By.id("android:id/button1");
        GetRunAppName.RunApp();
        this.caseName = caseName;
        newScreenShots();
        // 获取设备信息
        info = DevicesInfo.getDevicesInfo();
        this.iphoneVersion = Integer.parseInt(info.getDevicesVersion().substring(0, 1));
        this.devicesBrand = info.getDevicesBrand();
        // 检查是否安装appium环境apk
        //new InstallAppiumApk();
        System.out.println("开始执行Devices");
        cap = new DesiredCapabilities();
        cap.setCapability("automationName", "Appium");// appium做自动化
        // cap.setCapability("app", "C:\\software\\jrtt.apk");//安装apk
        //cap.setCapability("browserName", "chrome");//设置HTML5的自动化，打开谷歌浏览器
        cap.setCapability("deviceName", info.getDevicesName());// 设备名称
        cap.setCapability("platformName", "Android"); // 安卓自动化还是IOS自动化
        cap.setCapability("platformVersion", info.getDevicesVersion()); // 安卓操作系统版本
        // cap.setCapability("udid", "03157df3d9998625"); // 设备的udid (operationAdb   devices 查看到的)
        cap.setCapability("appPackage", Config.APP_PACKAGE);// 被测app的包名
        System.out.println("被测app的包名:" + Config.APP_PACKAGE);
        cap.setCapability("appActivity", Config.APP_ACTIVITY);// 被测app的入口Activity名称
        System.out.println("被测app的Activity:" + Config.APP_ACTIVITY);
        cap.setCapability("unicodeKeyboard", "True"); // 支持中文输入
        cap.setCapability("resetKeyboard", "True"); // 支持中文输入，必须两条都配置
        cap.setCapability("noSign", "True"); // 不重新签名apk
        cap.setCapability("newCommandTimeout", "600000"); // 没有新命令，appium秒退出
        if (iphoneVersion > 6) cap.setCapability("automationName", "uiautomator2");
        start_App(cap);
    }

    /**
     * 启动APP
     */
    private void start_App(DesiredCapabilities cap) {
        try {
            driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
            // 隐式等待,元素未找到时等待的时间
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);//
            resetWidth_Height();
            System.out.println(width);
            System.out.println(height);
            System.out.println(info.getDevicesBrand());

        } catch (MalformedURLException e) {
            System.out.println("启动Devices发生未知错误");
            e.printStackTrace();
            System.exit(0);
        } catch (org.openqa.selenium.remote.UnreachableBrowserException e) {
            e.printStackTrace();
            System.out.println("您可能还没有启动appium！或者自己查查问题");
            System.exit(0);
        } catch (org.openqa.selenium.SessionNotCreatedException e) {
            e.printStackTrace();
            System.out.println("这个设备好像不支持或者没有解锁或者自己查查问题");
            System.exit(0);
        } catch (org.openqa.selenium.WebDriverException e) {
            e.printStackTrace();
            System.out.println("有可能包名或app启动Activity配置不正确");
            System.exit(0);
        }
    }

    /**
     * 外部调用启动APP
     */
    public void startApp() {
        driver.quit();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Process exec = Runtime.getRuntime().exec("operationAdb shell am start -n " + Config.APP_PACKAGE + "/" + Config.APP_ACTIVITY);
            BufferedReader bf = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String msg = null;
            while ((msg = bf.readLine()) != null) {
                if (!"".equals(msg)) RunTest.addList(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建截图
     */
    public void newScreenShots() {
        // 创建截图对象
        so = new ScreenshotsOperation(caseName);
    }

    public static Devices getDevices(String caseName) {
        caseName += AppiumRuningLogs.dateName;
        if (di == null) {
            di = new Devices(caseName);
        }
        di.caseName = caseName;
        di.newScreenShots();
        Devices.caseNameStatic = caseName;
        return di;
    }

    /**
     * 点击元素
     *
     * @param by
     */
    public boolean clickfindElement(By by) {
        boolean bl = false;
        if (isElementExsitAndroid(by)) {
            driver.findElement(by).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bl = true;
        }
        AppiumRuningLogs.saveLog(caseName, "clickfindElement:" + by.toString() + "=" + bl);
        return bl;
    }

    public boolean clickfindElement(String using) {
        boolean bl = false;
        if (isElementExsitAndroid(using)) {
            driver.findElementByAndroidUIAutomator(using).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bl = true;
        }
        AppiumRuningLogs.saveLog(caseName, "clickfindElement:" + using + "=" + bl);
        return bl;
    }

    /**
     * 输入字符
     *
     * @param
     */
    public boolean inputCharacter(By by, String content) {
        boolean bl = false;
        if (isElementExsitAndroid(by)) {
            driver.findElement(by).sendKeys(content);
            bl = true;
        }
        AppiumRuningLogs.saveLog(caseName, "inputCharacter:" + by.toString() + "=" + bl + "," + content);
        return bl;
    }

    public boolean inputCharacter(String using, String content) {
        boolean bl = false;
        if (isElementExsitAndroid(using)) {
            driver.findElementByAndroidUIAutomator(using).sendKeys(content);
            bl = true;
        }
        AppiumRuningLogs.saveLog(caseName, "inputCharacter:" + using + "=" + bl + "," + content);
        return bl;
    }

    /**
     * 获取元素
     */
    public String getText(By by) {
        String text = null;
        if (isElementExsitAndroid(by)) {
            text = driver.findElement(by).getText();
        }
        AppiumRuningLogs.saveLog(caseName, "getText:" + by.toString() + "=" + text);
        System.out.println("getText:" + by.toString() + "=" + text);
        return text;
    }

    public static String newUiSelector(String key, String value) {
        return "new UiSelector()." + key + "(" + "\"" + value + "\")";
    }

    public String getText(String key, String value) {
        String using = newUiSelector(key, value);
        String text = null;
        if (isElementExsitAndroid(using)) {
            text = driver.findElementByAndroidUIAutomator(using).getText();
        }
        AppiumRuningLogs.saveLog(caseName, "getText:" + using + "=" + text);
        System.out.println("getText:" + using + "=" + text);
        return text;
    }

    public String getText(String using) {
        String text = null;
        if (isElementExsitAndroid(using)) {
            text = driver.findElementByAndroidUIAutomator(using).getText();
        }
        AppiumRuningLogs.saveLog(caseName, "getText:" + using + "=" + text);
        System.out.println("getText:" + using + "=" + text);
        return text;
    }

    /**
     * 获取content-desc
     *
     * @param using
     * @return
     */
    public String getAttribute(String using) {
        String text = null;
        if (isElementExsitAndroid(using)) {
            text = driver.findElementByAndroidUIAutomator(using).getAttribute("name");
        }
        AppiumRuningLogs.saveLog(caseName, "getText:" + using + "=" + text);
        System.out.println("getAttribute:" + using + "=" + text);
        return text;
    }

    public String getAttribute(By by) {
        String text = null;
        if (isElementExsitAndroid(by)) {
            text = driver.findElement(by).getAttribute("name");
        }
        AppiumRuningLogs.saveLog(caseName, "getText:" + by.toString() + "=" + text);
        System.out.println("getAttribute:" + by.toString() + "=" + text);
        return text;
    }

    /**
     * 通过坐标点击元素
     *
     * @param x
     * @param y
     */
    public void clickScreen(int x, int y) {
        TouchAction action = new TouchAction(driver);
        action.tap(x, y).perform();
        AppiumRuningLogs.saveLog(caseName, "clickScreen:x=" + x + ",y=" + y);
        sleep(500);
    }

    public void clickScreen(int[] xy) {
        if (xy.length != 2) {
            throw new IllegalArgumentException("参数不正常:" + Arrays.toString(xy));
        }
        TouchAction action = new TouchAction(driver);
        action.tap(xy[0], xy[1]).perform();
        AppiumRuningLogs.saveLog(caseName, "clickScreen:x=" + xy[0] + ",y=" + xy[1]);
        sleep(500);
    }

    public void clickScreen(String xy) {
        if (!xy.matches("^\\d+(,)\\d+$")) {
            throw new IllegalArgumentException("参数不正常:" + xy);
        }
        ;
        String[] arr = xy.split(",");
        TouchAction action = new TouchAction(driver);
        action.tap(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])).perform();
        AppiumRuningLogs.saveLog(caseName, "clickScreen:x=" + arr[0] + ",y=" + arr[1]);
        sleep(500);
    }

    /**
     * This Method create for take screenshot 捕获截图功能
     *
     * @author Young
     */
    public void snapshot(String fileName) {
        if (!fileName.endsWith(".png")) {
            fileName = fileName + ".png";
        }
        this.so.setFileName(fileName);
        this.so.screenshot(driver.getScreenshotAs(OutputType.FILE));
    }

    /**
     * 验证元素是否存在
     *
     * @param elemnt
     * @return
     */
    public boolean isElementExsitAndroid(By elemnt) {
        //三星有时会弹出已连接的设备无法访问，直接点击确定按钮
        try {
            if (elemnt != this.androidErr &&
                    this.devicesBrand.contains(CXBConfig.modelSM)) clickfindElement(this.androidErr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean flag = false;
        try {
            WebElement element = driver.findElement(elemnt);
            flag = null != element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            flag = false;
        } catch (org.openqa.selenium.WebDriverException e) {
            flag = false;
        }
        if (elemnt == this.androidErr &&
                this.devicesBrand.contains(CXBConfig.modelSM)) {
            if (flag) {
                System.out.println("发现三星弹出错误");
                RunTest.addList(elemnt.toString() + ":" + flag, 1);
                AppiumRuningLogs.saveLog(caseName, elemnt.toString() + ":" + flag);
            }
        } else {
            RunTest.addList(elemnt.toString() + ":" + flag, 1);
            AppiumRuningLogs.saveLog(caseName, elemnt.toString() + ":" + flag);
        }
        return flag;
    }

    public boolean isElementExsitAndroid(String key, String value) {
        return isElementExsitAndroid(newUiSelector(key, value));
    }

    public boolean isElementExsitAndroid(String elemnt) {
        //三星有时会弹出已连接的设备无法访问，直接点击确定按钮
        try {
            if (!this.androidErr.toString().equals(elemnt)) clickfindElement(this.androidErr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean flag = false;
        try {
            WebElement element = driver.findElementByAndroidUIAutomator(elemnt);
            flag = null != element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            flag = false;
        } catch (org.openqa.selenium.UnsupportedCommandException e) {
            flag = false;
        } catch (org.openqa.selenium.WebDriverException e) {
            flag = false;
        }

        if (this.androidErr.toString().equals(elemnt) &&
                this.devicesBrand.contains(CXBConfig.modelSM)) {
            if (flag) {
                System.out.println("发现三星弹出错误");
                RunTest.addList(elemnt.toString() + ":" + flag, 1);
                AppiumRuningLogs.saveLog(caseName, elemnt.toString() + ":" + flag);
            }
        } else {
            RunTest.addList(elemnt.toString() + ":" + flag, 1);
            AppiumRuningLogs.saveLog(caseName, elemnt.toString() + ":" + flag);
        }
        return flag;
    }

    /**
     * 通过adb输入文字
     *
     * @return
     */
    public boolean adbInput(String text) {
        boolean b = true;
        if (AdbUtil.adb("shell input text\"" + text + "\"").length > 0) {
            b = false;
        }
        RunTest.addList("adbInput:" + b, 1);
        AppiumRuningLogs.saveLog(caseName, "adbInput" + ":" + b);
        return b;
    }

    //获取坐标
    public int[] getXY(By by) {
        int[] xy = {0, 0};
        try {
            String str = null;
            if (isElementExsitAndroid(by)) {
                str = driver.findElement(by).getLocation().toString();
                if (str != null) {
                    str = str.replace("(", "");
                    str = str.replace(")", "");
                    str = str.replace(" ", "");
                    xy[0] = Integer.parseInt(str.substring(0, str.indexOf(",")));
                    xy[1] = Integer.parseInt(str.substring(str.indexOf(",") + 1, str.length()));
                }
            }
            RunTest.addList("getXY:" + by.toString() + "=" + str);
            AppiumRuningLogs.saveLog(caseName, "getXY:" + by.toString() + "=" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xy;
    }

    public int[] getXY(String using) {
        int[] xy = {0, 0};
        try {
            String str = null;
            if (isElementExsitAndroid(using)) {
                str = driver.findElementByAndroidUIAutomator(using).getLocation().toString();
                if (str != null) {
                    str = str.replace("(", "");
                    str = str.replace(")", "");
                    str = str.replace(" ", "");
                    xy[0] = Integer.parseInt(str.substring(0, str.indexOf(",")));
                    xy[1] = Integer.parseInt(str.substring(str.indexOf(",") + 1, str.length()));
                }
            }
            RunTest.addList("getXY:" + using + "=" + str);
            AppiumRuningLogs.saveLog(caseName, "getXY:" + using + "=" + str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xy;
    }

    /**
     * 获取当前页的xml
     *
     * @return String xml
     */
    public String getPageXml() {
        String s = driver.getPageSource();
        s = JavaUtils.replaceLineBreak(s, "");
        Pattern CRLF = Pattern.compile(">(\\s)+<");
        Matcher m = CRLF.matcher(s);
        if (m.find()) {
            s = m.replaceAll("><");
        }
        AppiumRuningLogs.saveLog(caseName, "getPageXml:" + s);
        return s;
    }

    /**
     * 点击退格键
     *
     * @return
     */
    public void backspace() {
//        if (iphoneVersion > 6) {
//            AdbUtil.adb("shell input keyevent 4");
//        } else {
        driver.pressKeyCode(4);
//        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AppiumRuningLogs.saveLog(caseName, "backspace");
    }

    /**
     * 点击音量
     *
     * @volume +,-
     */
    public void clickVolume(String volume) {

        if ("+".equals(volume)) {
            if (this.iphoneVersion < 7) {
                driver.pressKeyCode(24);
            } else {
                AdbUtil.adb("shell input keyevent 24");
            }
        } else {
            if (this.iphoneVersion < 7) {
                driver.pressKeyCode(25);
            } else {
                AdbUtil.adb("shell input keyevent 25");
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AppiumRuningLogs.saveLog(caseName, "clickVolume:" + volume);
    }

    /**
     * 向左滑动
     */
    public void swipeToLeft(int time) {
        try {
            int width = driver.manage().window().getSize().width;
//        RunTest.addList("width：" + width);
            int height = driver.manage().window().getSize().height;
//        RunTest.addList("height:" + height);
            driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2,
                    time);
            AppiumRuningLogs.saveLog(caseName, "swipeToLeft:" + time + " time");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向上滑动
     */
    public void swipeToUp(int time) {
        try {
            driver.swipe(width / 2, height * 3 / 4, width / 2, height / 4, time);
            AppiumRuningLogs.saveLog(caseName, "swipeToUp:" + time + " time");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 滑动--->
     */
    public void swipeToRight(int time) {
        try {
            driver.swipe(width / 4, height / 2, width * 3 / 4, height / 2, time);
            AppiumRuningLogs.saveLog(caseName, "swipeToRight:" + time + " time");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向下滑动
     */
    public void swipeToDown(int time) {
        try {
            driver.swipe(width / 2, height / 4, width / 2, height * 3 / 4, time);
            AppiumRuningLogs.saveLog(caseName, "swipeToDown:" + time + " time");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义滑动
     *
     * @param time
     */
    public void customSlip(int startX, int startY, int endX, int endY, int time) {
        try {
            driver.swipe(startX, startY, endX, endY, time);
            AppiumRuningLogs.saveLog(caseName, "customSlip:startX:" + startX + " ;startY:" + startY
                    + " ;endX:" + endX + " ;endY:" + endY + " ;time:" + time
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void customSlip(int[] xy) {
        try {
            if (xy.length != 5) return;
            System.out.println("customSlip:startX:" + xy[0] + " ;startY:" + xy[1]
                    + " ;endX:" + xy[2] + " ;endY:" + xy[3] + " ;time:" + xy[4]
            );
            driver.swipe(xy[0], xy[1], xy[2], xy[3], xy[4]);
            AppiumRuningLogs.saveLog(caseName, "customSlip:startX:" + xy[0] + " ;startY:" + xy[1]
                    + " ;endX:" + xy[2] + " ;endY:" + xy[3] + " ;time:" + xy[4]
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 睡眠
     *
     * @param ms
     */
    public void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AppiumRuningLogs.saveLog(caseName, "sleep:" + ms + " ms");
    }

    /**
     * 关闭App
     *
     * @return
     */
    public void closeApp() {
        driver.closeApp();
    }

    public String getCaseName() {
        return caseName;
    }

    public AndroidDriver getDriver() {
        return driver;
    }

    public String getDevicesBrand() {
        return this.devicesBrand;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void resetWidth_Height() {
        this.width = driver.manage().window().getSize().width;
        this.height = driver.manage().window().getSize().height;
    }

    public int getHeight() {
        return height;
    }

    public static class installAPPPackage {
        /**
         * 卸载包
         */
        public static void uninstallPackge(String appPackage) {
            System.out.println("开始卸载包:" + appPackage);
            String[] ad = DevicesInfo.adb("uninstall " + appPackage);
            for (String s : ad) {
                System.out.println("卸载包手机返回log:" + s);
                if (s.toLowerCase().contains("Success".toLowerCase())) {
                    System.out.println(appPackage + " 卸载成功");
                    return;
                }
                if (s.toLowerCase().contains("not installed".toLowerCase()) ||
                        s.toLowerCase().contains("DELETE_FAILED_INTERNAL_ERROR".toLowerCase())) {
                    System.out.println("没有检测到卸载是否成功，有可能是客户端没有安装包:" + appPackage);
                    return;
                }

            }
            Tooltip.errHint(Config.APP_PACKAGE + "卸载失败！", ad);
        }

        /**
         * 安装包
         */
        public static void installPackage(String packagePuth, String packageName) {
            System.out.println("开始安装app包:" + packagePuth);
            isPuth(packagePuth);
            String[] ad = DevicesInfo.adb("install -r  " + packagePuth);

            for (String s : ad) {
                System.out.println("安装包手机返回log:" + s);
                if (s.toLowerCase().contains("Success".toLowerCase())) {
                    System.out.println(Config.APP_PACKAGE + "安装成功");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ;
                    return;
                }
            }
            for (String s : DevicesInfo.adb(" shell pm  list package ")) {
                if (s.toLowerCase().contains(packageName.toLowerCase())) {
                    System.out.println(Config.APP_PACKAGE + "安装成功");
                    return;
                }
            }
            Tooltip.errHint(Config.APP_PACKAGE + "安装失败！", ad);
        }

        /**
         * 查找电脑本地桌面的apk包
         */
        public static String findPackge() {
            String msg = null;
            String s = FileSystemView
                    .getFileSystemView().getHomeDirectory().getPath();
            try {
                String regStr = "^.:.*(" + Config.APP_FILE_NAME + ")$";
                Pattern pattern = Pattern.compile(regStr);
                System.out.println("正在寻找" + s + File.separator + Config.APP_PACKAGE);
                Process pro = Runtime.getRuntime().exec("cmd /c dir/s/a/b " + s + File.separator + Config.APP_FILE_NAME);
                BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                while ((msg = br.readLine()) != null) {
                    Matcher matcher = pattern.matcher(msg);
                    if (matcher.find()) {
                        System.out.println("获取到的" + Config.APP_FILE_NAME + "文件路径:" + msg);
                        return msg;
                    }
                }
                Tooltip.errHint("没有找到" + Config.APP_FILE_NAME + "文件路径,系统退出");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 检查文件路径是否合法
         */
        public static void isPuth(String puth) {
            if (puth == null) {
                Tooltip.errHint("文件路径不合法，空的:" + puth);
            }
            Pattern pattern = Pattern.compile("^[A-Z]{1}:[\\\\A-Za-z_0-9- \\.]*(\\\\[A-Za-z_0-9- ]*)(\\.apk|\\.exe)$");
            Matcher matcher = pattern.matcher(puth);
            if (!matcher.find() || isChinese(puth)) {
                Tooltip.errHint("文件路径或名称不合法，可能存在中文，特殊字符，请使用英文:" + puth);
            }
        }

        /**
         * 判断是否包含中文字符
         *
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

    /**
     * 获取手机时间
     *
     * @return
     */

    public String getIphoneDate() {
        for (String s : AdbUtils.operationAdb(" shell date")) {
            if (s.matches(".+\\d{2}:\\d{2}:\\d{2}.+")) {
                s = s.replace("CST", "");
                SimpleDateFormat sfEnd = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sfStart = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
                try {
                    return sfEnd.format(sfStart.parse(s));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
