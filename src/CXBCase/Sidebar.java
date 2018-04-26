package CXBCase;

import AppTest.AppXmlUtil;
import AppTest.Devices;
import AppTest.Logs;
import org.openqa.selenium.By;

import java.util.Random;

/**
 * 测试侧边栏
 * 1.检测侧边栏打开后框架和日夜间切换按钮,个人头像和名称框架框架是否存在
 * 2.检查侧边栏中的赚积分、积分商城、积分记录、获赠记录、今日推荐和关于我们按钮是否存在和点击后的页面title是否展示和
 * 页面中不存在加载失败提示按钮
 * 3.检查页面右上角跳转到精品页按钮:精品按钮页检查是否出现男频和侧边栏是否关闭
 */
public class Sidebar extends StartCase {
    //书架左上角侧边栏按钮
    public static final String BOOK_SHELF_SIDEBAR = ("侧边栏按钮");
    //侧边栏中的用户名
    private final By SIDEBAR_USER_NAME = By.id("com.mianfeia.book:id/navi_name_view");
    //页面中的提示按钮
    private final By empty_view_btn = By.id("com.mianfeia.book:id/empty_view_btn");
    public static String ToEarnPoints = "赚积分";
    public static String integralShopping = "积分商城";
    public static String integralRecord = "积分记录";
    public static String MyMonthly = "我的包月";
    public static String GivenRecord = "获赠记录";
    public static String dailyRecommendation = "今日推荐";
    public static String AboutUs = "关于我们";
    public static String personalData = "个人资料";
    public static String integrationRule = "积分规则";

    public Sidebar(String caseName) {
        super(caseName);
    }

    public boolean caseMap() {
        //点击书架左上角侧边栏按钮
        devices.clickScreen(CXBConfig.chickXY(devices.getWidth(), devices.getHeight(), Sidebar.BOOK_SHELF_SIDEBAR));
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
        if (!verificationEarnPoints(ToEarnPoints, 1)) return false;
        if (!verificationEarnPoints(integralShopping, 2)) return false;
        if (!verificationEarnPoints(integralRecord, 3)) return false;
        if (!verificationEarnPoints(MyMonthly, 4)) return false;
        if (!verificationEarnPoints(GivenRecord, 5)) return false;
        if (!verificationEarnPoints(dailyRecommendation, 6)) return false;
        if (!verificationEarnPoints(AboutUs, 9)) return false;
        /**
         * 检查日夜间模式
         */
        if (!dayTimeNight()) return false;
        /**
         * 检查个人资料
         */
        if (!userData(personalData)) return false;
        //检查积分规则
        if (!integrationRule(integrationRule)) return false;
        return true;
    }

