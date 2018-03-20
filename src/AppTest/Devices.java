package AppTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import AppiumMethod.Config;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;
import AppiumMethod.DevicesInfo;
import AppiumMethod.ScreenshotsOperation;

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

    private Devices(String caseName) {
        this.caseName = caseName;
        newScreenShots();
        // 获取设备信息
        DevicesInfo info = DevicesInfo.getDevicesInfo();
        // 检查是否安装appium环境apk
//        new InstallAppiumApk();
        System.out.println("开始执行Devices");
        cap = new DesiredCapabilities();
        cap.setCapability("automationName", "Appium");// appium做自动化
        // cap.setCapability("app", "C:\\software\\jrtt.apk");//安装apk
//         cap.setCapability("browserName", "chrome");//设置HTML5的自动化，打开谷歌浏览器
        cap.setCapability("deviceName", info.getDevicesName());// 设备名称
        cap.setCapability("platformName", "Android"); // 安卓自动化还是IOS自动化
        cap.setCapability("platformVersion", info.getDevicesVersion()); // 安卓操作系统版本
        // cap.setCapability("udid", "03157df3d9998625"); // 设备的udid (adb
        // // devices 查看到的)
        cap.setCapability("appPackage", Config.APP_PACKAGE);// 被测app的包名
        System.out.println("被测app的包名:" + Config.APP_PACKAGE);
        cap.setCapability("appActivity", Config.APP_ACTIVITY);// 被测app的入口Activity名称
        System.out.println("被测app的Activity:" + Config.APP_ACTIVITY);
        cap.setCapability("unicodeKeyboard", "True"); // 支持中文输入
        cap.setCapability("resetKeyboard", "True"); // 支持中文输入，必须两条都配置
        cap.setCapability("noSign", "True"); // 不重新签名apk
        cap.setCapability("newCommandTimeout", "6000"); // 没有新命令，appium30秒退出
        start_App(cap);
    }

    /**
     * 启动APP
     */
    private static void start_App(DesiredCapabilities cap) {
        try {
            driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);
            // 隐式等待,元素未找到时等待的时间
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);//
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
            Process exec = Runtime.getRuntime().exec("adb shell am start -n " + Config.APP_PACKAGE + "/" + Config.APP_ACTIVITY);
            BufferedReader bf = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String msg = null;
            while((msg=bf.readLine())!=null){
                if(!"".equals(msg))RunTest.addList(msg);
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
        if (di == null) {
            di = new Devices(caseName);
        }
        di.caseName = caseName;
        di.newScreenShots();
        return di;
    }

    /**
     * 点击元素
     *
     * @param by
     */
    public boolean clickfindElement(By by) {
        if (isElementExsitAndroid(by)) {
            driver.findElement(by).click();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 输入字符
     *
     * @param
     */
    public boolean inputCharacter(By by, String content) {
        if (isElementExsitAndroid(by)) {
            driver.findElement(by).sendKeys(content);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取元素
     */
    public String getText(By by) {
        if (isElementExsitAndroid(by)) {
            String text = driver.findElement(by).getText();
            return text;
        } else {
            return null;
        }
    }

    /**
     * 通过坐标点击元素
     *
     * @param x
     * @param y
     * @param duration
     */
    public void clickScreen(int x, int y, int duration) {
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        HashMap<String, Integer> tapObject = new HashMap<String, Integer>();
//        tapObject.put("x", x);
//        tapObject.put("y", y);
//        tapObject.put("duration", duration);
//        js.executeScript("mobile: tap", tapObject);
        TouchAction action = new TouchAction(driver);
        action.tap(x, y).perform();
    }

    /**
     * This Method create for take screenshot 捕获截图功能
     *
     * @author Young
     */
    public void snapshot(String fileName) {
        if (!fileName.endsWith(".png")) {
            if (fileName.indexOf(".") == -1) {
                fileName = fileName + ".png";
            } else {
                fileName = fileName.substring(0, fileName.indexOf("."));
            }
        }
        so.setFileName(fileName);
        so.screenshot(driver.getScreenshotAs(OutputType.FILE));
    }

    /**
     * 验证元素是否存在
     *
     * @param elemnt
     * @return
     */
    public boolean isElementExsitAndroid(By elemnt) {
        boolean flag = false;
        try {
            WebElement element = driver.findElement(elemnt);
            flag = null != element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            flag = false;
        }
        RunTest.addList(elemnt.toString() + ":" + flag, 1);
        return flag;
    }
    public boolean isElementExsitAndroid(String elemnt) {
        boolean flag = false;
        try {
           WebElement element = driver.findElementByAndroidUIAutomator(elemnt);
            flag = null != element;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            flag = false;
        }
        RunTest.addList(elemnt.toString() + ":" + flag, 1);
        return flag;
    }
    /**
     * 点击退格键
     *
     * @return
     */
    public void backspace() {
        driver.sendKeyEvent(4);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ;
    }

    /**
     * 向左滑动
     */
    public void swipeToLeft2(int time) {
        int width = driver.manage().window().getSize().width;
//        RunTest.addList("width：" + width);
        int height = driver.manage().window().getSize().height;
//        RunTest.addList("height:" + height);
        driver.swipe(width * 3 / 4, height / 2, width / 4, height / 2,
                time);
    }

    /**
     * 向上滑动
     */
    public void swipeToUp(int time) {
        int width = driver.manage().window().getSize().width;
//        RunTest.addList("width：" + width);
        int height = driver.manage().window().getSize().height;
//        RunTest.addList("height:" + height);
        driver.swipe(width / 2, height * 3 / 4, width / 2, height / 4, time);
    }

    /**
     * Swipe to right on the screen
     */
    public void swipeToRight(int time) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 4, height / 2, width * 3 / 4, height / 2, time);
    }

    /**
     * 向下滑动
     */
    public void swipeToDown(int time) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        driver.swipe(width / 2, height / 4, width / 2, height * 3 / 4, time);
    }
    public void findUiautomatorClick(String using){

        System.out.println( driver.findElementByAndroidUIAutomator(using));


    }
    public void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
}
