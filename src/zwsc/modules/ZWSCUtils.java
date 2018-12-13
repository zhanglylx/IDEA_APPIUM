package zwsc.modules;

import AppTest.Devices;
import org.openqa.selenium.By;

/**
 * 中文书城工具类
 */
public class ZWSCUtils {
    /**
     * 返回到书架起始页
     */
    public static void returnToTheBookshelfStartPage(Devices devices) {
        int i = 0;
        while (true) {
            devices.backspace();
            if (devices.isElementExsitAndroid(
                    By.id("com.chineseall.singlebook:id/dlg_exit_app_left_btn"))) {
                if ("退出".equals(devices.getText(
                        By.id("com.chineseall.singlebook:id/dlg_exit_app_left_btn")))) {
                    devices.backspace();
                    break;
                }
            }
            if (i > 20) {
                devices.snapshot("返回到书架默认起始页失败");
                throw new RuntimeException("返回到书架默认起始页失败");
            }
            i++;
        }
    }


}
