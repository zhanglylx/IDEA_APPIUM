package CXBCase;

import AppTest.Logs;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Read extends Sidebar {
    private String schedule = "进度";
    private String catalog = "目录";
    private String luminance = "亮度";
    private String bookmark = "书签";
    private String setting = "设置";
    private String[] style = {"米色", "护眼", "纸质", "宣纸", "木纹", "羊皮", "蓝灰", "粉色"};
    private String[] character = {"A-", "A+"};
    private String[] pageMode = {"仿真", "滑动", "移动"};
    private String[] volume = {"开启", "关闭"};
    //使用过的数组中的元素
    private List<String> list = new ArrayList<String>();
    private Random random = new Random();
    /**
     * 元素
     */
    //目录侧边栏目录元素id
    private String sidebar_catalog_id = "com.mianfeia.book:id/tabIndicatorView2";
    //目录侧边栏书签元素id
    private String sidebar_bookmark_id = "com.mianfeia.book:id/tabIndicatorView1";
    //目录侧边栏class
    private String directory_sidebar_class = "android.widget.RelativeLayout";
    //VIP页框架继续阅读
    private String VIP_rl_each_dialog = "com.mianfeia.book:id/rl_each_dialog";

    public Read(String caseName) {
        super(caseName);
    }

    @Override
    public void startCase() {
        Logs.recordLogs(caseName, runRead());
    }

    private boolean runRead() {
        if (!devices.clickfindElement("new UiSelector().text(\"" + CXBConfig.BOOK_NAME + "\")")) {
            print.print("检查书架中的" + CXBConfig.BOOK_NAME);
            return false;
        }
        devices.sleep(2000);
        /**
         * 关闭视听弹出框
         */
        if (devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/btn_left"))) {
            devices.clickfindElement(By.id("com.mianfeia.book:id/btn_left"));
            devices.sleep(1000);
            devices.clickScreen(devices.getWidth() / 2, devices.getHeight() / 2);
        }
        devices.sleep(1000);

        /**
         * 检查目录
         */
        if (!checkCatalogue()) return false;
        /**
         * 检查亮度
         */
        if (!luminance()) return false;
        /**
         * 检查进度
         */
        if (!progress()) return false;
        /**
         * 翻页+设置
         */
        if (!pageTurning()) return false;


        return true;

    }

    private String randValues(String key,String[] values) {
        String str = null;
        int i = 0;
        //已随机过的随机数
        List<Integer> randList = new ArrayList<Integer>();
        //判断当前值是否已获取，如果当前数组的值都被获取，返回null
        wh:while (true) {
            int n = random.nextInt(values.length);
            if (i == values.length) return null;
            str = values[n];
            for (String s : list) {
                //判断值是否被使用过
                if (s != null && s.equals(key+n)) {
                    //判断当前随机数是否出现
                    for (int randlist : randList) {
                        if (randlist == n) continue wh;
                    }
                    randList.add(n);
                    i++;
                    continue wh;
                }
            }
            list.add(key+n);
            break;
        }
        return str;
    }

    /**
     * 翻页+设置
     */
    private boolean pageTurning() {


        style(setting);
        while (true) {
            String styleStr = randValues(style.toString(),style);
            String characterStr = randValues(character.toString(),character);
            String pageModeStr = randValues(pageMode.toString(),pageMode);
            String volumeStr = randValues(volume.toString(),volume);
            if(styleStr ==null &&
                    characterStr ==null &&
                    pageModeStr ==null &&
                    volumeStr ==null ){
                    break;
            }
            if(styleStr ==null) styleStr = style[0];
            if(characterStr ==null) characterStr = character[0];
            if(pageModeStr ==null) pageModeStr = pageMode[0];
            if(volumeStr ==null) volumeStr = volume[0];
            ckickStyle(styleStr);
            clickPageMode(pageModeStr);
            clickCharacterA(characterStr);
            clickVolumeTurning(volumeStr);
            //点击屏幕中上方位置，关闭设置页
            devices.clickScreen((int)(devices.getWidth()*0.5),
                    (int)(devices.getHeight()*0.3));


        }

        return true;
    }

    //字号
    private void clickCharacterA(String Character) {
        if ("A+".equals(Character)) {
            devices.clickScreen((int) (devices.getWidth() * 0.9)
                    , (int) (devices.getHeight() * 0.7));
        } else {
            devices.clickScreen((int) (devices.getWidth() * 0.25)
                    , (int) (devices.getHeight() * 0.7));
        }
    }

    //翻页模式
    private void clickPageMode(String mode) {
        if ("仿真".equals(mode)) {
            devices.clickScreen((int) (devices.getWidth() * 0.3)
                    , (int) (devices.getHeight() * 0.78));
        } else if ("滑动".equals(mode)) {
            devices.clickScreen((int) (devices.getWidth() * 0.5)
                    , (int) (devices.getHeight() * 0.78));
        } else {
            devices.clickScreen((int) (devices.getWidth() * 0.68)
                    , (int) (devices.getHeight() * 0.78));
        }
    }

    //音量键翻页
    private void clickVolumeTurning(String volume) {
        if ("关闭".equals(volume)) {
            devices.clickScreen((int) (devices.getWidth() * 0.6)
                    , (int) (devices.getHeight() * 0.85));
        } else {
            devices.clickScreen((int) (devices.getWidth() * 0.35)
                    , (int) (devices.getHeight() * 0.85));
        }
    }

    /**
     * 风格
     */
    private void ckickStyle(String style) {
        startX = (int) (devices.getWidth() * 0.87);
        startY = (int) (devices.getHeight() * 0.6);
        endX = (int) (devices.getWidth() * 0.18);
        endY = startY;
        time = 500;
        //滑动风格到最左侧
        devices.customSlip(startX, startY, endX, endY, time);
        switch (style) {
            case "米色":
                devices.clickScreen((int) (devices.getWidth() * 0.1)
                        , (int) (devices.getHeight() * 0.6));
                break;
            case "护眼":
                devices.clickScreen((int) (devices.getWidth() * 0.25)
                        , (int) (devices.getHeight() * 0.6));
                break;
            case "纸质":
                devices.clickScreen((int) (devices.getWidth() * 0.45)
                        , (int) (devices.getHeight() * 0.6));
                break;
            case "宣纸":
                devices.clickScreen((int) (devices.getWidth() * 0.6)
                        , (int) (devices.getHeight() * 0.6));
                break;
            case "木纹":
                devices.clickScreen((int) (devices.getWidth() * 0.8)
                        , (int) (devices.getHeight() * 0.6));
                break;
            case "羊皮纸":
                devices.customSlip(endX, startY, startX, endY, time);
                devices.clickScreen((int) (devices.getWidth() * 0.57)
                        , (int) (devices.getHeight() * 0.6));
                break;
            case "蓝灰":
                devices.customSlip(endX, startY, startX, endY, time);
                devices.clickScreen((int) (devices.getWidth() * 0.75)
                        , (int) (devices.getHeight() * 0.6));
                break;
            default:
                devices.customSlip(endX, startY, startX, endY, time);
                devices.clickScreen((int) (devices.getWidth() * 0.9)
                        , (int) (devices.getHeight() * 0.6));
                break;
        }
    }


    /**
     * 检查进度
     */
    private boolean progress() {
        style(schedule);
        startX = (int) (devices.getWidth() * 0.11);
        startY = (int) (devices.getHeight() * 0.86);

        /**
         * 检查上一章按钮
         * 执行检查目录后页面停留在BOOK_CHAPTER_END页
         * 从BOOK_CHAPTER_END页开始滑动到LAST_CHAPTER_NUM
         */
        //点击上一章按钮
        for (int i = 0; i < CXBConfig.LAST_CHAPTER_NUM; i++) {
            devices.clickScreen(startX, startY);
            int clickReadmore = clickReadmore();
            if (clickReadmore == 0) {
                print.print("点击上一章检查vip页");
                return false;
            } else if (clickReadmore == 1) {
                style(schedule);
            }
        }
        startX = (int) (devices.getWidth() * 0.5);
        startY = (int) (devices.getHeight() * 0.5);
        //点击屏幕中间，关闭进度设置页
        devices.clickScreen(startX, startY);
        style(catalog);
        //检查目录中的章节是否存在BOOKCHAPTERENDLAST
        devices.sleep(3500);
        if (!devices.isElementExsitAndroid(
                "new UiSelector().text(\"" + CXBConfig.BOOK_CHAPTER_END_LAST + "\")")) {
            print.print("检查上一章按钮目录中的章节是否存在BOOK_CHAPTER_END_LAST:" + CXBConfig.BOOK_CHAPTER_END_LAST);
            return false;
        }
        devices.backspace();
        style(schedule);
        startX = (int) (devices.getWidth() * 0.9);
        startY = (int) (devices.getHeight() * 0.86);
        /**
         * 检查下一章按钮
         * 从BOOK_CHAPTER_END_LAST开始点击到+1页后检查目录中是否存在BOOK_CHAPTER_END_NEXT
         */
        for (int i = 0; i < CXBConfig.LAST_CHAPTER_NUM + 1; i++) {
            devices.clickScreen(startX, startY);
            int clickReadmore = clickReadmore();
            //点击下一章按钮不存在vip页
            if (clickReadmore == 0) {
                print.print("点击下一章按钮检查vip页");
                return false;
            } else if (clickReadmore == 1) {
                style(schedule);
            }
        }
        startX = (int) (devices.getWidth() * 0.5);
        startY = (int) (devices.getHeight() * 0.5);
        //点击屏幕中间，关闭进度设置页
        devices.clickScreen(startX, startY);
        style(catalog);
        if (!devices.isElementExsitAndroid(
                "new UiSelector().text(\"" + CXBConfig.BOOK_CHAPTER_END_NEXT + "\")")) {
            print.print("检查下一章按钮中的章节是否存在BOOK_CHAPTER_END_NEXT:" + CXBConfig.BOOK_CHAPTER_END_NEXT);
            return false;
        }
        devices.backspace();
        /**
         * 检查滑动进度条,滑动方向 <---
         * 滑动进度到第一页，然后点击最左侧的屏幕跳转到上一章
         * 检查跳转次数为LAST_CHAPTER_NUM的目录中的BOOK_CHAPTER_END_LAST是否存在
         */
        //滑动进度条由右到左
        style(schedule);
        time = 10000;
        for (int i = 0; i < CXBConfig.LAST_CHAPTER_NUM; i++) {
            startX = (int) (devices.getWidth() * 0.7);
            startY = (int) (devices.getHeight() * 0.836);
            endX = (int) (devices.getWidth() * 0.25);
            endY = startY;
            int x = endX;
            while (true) {
                endX = startX + 100;
                if (endX > x) endX = x;
                devices.customSlip(startX, startY, endX, endY, time);
                startX = endX;
                if (endX == x) break;
            }
            //点击屏幕中间，关闭设置
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.5));
            devices.sleep(500);
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.5));
            devices.sleep(500);
            //点击屏幕左侧
            devices.clickScreen((int) (devices.getWidth() * 0.1),
                    (int) (devices.getHeight() * 0.5));
            int clickReadmore = clickReadmore();
            //向上一页翻页不存在VIP页
            if (clickReadmore == 0) {
                print.print("向上一页翻页VIP页检查");
                return false;
            }
            style(schedule);
        }
        style(catalog);
        //检查目录中的章节是否存在BOOKCHAPTERENDLAST
        if (!devices.isElementExsitAndroid(
                "new UiSelector().text(\"" + CXBConfig.BOOK_CHAPTER_END_LAST + "\")")) {
            print.print("检查滑动<-目录中的章节是否存在BOOK_CHAPTER_END_LAST:" + CXBConfig.BOOK_CHAPTER_END_LAST);
            return false;
        }
        devices.backspace();
        /**
         * 检查滑动进度条,滑动方向 --->
         * 滑动进度到最后一页，然后点击最右侧的屏幕跳转到下一章
         * 检查跳转次数为LAST_CHAPTER_NUM+1的目录中的BOOK_CHAPTER_END_LAST是否存在
         */
        style(schedule);
        time = 10000;
        for (int i = 0; i < CXBConfig.LAST_CHAPTER_NUM + 1; i++) {
            startX = (int) (devices.getWidth() * 0.3);
            startY = (int) (devices.getHeight() * 0.836);
            endX = (int) (devices.getWidth() * 0.8);
            endY = startY;
            int x = endX;
            while (true) {
                endX = startX - 100;
                if (endX < x) endX = x;
                devices.customSlip(startX, startY, endX, endY, time);
                startX = endX;
                if (endX == x) break;
            }
            //点击屏幕中间，关闭设置
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.5));
            devices.sleep(500);
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.5));
            devices.sleep(500);
            //点击屏幕最右侧
            devices.clickScreen((int) (devices.getWidth() * 0.9),
                    (int) (devices.getHeight() * 0.5));
            int clickReadmore = clickReadmore();
            //向下一页翻页存在VIP页
            if (clickReadmore == 0) {
                print.print("向下一页翻页检查VIP页");
                return false;
            }
            style(schedule);
        }
        style(catalog);
        //检查目录中的章节是否存在BOOK_CHAPTER_END_NEXT
        if (!devices.isElementExsitAndroid(
                "new UiSelector().text(\"" + CXBConfig.BOOK_CHAPTER_END_NEXT + "\")")) {
            print.print("检查滑动->中的章节是否存在BOOK_CHAPTER_END_NEXT:" + CXBConfig.BOOK_CHAPTER_END_NEXT);
            return false;
        }
        devices.backspace();
        return true;
    }

    /**
     * 检查亮度
     */
    private boolean luminance() {
        //检查亮度按钮是否存在
        if (!style(luminance)) return false;
        devices.snapshot("请确认点击" + luminance + "后显示是否正确");
        devices.sleep(1000);
        //滑动亮度条到最亮
        startX = (int) (devices.getWidth() * 0.25);
        startY = (int) (devices.getHeight() * 0.83);
        endX = (int) (devices.getWidth() * 0.9);
        endY = startY;
        time = 10000;
        devices.customSlip(startX, startY, endX, endY, time);
        devices.snapshot("请检查滑动" + luminance + "到最亮后的屏幕");
        //滑动亮度条到最暗
        startX = (int) (devices.getWidth() * 0.6);
        startY = (int) (devices.getHeight() * 0.83);
        endX = (int) (devices.getWidth() * 0.15);
        endY = startY;
        time = 10000;
        devices.customSlip(startX, startY, endX, endY, time);
        devices.snapshot("请检查滑动" + luminance + "到最暗后的屏幕");
        //点击系统亮度按钮
        devices.clickScreen(((int) (devices.getWidth() * 0.93)),
                ((int) (devices.getHeight() * 0.83)));
        devices.snapshot("请检查点击系统" + luminance + "按钮");
        return true;
    }

    /**
     * 打开设置
     */
    private void setting() {
        int i = 0;
        if (devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/RelativeLayout_Item"))) {
            devices.clickScreen((int) (devices.getWidth() / 2), (int) (devices.getHeight() / 2));
            devices.clickScreen((int) (devices.getWidth() / 2), (int) (devices.getHeight() / 2));
        }
        while (true) {
            if (!devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/RelativeLayout_Item"))) {
                devices.clickScreen((int) (devices.getWidth() / 2), (int) (devices.getHeight() / 2));
            } else {
                break;
            }
            if (i > 10) return;
            i++;
        }
    }

    /**
     * 检查目录
     */
    private boolean checkCatalogue() {
        if (!style(catalog)) return false;
        devices.sleep(2000);
        /**
         * 检查目录框是否展示
         */
        if (!catalog.equals(devices.getText(By.id(sidebar_catalog_id)))
                || !bookmark.equals(devices.getText(By.id(sidebar_bookmark_id)))
                || !devices.isElementExsitAndroid(By.className(directory_sidebar_class))) {
            print.print("检查" + catalog + "是否展示");
            return false;
        }
        //点击目录
        devices.clickfindElement(By.id(sidebar_catalog_id));
        /**
         * 滑动目录由上而下到底部
         */
        startX = (int) (devices.getWidth() * 0.7);
        startY = (int) (devices.getHeight() * 0.9);
        endX = startX;
        endY = (int) (devices.getHeight() * 0.3);
        time = 500;
        slideNumber = 1;
        /**
         * 滑动到目录最后一张，检查最后一章章节的正确性
         */
        int count = CXBConfig.BOOK_CATALOGUE_SLIDE_COUNT;
        boolean chaptertitle = false;
        //用于随机上下滑动

        int[] rd = new int[(random.nextInt(10) + 1)];
        for (int j = 0; j < rd.length; j++) {
            rd[j] = (count / (random.nextInt(10) + 1));
        }
        while (true) {
            devices.customSlip(startX, startY, endX, endY, time);
            //从第count次后开始检查
            if (slideNumber == count) {
                if (devices.isElementExsitAndroid(
                        "new UiSelector().text(\"" + CXBConfig.BOOK_CHAPTER_END + "\")")) break;
                count += CXBConfig.BOOK_CATALOGUE_SLIDE_COUNT_index;
                //检查当前目录是否存在
                chaptertitle = devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/chapterlist_chaptertitle"));
                //检查循环超过2000次或者目录是否存在
                if (slideNumber > CXBConfig.BOOK_CATALOGUE_RESTRICT_SLIDE_SUM || !chaptertitle) {
                    print.print("检查" + catalog + "最后一章章节错误");
                    return false;
                }
            }
            //检查slideNumber是否在rd中
            for (int k = 0; k < rd.length; k++) {
                if (rd[k] == slideNumber) {
                    int i = random.nextInt(10) * 2 + 1;
                    int n = 0;
                    while (true) {
                        if (n == i) {
                            break;
                        }
                        if (random.nextInt(2) == 1) {
                            devices.customSlip(startX, startY, endX, endY, time);
                            devices.customSlip(startX, endY, endX, startY, time);
                            devices.customSlip(startX, endY, endX, startY, time);
                            n++;
                        } else {
                            devices.customSlip(startX, startY, endX, endY, time);
                            devices.customSlip(startX, endY, endX, startY, time);
                            n++;
                        }
                    }
                }
            }
            slideNumber++;
        }
        devices.sleep(4000);
        //点击最后一章节
        String xy = devices.getXY(
                By.xpath("//android.widget.TextView[contains(@text,\"" + CXBConfig.BOOK_CHAPTER_END + "\")]"));
        devices.clickScreen(Integer.parseInt(xy.substring(0, xy.indexOf(","))),
                Integer.parseInt(xy.substring(xy.indexOf(",") + 1, xy.length())));
        devices.sleep(1000);
        devices.snapshot("请确认点击" + catalog + "的最后一章：" + CXBConfig.BOOK_CHAPTER_END);
        /**
         * 检查点击目录页目录页是否关闭
         */
        if (devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/left"))) {
            print.print("点击目录页章节后目录没有关闭");
            return false;
        }
        /**
         * 检查VIP页是否打开
         */
        if (clickReadmore() == 0) {
            print.print("在目录页点击章节后检查VIP页是否打开");
            return false;
        }
        return true;
    }

    /**
     * 点击设置中选项
     *
     * @param name
     * @return
     */
    private boolean style(String name) {
        setting();
        if (catalog.equals(name)) {
            if (!name.equals(devices.getText(
                    By.xpath("//android.widget.RelativeLayout/android.widget.TextView")))) {
                devices.snapshot("检查设置中的" + name + "字样失败");
                print.print("检查设置中的" + name + "字样");
                return false;
            }
        } else {
            if (!devices.isElementExsitAndroid(
                    "new UiSelector().text(\"" + name + "\")")) {
                devices.snapshot("检查设置中的" + name + "字样失败");
                print.print("检查设置中的" + name + "字样");
                return false;
            }
        }
        devices.sleep(1000);
        devices.clickfindElement("new UiSelector().text(\"" + name + "\")");
        return true;
    }

    /**
     * 点击继续阅读按钮
     *
     * @return 0为页面存在后检查失败，1为存在,2为不存在
     */
    private int clickReadmore() {
        if (devices.isElementExsitAndroid(By.id(VIP_rl_each_dialog))) {

            if (!CXBConfig.BOOK_CHAPTER_END.equals(devices.getText(By.id("com.mianfeia.book:id/chapter_name"))) &&
                    devices.isElementExsitAndroid(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_1))) {
                return 0;
            }
            if (devices.isElementExsitAndroid(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_1))) {
                devices.clickfindElement(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_1));
                return 1;
            } else if (devices.isElementExsitAndroid(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_2))) {
                devices.clickfindElement(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_2));
                return 1;
            } else {
                return 0;
            }

        }
        return 2;
    }

}
