package CXBCase;

import AppTest.Devices;
import AppTest.Logs;
import org.openqa.selenium.By;

/**
 * 测试侧边栏
 * 1.检测侧边栏打开后框架和日夜间切换按钮,个人头像和名称框架框架是否存在，存在代表检测通过
 * 2.检查侧边栏中的赚积分、积分商城、积分记录、获赠记录、今日推荐和关于我们按钮是否存在和点击后的页面title是否展示
 */
public class Sidebar {
    int startX, startY, endX, endY, time, slideNumber;
    Devices devices;
    PrintErr print;
    String caseName;
    public Sidebar(String caseName) {
        this.caseName = caseName;
        devices = Devices.getDevices(caseName);
        print = new PrintErr(caseName);

    }
    public void startCase(){
        Logs.recordLogs(caseName,runSidebar());
    }

    private boolean runSidebar() {
        //点击书架左上角侧边栏按钮
        devices.clickfindElement(By.xpath("//android.widget.ImageButton[contains(@index,0)]"));
        /**
         *   验证侧边栏是否展示
         */
        if (!examineMenu()) {
            print.print("验证侧边栏是否展示");
            return false;
        }
        /**
         * 验证赚积分页
         */
        if (!verificationEarnPoints("赚积分", 1)) return false;
        if (!verificationEarnPoints("积分商城", 2)) return false;
        if (!verificationEarnPoints("积分记录", 3)) return false;
        if (!verificationEarnPoints("我的包月", 4)) return false;
        if (!verificationEarnPoints("获赠记录", 5)) return false;
        if (!verificationEarnPoints("今日推荐", 6)) return false;
//        if (!verificationEarnPoints("美女直播",8)) return;
        if (!verificationEarnPoints("关于我们", 9)) return false;
        /**
         * 检查日夜间模式
         */
        if (!dayTimeNight()) return false;
        /**
         * 检查个人资料
         */
        if(!userData("个人资料"))return false;
        return true;
    }

    private boolean examineMenu() {
        //验证侧边栏框架
        if (!devices.isElementExsitAndroid(
                By.id("com.mianfeia.book:id/slider_left_menu"))) return false;
        //验证白夜间框架
        if (!devices.isElementExsitAndroid(
                By.id("com.mianfeia.book:id/night_mode_btn"))) return false;
        if (!devices.isElementExsitAndroid(By.id("" +
                "com.mianfeia.book:id/head_layout"))) return false;
        if (!devices.isElementExsitAndroid(By.id(
                "com.mianfeia.book:id/integral_layout"))) return false;
        return true;
    }

    /**
     * 验证侧边栏页
     * 判断侧边栏中的tab页中的title是否正确
     *
     * @return
     */
    private boolean verificationEarnPoints(String name, int index) {
        //点击name
        if (!devices.clickfindElement("new UiSelector().text(\"" + name + "\")")) {
            print.print("侧边栏" + name + "按钮不存在");
            return false;
        }
        devices.sleep(2000);
        /**
         * 验证页面是否展示
         */
        if (!devices.isElementExsitAndroid(
                By.xpath("//android.widget.TextView[normalize-space(@text)='" + name + "']"))
                || devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/empty_view_btn"))) {
            print.print(name + "页面是否展示");
            return false;
        }
        //点击返回按钮
        devices.backspace();
        devices.sleep(1000);
        return true;

    }

    private boolean dayTimeNight() {
        if (!"夜间模式".equals(devices.getText(By.id("com.mianfeia.book:id/txt_eye_mode")))) {
            print.print("检查夜间模式按钮是否存在");;
            return false;
        }
        //点击白夜间按钮
        devices.clickfindElement(By.id("com.mianfeia.book:id/txt_eye_mode"));
        if (!"日间模式".equals(devices.getText(By.id("com.mianfeia.book:id/txt_eye_mode")))) {
            print.print("检查日间模式按钮是否存在");;
            return false;
        }
        //置回默认值
        devices.clickfindElement(By.id("com.mianfeia.book:id/txt_eye_mode"));
        return true;
    }

    /**
     * 个人资料
     * @param name
     * @return
     */
    private boolean userData(String name){
        devices.clickfindElement(By.id("com.mianfeia.book:id/head_layout"));
        if (!devices.isElementExsitAndroid(
                By.xpath("//android.widget.TextView[normalize-space(@text)='" + name + "']"))
                || devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/empty_view_btn"))) {
            print.print(name + "页面是否展示");
            return false;
        }
        return true;
    }
}
