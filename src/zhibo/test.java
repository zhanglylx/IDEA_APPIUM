package zhibo;

import AppTest.AppXmlUtil;
import AppTest.Devices;
import org.openqa.selenium.By;

public class test  {
    public static void main(String[] args) {
        Devices di =Devices.getDevices("测试");
        di.sleep(5000);
        di.clickfindElement(By.id("com.chineseall.youzi:id/tab_me_layout"));
        di.sleep(2000);
        String a = di.getAttribute(By.xpath("android.view.View[contains(index,1)]"));
        di.clickfindElement(By.name("个人资料"));
        di.clickScreen(AppXmlUtil.getXMLElement(
                "//android.view.View(resource-id=userLevel;)//android.view.View(index=1;)(content-desc=我的等级;)",
                di.getPageXml(),"bounds"))    ;                                                                                       ;
        System.out.println(a);
    }
}
