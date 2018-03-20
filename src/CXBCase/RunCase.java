package CXBCase;
import AppTest.GetRunApp;
import AppTest.Devices;
import org.openqa.selenium.By;

public class RunCase {
    public static void main(String[] args) throws InterruptedException {
        GetRunApp.RunApp();
        Devices devices =  Devices.getDevices("开启免电");
        Thread.sleep(5000);
        devices.clickfindElement(By.id("com.mianfeia.book:id/guide3"));
        devices.clickfindElement(By.id("com.mianfeia.book:id/tab_shelf_view"));
        new Sidebar("测试侧边栏");
    }
}
