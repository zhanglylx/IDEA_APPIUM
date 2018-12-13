package zhibo;

import AppTest.AppXmlUtil;
import AppTest.Devices;
import CXBCase.PrintErr;
import org.openqa.selenium.By;

public class ZhiBoUtils {
    /**
     * 检查直播页面是否出现网络异常
     * @param devices
     * @return
     */
    public static boolean checkNetorkAbnormal(Devices devices){
        if(devices==null)throw  new IllegalArgumentException("devces为空");
        if("您的网络已断开，下拉屏幕重试".equals(AppXmlUtil.getXMLElement(
                "//android.widget.TextView(text=您的网络已断开，下拉屏幕重试;)",devices.getPageXml(),"text"))){
            return false;
        }
        return true;
    }

    /**
     * 回到首页
     * @param devices
     */
    public static void home(Devices devices, PrintErr print){
        //30秒
        long startTime = System.currentTimeMillis()*10*100*30;
        while (true){
            if(devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/tab_me_layout")) &&
                    devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/tab_attention_layout"))&&
                    devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/tab_me_layout"))
                    ){
                devices.clickfindElement(By.id("com.chineseall.youzi:id/tab_square_layout"));
                devices.swipeToDown(1500);
                devices.sleep(3000);
               if((!"首页".equals(devices.getText(By.id("com.chineseall.youzi:id/tab_title")))) ||
                       !checkNetorkAbnormal(devices) ){
                   print.printErr("首页检查失败");
                   System.exit(0);
               }

                break;
            }
            devices.backspace();
            if(System.currentTimeMillis()>startTime){
                print.printErr("返回30秒后没有回到直播首页");
                System.exit(0);
            }
        }
    }

    /**
     * 检查直播间
     * @param devices
     * @param print
     * @param liveName
     * @return
     */
    public static boolean checkLive(Devices devices, PrintErr print,String liveName){
        System.out.println("检查直播间");
        if(devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/next"))){
            devices.clickfindElement(By.id("com.chineseall.youzi:id/next"));
            if(devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/ok")))devices.clickfindElement(
                    By.id("com.chineseall.youzi:id/ok"));

        }
        System.out.println("检查直播间的名字");
        if(liveName==null)throw  new IllegalArgumentException("liveName为空");
        if(!liveName.equals(devices.getText(By.id("com.chineseall.youzi:id/wgt_anchor_name_view")))){
            print.printErr("直播间名字检查");
            return false;
        }
        System.out.println("检查贡献值是否出现");
        String wgt_mobile_live_pc_top_view = devices.getText(By.id("com.chineseall.youzi:id/wgt_mobile_live_pc_top_view"));
        if(wgt_mobile_live_pc_top_view==null){
            print.printErr("贡献值为空");
            return false;
        }
        if(!wgt_mobile_live_pc_top_view.startsWith("贡献值: ")){
            print.printErr("贡献值不正确");
            return false;
        }
        System.out.println("检查底部type");
        if (!devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_mobile_live_message_view"))||
                !devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_mobile_live_quality_view"))||
                !devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_mobile_live_share_view"))||
                !devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_mobile_live_car_view"))||
                !devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_mobile_live_game_view"))||
                !devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_mobile_live_gift_view"))){
            print.printErr("底部type检查");
            return false;
        }
        return true;
    }

    /**
     * 检查关注首页关注栏是否显示attentionName
     * 注意:方法中会检测是否在关注首页，如果不在将会返回到关注首页
     * @param devices
     * @param attentionName 直播间名称
     * @return
     */
    public static boolean checkAttentionDisplayedHomePage(Devices devices,PrintErr print,String attentionName){
        System.out.println("检查关注首页关注栏是否显示attentionName");
        home(devices,print);
        devices.clickfindElement(By.id("com.chineseall.youzi:id/tab_attention_layout"));
        devices.sleep(1000);
        devices.swipeToDown(1500);
        devices.sleep(5000);
        return attentionName.equals(AppXmlUtil.getXMLElement(
                "android.widget.ListView//android.view.View(text="+attentionName+";)",devices.getPageXml(),"text"));
    }
}
