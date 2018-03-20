package CXBCase;

import AppTest.Devices;
import org.openqa.selenium.By;

/**
 * 测试
 */
public class Sidebar {
    Devices devices;
    PrintErr print;

    public Sidebar(String caseName) {
        devices = Devices.getDevices(caseName);
        print = new PrintErr(caseName);
        runSidebar();
    }

    private void runSidebar() {
        //点击书架左上角侧边栏按钮
        devices.clickfindElement(By.xpath("//android.widget.ImageButton[contains(@index,0)]"));
        devices.snapshot("点击书架左上角侧边栏按钮");
        /**
         *   验证侧边栏是否展示
         */
        if (!examineMenu()) {
            print.print("验证侧边栏是否展示");
            devices.snapshot("验证侧边栏是否展示");
            return;
        }
        /**
         * 验证赚积分页
         */
        if (!verificationEarnPoints("赚积分",1)) return;
        if (!verificationEarnPoints("积分商城",2)) return;
        if (!verificationEarnPoints("积分记录",3)) return;
        if (!verificationEarnPoints("我的包月",4)) return;
        if (!verificationEarnPoints("获赠记录",5)) return;
        if (!verificationEarnPoints("今日推荐",6)) return;
//        if (!verificationEarnPoints("美女直播",8)) return;
        if (!verificationEarnPoints("关于我们",9)) return;
        /**
         * 检查日夜间模式
         */
        if(!dayTimeNight())return;

    }

    private boolean examineMenu() {
        //验证侧边栏框架
        if (!devices.isElementExsitAndroid(
                By.id("com.mianfeia.book:id/slider_left_menu"))) return false;
        //验证白夜间框架
        if (!devices.isElementExsitAndroid(
                By.id("com.mianfeia.book:id/night_mode_btn"))) return false;
        return true;
    }

    /**
     * 验证赚积分页
     * 判断赚积分页title是否正确
     * @return
     */
    private boolean verificationEarnPoints(String name,int index) {
        devices.findUiautomatorClick("new UiSelector().text(\""+name+"\")");
        //点击赚积分
        if(!devices.clickfindElement(By.xpath("//android.widget.ListView/android.widget.RelativeLayout/android.widget.TextView[contains(@test,\""+name+"\")]"))){
            devices.snapshot("侧边栏"+name+"按钮不存在"+name);
            print.print("侧边栏"+name+"按钮不存在");
            return false;
        }
        devices.sleep(2000);
        devices.snapshot("点击"+name);
        /**
         * 验证赚积分页面是否展示
         */
        if (!devices.isElementExsitAndroid(
                By.xpath("//android.widget.TextView[normalize-space(@text)='"+name+"']"))) {
            print.print(name+"页面是否展示");
            devices.snapshot(name+"页面是否展示");
            return false;
        }
        //点击返回按钮
        devices.backspace();
        devices.sleep(1000);
        return true;

    }

    private boolean dayTimeNight(){
        if(!"夜间模式".equals(devices.getText(By.id("com.mianfeia.book:id/txt_eye_mode")))){
            print.print("检查夜间模式按钮是否存在");
            devices.snapshot("检查夜间模式按钮是否存在");;
            return false;
        }
        //点击白夜间按钮
        devices.clickfindElement(By.id("com.mianfeia.book:id/txt_eye_mode"));
        devices.snapshot("点击夜间模式");;
        if(!"日间模式".equals(devices.getText(By.id("com.mianfeia.book:id/txt_eye_mode")))){
            print.print("检查日间模式按钮是否存在");
            devices.snapshot("检查日间模式按钮是否存在");;
            return false;
        }
        devices.snapshot("检查日间模式按钮是否存在");;
        //置回默认值
        devices.clickfindElement(By.id("com.mianfeia.book:id/txt_eye_mode"));
        return true;
    }
}
