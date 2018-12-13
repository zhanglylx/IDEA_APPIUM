package CXBCase;

import AppTest.Devices;
import AppiumMethod.Config;
import ZLYUtils.TooltipUtil;
import org.openqa.selenium.By;

/**
 * 插页广告,免费追书
 * 循环展示插页广告，检查插页广告无异常
 */
public class AdInsetTest extends CaseFrame {

    public AdInsetTest(String caseName) {
        super(caseName);
    }

    @Override
    public boolean caseMap() {
        String bookName = "官梯（完整版）";
        //点击书架中的官梯（完整版）
        devices.clickfindElement(Devices.newUiSelector("text", bookName));
        //点击在线阅读
        devices.clickfindElement(By.id(Config.APP_PACKAGE + ":id/book_detail_to_read_view"));
        //点击屏幕上方，关闭黑色功能菜单浮窗
        devices.clickScreen(562, 66);
        devices.clickScreen(562, 66);
        //点击屏幕中间部分，唤起设置栏
        devices.clickScreen(this.width / 2, this.height / 2);
        //点击目录
        devices.clickfindElement("new UiSelector().text(\"目录\")");
        //点击第10章惊艳表演
        devices.clickfindElement("new UiSelector().text(\"第10章\")");

        if (devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/each_btn_nextChapter"))) {
            devices.clickfindElement(By.id(Config.APP_PACKAGE + ":id/each_btn_nextChapter"));
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //向前翻一页
        devices.clickScreen(10, this.height / 2);
        final boolean[] b = {true};
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (TooltipUtil.yesOrNo("是否关闭") == 0) {
                        b[0] = false;
                        break;
                    }
                }
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 1;
        while (b[0]) {
            //向后翻页(插页)
            devices.clickScreen(this.width - 10, this.height / 2);
            if (!bookName.equals(devices.getText(By.id(Config.APP_PACKAGE + ":id/txt_book_name")))) {
                print.printErr("检查插页的书籍名称失败");
                return false;
            }
            if (!"下一章: 第10章".equals(devices.getText(By.id(Config.APP_PACKAGE + ":id/txt_second_chapter")))) {
                print.printErr("检查插页应该为下一章:第10章");
                return false;
            }
            //广告标题与图片
            if (!checkAd(i)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!checkAd(i)) return false;
            }
            if (!devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/each_btn_nextChapter"))) {
                print.printErr("继续阅读按钮检查失败");
                return false;
            } else {
                devices.clickfindElement(By.id(Config.APP_PACKAGE + ":id/each_btn_nextChapter"));
            }
            //向前翻一页
            devices.clickScreen(10, this.height / 2);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第:" + i++ + "遍");
        }


        return true;
    }

    public boolean checkAd(int i) {
        boolean[] b = new boolean[2];
        if (devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/txt_one")) &&
                devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/img_one"))
                ) {
            RecordAd.getRecordAd().setAd("GG-31",
                    devices.getText(By.id(Config.APP_PACKAGE + ":id/txt_one")) + "[" + i + "]");
        }else if (devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/adtitle")) &&
                devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/adimg"))
                ){
            RecordAd.getRecordAd().setAd("GG-31",
                    devices.getText(By.id(Config.APP_PACKAGE + ":id/adtitle")) + "[" + i + "]");
        }else {
            if (devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/adtitle"))) {
                devices.snapshot("请手动检查是否为小图+" + i);
                RecordAd.getRecordAd().setAd("GG-31",
                        devices.getText(By.id(Config.APP_PACKAGE + ":id/adtitle")) + "[" + i + "]");
            } else {
                print.printErr("检查广告失败");
                return false;
            }
        }


        if (!devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/adtitle")) ||
                !devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/adimg"))) {
            if (devices.isElementExsitAndroid(By.id(Config.APP_PACKAGE + ":id/adtitle"))) {
                devices.snapshot("请手动检查是否为小图+" + i);
                RecordAd.getRecordAd().setAd("GG-31",
                        devices.getText(By.id(Config.APP_PACKAGE + ":id/adtitle")) + "[" + i + "]");
            } else {
                b[1] = true;
            }
        }
        if (b[0] == true && b[1] == true) {
            print.printErr("检查广告失败");
            return false;
        }
        if (b[0] == false && b[1] == true) {
            RecordAd.getRecordAd().setAd("GG-31",
                    devices.getText(By.id(Config.APP_PACKAGE + ":id/txt_one")) + "[" + i + "]");
        } else if (b[0] == true && b[1] == false) {
            RecordAd.getRecordAd().setAd("GG-31",
                    devices.getText(By.id(Config.APP_PACKAGE + ":id/adtitle")) + "[" + i + "]");
        }

        return true;
    }


    public static void main(String[] args) {
        Devices devices = Devices.getDevices("开启免电");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CXBRunCase.initialize(devices);
        new AdInsetTest("免费插页广告").startCase();
        RecordAd.getRecordAd().printAd();
    }
}