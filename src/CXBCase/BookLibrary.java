package CXBCase;

import AppTest.AppXmlUtil;
import org.openqa.selenium.By;

import java.util.Random;

public class BookLibrary extends StartCase {
    public static final String schoolboy = "男生";
    public static final String schoolgirl = "女生";
    public static final String publish = "出版";
    //排行
    public static final By item_stacks_board_view = By.id("com.mianfeia.book:id/item_stacks_board_view");
    //推荐
    public static final By item_stacks_classify_tv = By.id("com.mianfeia.book:id/item_stacks_classify_tv");

    public BookLibrary(String caseName) {
        super(caseName);
    }

    @Override
    public boolean caseMap() {
        return true;
    }

    /**
     * 检查书库页和子页面
     *
     * @return
     */
    public boolean checkBookLibrary() {
        if (!checkPage(schoolboy)) return false;
        //点击女生
        devices.clickScreen(AppXmlUtil.getXMLElement
                ("android.widget.TextView(" +
                        "resource-id=com.mianfeia.book:id/item_stacks_left_tv;)" +
                        "(text=" + schoolgirl + ")", devices.getPageXml(), "bounds"));
        if (!checkPage(schoolgirl)) return false;
        //点击出版
        devices.clickScreen(AppXmlUtil.getXMLElement
                ("android.widget.TextView(" +
                        "resource-id=com.mianfeia.book:id/item_stacks_left_tv;)" +
                        "(text=" + publish + ")", devices.getPageXml(), "bounds"));
        if (!checkPage(publish)) return false;
        //点击男生
        devices.clickScreen(AppXmlUtil.getXMLElement
                ("android.widget.TextView(" +
                        "resource-id=com.mianfeia.book:id/item_stacks_left_tv;)" +
                        "(text=" + schoolboy + ")", devices.getPageXml(), "bounds"));
        if (!checkPage(schoolboy)) return false;
        return true;
    }

    /**
     * 检查书库页面
     *
     * @return
     */
    private boolean checkPage(String tab) {
        String xml = devices.getPageXml();
        if (!schoolboy.equals(
                AppXmlUtil.getXMLElement
                        ("android.widget.TextView(" +
                                "resource-id=com.mianfeia.book:id/item_stacks_left_tv;)" +
                                "(text=" + schoolboy + ")", xml, "text"))) {
            print.print("检查首页" + schoolboy + "存在");
            return false;
        }
        if (!schoolgirl.equals(
                AppXmlUtil.getXMLElement
                        ("android.widget.TextView(" +
                                "resource-id=com.mianfeia.book:id/item_stacks_left_tv;)" +
                                "(text=" + schoolgirl + ")", xml, "text"))) {
            print.print("检查首页" + schoolgirl + "存在");
            return false;
        }
        if (!publish.equals(
                AppXmlUtil.getXMLElement
                        ("android.widget.TextView(" +
                                "resource-id=com.mianfeia.book:id/item_stacks_left_tv;)" +
                                "(text=" + publish + ")", xml, "text"))) {
            print.print("检查首页" + publish + "存在");
            return false;
        }
        if (!devices.isElementExsitAndroid(item_stacks_board_view) ||
                !devices.isElementExsitAndroid(item_stacks_classify_tv)
                ) {
            print.print("检查书库页面中的排行和推荐");
            return false;
        }
        devices.clickfindElement(item_stacks_board_view);
        devices.sleep(3000);
        //检查子页面
        if (!checkSubpage(tab)) return false;
        //点击返回按钮
        if (new Random().nextInt(2) == 0) {
            devices.clickScreen(AppXmlUtil.getXMLElement(
                    "android.widget.ImageButton(index=0)", devices.getPageXml(), "bounds"));
        }else{
            //检查跳转到精品
            if (!new Sidebar(this.caseName).checkBoutiqueButton()) return false;
            devices.clickfindElement(ElementAttributes.STACK_ROOM);
        }
        //获取推荐名称
        String tv = devices.getText(item_stacks_classify_tv);
        //点击推荐
        devices.clickfindElement(item_stacks_classify_tv);
        devices.sleep(3000);
        //检查子页面
        if (!checkSubpage(tv)) return false;
        if (new Random().nextInt(2) == 0) {
            devices.clickScreen(AppXmlUtil.getXMLElement(
                    "android.widget.ImageButton(index=0)", devices.getPageXml(), "bounds"));
        }else{
            //检查跳转到精品
            if (!new Sidebar(this.caseName).checkBoutiqueButton()) return false;
            devices.clickfindElement(ElementAttributes.STACK_ROOM);
        }
        return true;
    }

    /**
     * 检查子页
     *
     * @param tab
     * @return
     */
    private boolean checkSubpage(String tab) {
        if (!devices.getText(By.className("android.widget.TextView")).contains(tab) ||
                !devices.isElementExsitAndroid(By.className("android.widget.ImageButton")) ||
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/title_right_view"))
                ) {
            print.print("检查H5子页面失败");
            return false;
        }
        return new Sidebar(this.caseName).checkNetWork();
    }
}
