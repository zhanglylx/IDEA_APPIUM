package CXBCase;
import AppTest.GetRunApp;
import AppTest.Devices;
import AppTest.Logs;
import org.openqa.selenium.By;

public class RunCase {
    public static void main(String[] args) throws InterruptedException {
        GetRunApp.RunApp();
        Devices devices =  Devices.getDevices("开启免电");
        Thread.sleep(5000);
        devices.clickfindElement(By.id("com.mianfeia.book:id/guide3"));
        devices.clickfindElement(By.id("com.mianfeia.book:id/tab_shelf_view"));

        new Sidebar("测试侧边栏");
        initialize(devices);






        Logs.prrLogs();
    }

    /**
     * 初始化到桌面
     */
    public static void initialize(Devices devices){
        while(true){
            if(devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/btn_left"))){
                devices.clickfindElement(By.id("com.mianfeia.book:id/btn_left"));
                return;
            }
            devices.backspace();
        }
    }
}