    private boolean examineMenu() {
        devices.sleep(2000);
//        //验证侧边栏框架
//        if (!devices.isElementExsitAndroid(
//                By.id("com.mianfeia.book:id/slider_left_menu"))) return false;
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
        if (integralRecord.equals(name)) {
            int i = 0;
            if (!devices.clickfindElement(By.xpath("//android.widget.TextView[contains(@text,\"兑换记录\")]"))) i++;
            devices.sleep(1500);
            if (!chickShow(name)) return false;
            if (!devices.clickfindElement(By.xpath("//android.widget.TextView[contains(@text,\"图书记录\")]"))) i++;
            devices.sleep(1500);
            if (!chickShow(name)) return false;
            if (!devices.clickfindElement(By.xpath("//android.widget.TextView[contains(@text,\"获得记录\")]"))) i++;
            devices.sleep(1500);
            if (!chickShow(name)) return false;
            if (i != 0) {
                print.print("点击积分记录中的tab按钮");
                return false;
            }
        } else if (GivenRecord.equals(name)) {
            int i = 0;
            if (!devices.isElementExsitAndroid(By.className("android.webkit.WebView"))) i++;
            if (i != 0) {
                print.print("点击获赠记录中的tab按钮");
                return false;
            }
        } else if (dailyRecommendation.equals(name)) {
            if (!devices.isElementExsitAndroid(By.className("android.webkit.WebView"))) {
                print.print("检查" + name + "中的WebView");
                return false;
            }
            if (!chickShow(name)) return false;
        } else if (MyMonthly.equals(name)) {
            if ("您还未开通包月哦".equals(devices.getText(By.id("com.mianfeia.book:id/monthly_header_title_view")))) {
                if (!"立即开通，享受免广告阅读".equals(devices.getText(By.id("com.mianfeia.book:id/monthly_header_pay_view")))) {
                    print.print("检查" + name + "中的包月记录:立即开通，享受免广告阅读按钮");
                    return false;
                }
            } else {
                print.print("检查" + name + "中的包月记录:立即开通，享受免广告阅读按钮");
                return false;
            }
            if (!devices.isElementExsitAndroid(By.xpath("//android.widget.TextView[contains(@text,\"包月记录\")]"))) {
                print.print("检查" + name + "中的包月记录");
                return false;
            }
            if (!chickShow(name)) return false;
        }


        if (devices.isElementExsitAndroid(empty_view_btn)) {
            if (integralRecord.equals(name)) {
                if ((!"赚取积分".equals(devices.getText(empty_view_btn))
                        || !"还没有获得任何积分，快去赚取吧".equals(devices.getText(By.id("com.mianfeia.book:id/empty_view_tip")))
                        || !"0\n今日已获得积分".equals(devices.getText(By.id("com.mianfeia.book:id/integral_count_view"))))) {
                    print.print(name + "中的赚取积分提示信息不正确");
                    return false;
                }
            }
        }

        if (!chickShow(name)) return false;
        //检查右上角跳转精品房子按钮
        if (integralShopping.equals(name) || integralRecord.equals(name) ||
                MyMonthly.equals(name) || GivenRecord.equals(name)
                || dailyRecommendation.equals(name) || AboutUs.equals(name)) {
            if (new Random().nextInt(5) < 3) if (!checkBoutiqueButton(name)) return false;
        } else {
            //点击返回按钮
            devices.clickfindElement(By.className("android.widget.ImageButton"));
            devices.sleep(1000);
        }
        return true;

    }

    public boolean chickShow(String name) {
        /**
         * 验证页面是否展示
         */
        if (!devices.isElementExsitAndroid(
                By.xpath("//android.widget.TextView[normalize-space(@text)='" + name + "']"))
                || !devices.isElementExsitAndroid(By.className("android.widget.ImageButton"))) {
            print.print(name + "页面是否展示");
            return false;
        }
        if (!integralRecord.equals(name)) {
            if ((devices.isElementExsitAndroid(empty_view_btn))) {
                print.print(name + "页面是否展示");
                return false;
            }
        }
        return checkNetWork();
    }

    public boolean checkNetWork() {
        if (AppXmlUtil.getXMLElement(
                "android.webkit.WebView(text=网页无法打开;)",
                devices.getPageXml(), "text").contains("网页无法打开")) {
            print.print("检查页面失败");
            return false;
        }
        return true;
    }

    /**
     * 检查切换白夜间模式按钮
     *
     * @return
     */
    private boolean dayTimeNight() {
        if (!"夜间模式".equals(devices.getText(By.id("com.mianfeia.book:id/txt_eye_mode")))) {
            print.print("检查夜间模式按钮是否存在");
            ;
            return false;
        }
        //点击白夜间按钮
        devices.clickfindElement(By.id("com.mianfeia.book:id/txt_eye_mode"));
        if (!"日间模式".equals(devices.getText(By.id("com.mianfeia.book:id/txt_eye_mode")))) {
            print.print("检查日间模式按钮是否存在");
            ;
            return false;
        }
        //置回默认值
        devices.clickfindElement(By.id("com.mianfeia.book:id/txt_eye_mode"));
        return true;
    }

