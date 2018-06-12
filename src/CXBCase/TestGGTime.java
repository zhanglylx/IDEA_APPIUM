package CXBCase;

import AppTest.Devices;
import org.openqa.selenium.By;

public class TestGGTime {
    public static void main(String[] args) throws InterruptedException {
        Devices devices = Devices.getDevices("获取广告展现时间");
        Thread.sleep(8000);
        devices.clickfindElement(By.id("com.mianfeia.book:id/tab_shelf_view"));
        devices.clickScreen(931,382);
        Long time = 0l;

        for(int i=0;i<20;i++) {
            while (true) {
                //判断阅读页是否打开
              if(time!=0) {
                  if (devices.isElementExsitAndroid(By.className("android.view.View"))&&
                          !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/txt_book_name"))) {
                      System.out.println(System.currentTimeMillis() - time);
                      System.out.println(time);
                      System.out.println(System.currentTimeMillis());
                      break;
                  }
              }else{
                  break;
              }
            }
//            while (true) {
//                if (devices.isElementExsitAndroid(By.className("android.webkit.WebView"))) {
//                    System.out.println(System.currentTimeMillis() - time);
//                    break;
//                }
//            }
            devices.clickScreen(500,500);
            devices.backspace();
            Thread.sleep(2000);
            devices.clickScreen(216,298);
            time = System.currentTimeMillis();
        }
    }
}
