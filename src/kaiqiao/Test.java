package kaiqiao;
import AppTest.Devices;
import AppTest.GetRunApp;
import org.openqa.selenium.By;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        GetRunApp.RunApp();
        Devices devices = Devices.getDevices("锦绣大地");
        Thread.sleep(3000);
//        devices.clickfindElement(By.xpath("//android.view.View[contains(@content-desc,'清单')]"));
////        devices.clickfindElement(By.xpath("//android.view.View[@index=1]"));
//        devices.clickfindElement(By.xpath("//android.view.View[contains(@content-desc,'清单')]"));
//        devices.clickfindElement(By.xpath("//android.view.View[contains(@content-desc,'清单')]"));
//        devices.clickfindElement(By.xpath("//android.view.View[contains(@content-desc,'清单')]"));
//        devices.clickfindElement(By.xpath("//*[contains(@content-desc,'清单')]"));

        devices.clickfindElement(By.xpath("//android.view.View[contains(@content-desc,'首页')]"));
        devices.clickfindElement(By.className("android.widget.EditText"));
        Thread.sleep(3000);
        devices.clickfindElement(By.className("android.widget.EditText"));
        devices.inputCharacter(By.className("android.widget.EditText"),"沙比凯桥");


    }

}
