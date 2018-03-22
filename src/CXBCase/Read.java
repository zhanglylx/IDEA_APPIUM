package CXBCase;

import AppTest.Devices;
import AppTest.Logs;
import org.openqa.selenium.By;

public class Read extends Sidebar {
    int startX, startY, endX, endY, time, slideNumber;

    public Read(String caseName) {
        super(caseName);
        Logs.recordLogs(caseName, runRead());
    }

    private boolean runRead() {
        if (!devices.clickfindElement("new UiSelector().text(\"" + CXBConfig.BOOKNAME + "\")")) return false;
        devices.sleep(2000);
        devices.snapshot("点击书架的图书");
        /**
         * 关闭视听弹出框
         */
        if (devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/btn_left"))) {
            devices.clickfindElement(By.id("com.mianfeia.book:id/btn_left"));
            devices.clickScreen(devices.getWidth() / 2, devices.getHeight() / 2);
        }

        //打开设置
        setting();
        /**
         * 检查目录
         */
        if (!checkCatalogue()) return false;
        /**
         * 检查亮度
         */
        if (!luminance()) return false;
        return true;
    }

    /**
     * 检查亮度
     */
    private boolean luminance() {
        //检查亮度按钮是否存在
        if (!devices.isElementExsitAndroid(
                "new UiSelector().text(\"亮度\")")) {
            print.print("检查亮度按钮是否存在");
            devices.snapshot("检查亮度按钮是否存在失败");
            return false;
        }
        //点击亮度
        devices.clickfindElement(By.xpath("//android.widget.RelativeLayout[contains(@index,6)]"));
        devices.snapshot("请确认点击亮度后显示是否正确");
        //滑动亮度条
        startX = (int) (devices.getWidth() * 0.25);
        startY = (int) (devices.getHeight() * 0.83);
        endX = (int) (devices.getWidth() * 0.9);
        endY =  startY;
        time = 10000;
        devices.customSlip(startX,startY,endX,endY,time);
        devices.snapshot("请检查滑动亮度到最亮后的屏幕");
        startX = (int) (devices.getWidth() * 0.6);
        startY = (int) (devices.getHeight() * 0.83);
        endX = (int) (devices.getWidth() * 0.15);
        endY =  startY;
        time = 10000;
        devices.customSlip(startX,startY,endX,endY,time);
        devices.snapshot("请检查滑动亮度到最暗后的屏幕");

        return true;
    }

    /**
     * 打开设置
     */
    private void setting() {
        if (!devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/RelativeLayout_Item"))) {
            devices.clickScreen(devices.getWidth() / 2, devices.getHeight() / 2);
        }
    }

    /**
     * 检查目录
     */
    private boolean checkCatalogue() {
        //滑动的开启与结束坐标和滑动次数
        if (!"目录".equals(devices.isElementExsitAndroid(
                By.id("//android.widget.RelativeLayout/android.widget.TextView")))) {
            devices.snapshot("检查设置中的目录字样失败");
            print.print("检查设置中的目录字样");
            return false;
        }
        devices.clickfindElement("new UiSelector().index(5)");
        /**
         * 检查目录框是否展示
         */
        if (!devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/left"))
                || !"目录".equals(devices.getText(By.id("com.mianfeia.book:id/tabIndicatorView2")))) {
            devices.snapshot("检查目录框是否展示失败");
            print.print("检查目录框是否展示");
            return false;
        }
        /**
         * 滑动目录由上而下到底部
         */
        startX = (int) (devices.getWidth() * 0.7);
        startY = (int) (devices.getHeight() * 0.3);
        endX = startX;
        endY = (int) (devices.getHeight() * 0.3);
        time = 10000;
        slideNumber = 1;
        /**
         * 滑动到目录最后一张，检查最后一章章节的正确性
         */
        while (true) {
            devices.customSlip(startX, startY, endX, endY, time);
            if (devices.isElementExsitAndroid(
                    "new UiSelector().text(\"" + CXBConfig.BOOKCHAPTEREND + "\")")) break;
            if (slideNumber == 20) {
                print.print("检查目录最后一章章节错误");
                devices.snapshot("检查目录最后一章章节错误");
                return false;
            }
            slideNumber++;
        }
        //点击最后一章节
        devices.clickfindElement(
                "new UiSelector().text(\"" + CXBConfig.BOOKCHAPTEREND + "\")");

        devices.snapshot("点击目录的最后一章：" + CXBConfig.BOOKCHAPTEREND);
        /**
         * 检查点击目录页目录页是否关闭
         */
        if (devices.isElementExsitAndroid("com.mianfeia.book:id/left")) {
            devices.snapshot("点击目录页章节后目录没有关闭");
            print.print("点击目录页章节后目录没有关闭");
            return false;
        }
        return true;
    }

}
