package CXBCase;

import AppTest.AppXmlUtil;
import AppTest.Devices;
import AppTest.Logs;
import org.openqa.selenium.By;

public class RunCase {

    /**
     * 赚积分未实现
     * 个人资料修改未实现
     * 登录、账号切换未实现
     * 阅读页评论未实现
     * 书架新增，修改文件夹未实现
     * 本地浏览记录未实现
     * 导入本地书未实现
     * 书籍管理未实现
     * 直播未实现
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Devices devices = Devices.getDevices("开启免电");
        Thread.sleep(5000);
        devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/guide3"));
        devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/tab_shelf_view"));
        initialize(devices);
//        new Sidebar("测试侧边栏").startCase();
//        initialize(devices);
//        new Search("搜索+作者详情页中的在线阅读").startCase();
//        initialize(devices);
//        new TheWorkDetails("作者详情页下载、目录、分享").startCase();
//        initialize(devices);
//        new Read("阅读页").startCase();
//        initialize(devices);
        new Read2("阅读页Read2").startCase();
        Logs.prrLogs();
        RecordAd.getRecordAd().printAd();
    }
    /**
     * 初始化到桌面
     */
    public static void initialize(Devices devices) {

        while (true) {
            if (devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/btn_left"))) {
                devices.backspace();
                if(devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/adv_plaque_view"))) RecordAd.getRecordAd().setAd("GG-3",
                        devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/banner_txt_title")));
                if("9".equals(AppXmlUtil.getXMLElement("android.widget.RelativeLayout(resource-id="+AppiumMethod.Config.APP_PACKAGE+":id/adv_plaque_view;)(index=0;)",
                        devices.getPageXml(),"index"))) RecordAd.getRecordAd().setAd("GG-43");
                return;
            }
            devices.backspace();
        }
    }
}
