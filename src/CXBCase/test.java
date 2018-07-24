package CXBCase;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;

public class test {
    public static void main(String args[]) throws InterruptedException {
        AndroidDriver<WebElement> driver = null;
        DesiredCapabilities cap =new DesiredCapabilities();
        cap.setCapability("automationName","appium");//appium做自动化
        cap.setCapability("deviceName","259e7c2");//设备名称
        cap.setCapability("platformVersion", "4.4.4"); //安卓操作系统版本
        cap.setCapability("platfromName","Android");//appium做Android自动化
        //cap.setCapability("udid","");
        cap.setCapability("appPackage","'com.chineseall.youzi'");
        cap.setCapability("appActivity","'com.chineseall.youzi.activity.SplashActivity'");
        cap.setCapability("restKeyboard","True");
        cap.setCapability("unicoedKyeboard","True");
        cap.setCapability("noSign","True");
        cap.setCapability("newCommandTimeout","4000");
        try {
            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),cap);//把以上配置传到appium服务端并连接手机
        }
        catch (Exception e){
            e.printStackTrace();
        }
        //driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);

        Thread.sleep(7000);
        driver.findElement(By.id("com.chineseall.youzi:id/item_home_page_background_view")).click();
        Thread.sleep(5000);
        String TEXT1=driver.findElement(By.id("com.chineseall.youzi:id/act_watch_mobile_cover_view")).getText();
        System.out.println(TEXT1);
    }
}