    /**
     * 个人资料
     *
     * @param name
     * @return
     */
    private boolean userData(String name) {
        if ("登录".equals(devices.getText(By.id("com.mianfeia.book:id/navi_name_view")))) {
            devices.clickfindElement(By.id("com.mianfeia.book:id/navi_name_view"));
            devices.sleep(1000);
            if("登录".equals(devices.getText(By.id("com.mianfeia.book:id/navi_name_view")))){
                print.print("检查登录后");
                return false;
            }
        }
        devices.clickfindElement(By.id("com.mianfeia.book:id/head_layout"));
        if (!devices.isElementExsitAndroid(
                By.xpath("//android.widget.TextView[normalize-space(@text)='" + name + "']"))
                || devices.isElementExsitAndroid(empty_view_btn)) {
            print.print(name + "页面是否展示");
            return false;
        }
        if (!checkBoutiqueButton(name)) return false;
//        //点击切换账号
//        devices.clickScreen(CXBConfig.chickXY(Loging.switch_account));
//        //登录
//        devices.clickScreen(CXBConfig.chickXY(Loging.H5LogingUserName));
//        devices.adbInput(CXBConfig.USER_NAME);
//        devices.clickScreen(CXBConfig.chickXY(Loging.H5LogingPassword));
//        devices.adbInput(CXBConfig.PASSWORD);
//        devices.clickScreen(CXBConfig.chickXY(Loging.H5Loging));
//        /**
//         * 需要在补充
//         */
//        devices.backspace();
//        devices.sleep(500);
//        if(!CXBConfig.USER.equals(devices.getText(SIDEBAR_USER_NAME))){
//            print.print("检查登录后用户名");
//            return false;
//        }

        return true;
    }

    /**
     * 检查跳转到精品页
     *
     * @return
     */
    public boolean checkBoutiqueButton() {
        //点击右上角的房子按钮
        devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view"));
        devices.sleep(2000);
        if (examineMenu()) {
            print.print("侧边栏没有关闭");
            return false;
        }
        if (!"男频".equals(AppXmlUtil.getXMLElement(
                "//android.view.View//android.support.v7.widget.RecyclerView//" +
                        "android.widget.LinearLayout(index=2;)//android.widget.LinearLayout(index=0;)//" +
                        "android.widget.TextView(text=男频;)", devices.getPageXml(), "text"))) {
            print.print("检查跳转到精品页后没有找到男频");
            return false;
        }
        return true;
    }

    /**
     * 检查侧边栏中的精品按钮
     */
    private boolean checkBoutiqueButton(String name) {
        //点击右上角的房子按钮
        devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view"));
        devices.sleep(2000);
        if (examineMenu()) {
            print.print("点击" + name + ":精品按钮后侧边栏没有关闭");
            return false;
        }
        devices.sleep(5000);
        if (!"男频".equals(AppXmlUtil.getXMLElement(
                "//android.view.ViewGroup//android.support.v7.widget.RecyclerView//" +
                        "android.widget.LinearLayout//android.widget.LinearLayout//" +
                        "android.widget.TextView(text=男频;)", devices.getPageXml(), "text"))) {
            print.print("检查跳转到精品页后没有找到男频");
            return false;
        }
        devices.backspace();
        //点击书架左上角侧边栏按钮
        devices.clickScreen(CXBConfig.chickXY(devices.getWidth(), devices.getHeight(), Sidebar.BOOK_SHELF_SIDEBAR));
        if (!examineMenu()) {
            print.print("点击" + name + ":精品按钮后再次返回到侧边栏没有展开");
            return false;
        }
        return true;
    }

    /**
     * 积分规则
     */
    private boolean integrationRule(String name) {
        String navi_integral_view = devices.getText(By.id("com.mianfeia.book:id/navi_integral_view"));
        //com.mianfeia.book:id/navi_integral_view连续登录id
        if (navi_integral_view == null || !navi_integral_view.contains("连续登录")) {
            print.print("检查" + name + "中的连续登录失败");
            return false;
        }
        //点击连续登录
        devices.clickfindElement(By.id("com.mianfeia.book:id/navi_integral_view"));
        devices.sleep(1500);
        if (!chickShow(name)) return false;
        if (!checkBoutiqueButton(name)) return false;
        return true;
    }
}
