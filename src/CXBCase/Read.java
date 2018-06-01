package CXBCase;

import org.openqa.selenium.By;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 1.检查阅读页
 * 2.目录检查:随机向上向下滑动，检查目录页是否存在
 * 2.1滑动到目录底部，检查config中的章节是否存在
 * 2.2点击config中的章节，检查是否跳转到章节
 * 3.进度检查:向上向下和滑动上下，检查翻页到指定的章节后，章节是否存在
 * 4.亮度检查:调至最高、最暗和跟随系统亮度，截图手工检查
 * 5.设置检查，每一项设置后翻页，检查翻页无问题
 * 6.末尾推送页，检查推送页和返回书架三种方式
 */
public class Read extends StartCase {
    public static final String schedule = "进度";
    public static final String catalog = "目录";
    public static final String luminance = "亮度";
    public static final String bookmark = "书签";
    public static final String setting = "设置";
    public static final String download = "下载";
    private final String[] style = {"米色", "护眼", "纸质", "宣纸", "木纹", "羊皮", "蓝灰", "粉色"};
    private final String[] character = {"A-", "A+"};
    private final String[] pageMode = {"仿真", "滑动", "移动"};
    private final String[] volume = {"开启", "关闭"};
    //使用过的数组中的元素
    private List<String> list;

    {
        list = new ArrayList<>();
    }

    private Random random = new Random();
    private int randHight = (int) (devices.getHeight() * ((random.nextInt(4) + 4) * 0.1));
    /**
     * 元素
     */
    //目录侧边栏目录元素id
    private String sidebar_catalog_id = AppiumMethod.Config.APP_PACKAGE+":id/tabIndicatorView2";
    //目录侧边栏书签元素id
    private String sidebar_bookmark_id = AppiumMethod.Config.APP_PACKAGE+":id/tabIndicatorView1";
    //目录侧边栏class
    private String directory_sidebar_class = "android.widget.RelativeLayout";
    //VIP页框架继续阅读
    private String VIP_rl_each_dialog = AppiumMethod.Config.APP_PACKAGE+":id/rl_each_dialog";
    //末章推送页title框架id
    private String Unsealed_push_frame = AppiumMethod.Config.APP_PACKAGE+":id/title_bar_view";
    //末章推送页title
    private String Unsealed_push_title = "已读完《" + CXBConfig.BOOK_NAME + "》";
    //末章推送页title_class
    private String Unsealed_push_title_class = "android.widget.TextView";
    //末章推送页首页按钮id
    private String Unsealed_push_title_right_view_id = AppiumMethod.Config.APP_PACKAGE+":id/title_right_view";
    //末章推送页返回按钮
    private String Unsealed_push_title_right_view_back = "new UiSelector().className(" +
            "\"android.widget.LinearLayout\").childSelector(new UiSelector().className(\"android.widget.ImageButton\").index(" +
            "0))";

    //判断音量翻页是否关闭
    private boolean volumeBoolean = false;

    public Read(String caseName) {
        super(caseName);
    }

    public boolean caseMap() {
            if(!new Search(this.caseName).addBook(CXBConfig.BOOK_NAME,CXBConfig.BOOK_AUTHOR))return false;
            if (!clickChapter()) return false;
        devices.sleep(2000);
        //关闭视听框
        if (devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/btn_left"))) {
            devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/btn_left"));
            devices.sleep(1000);
            devices.clickScreen(devices.getWidth() / 2, devices.getHeight() / 2);
        }
        devices.sleep(2000);
        if(devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/banner_txt_title")))ad.setAd("GG-30");
        /**
         * 检查目录
         * 吊起设置页，检查目录按钮是否存在
         * 打开目录页，检查目录页是否显示
         * 向下翻页，随机向上翻页
         * 翻页到目录最底页，检查配置中的最后一页章节是否存在
         */
        if (!checkCatalogue()) return false;
        /**
         * 检查亮度
         * 滑动到最亮，截图，通过人工检查最亮是否生效
         * 滑动到最暗，截图，通过人工检查最暗是否生效
         * 点击跟随系统亮度，截图，通过人工检查跟随系统亮度是否生效
         */
        if (!luminance()) return false;
        /**
         * 检查进度
         * 点击上一章，根据配置中设置的页数进行翻章，然后打开目录，检查配置中的上一章是否存在
         * 点击下一章，根据上一章设置页数+1，打开目录，检查配置中的下一章是否存在
         * 向上滑动进度，然后关闭设置页，点击页面左侧，然后打开目录，检查配置中的上一章是否存在
         * 向下滑动进度，根据上一章翻页数+1，然后打开目录，检查配置中的下一章是否存在
         */
        if (!progress()) return false;
        /**
         * 翻页+设置+末页章节推送
         * 向左向右随机翻页，设置中的随机选择，循环到章节末尾，检查末页章节是否存在
         * 存在章节推送后检查章节推送页和章节推送页返回到书架的三种方式
         */
        if (!pageTurning()) return false;
        return true;

    }

    private String randValues(String key, String[] values) {
        String str;
        int i = 0;
        //已随机过的随机数
        List<Integer> randList = new ArrayList<Integer>();
        //判断当前值是否已获取，如果当前数组的值都被获取，返回null
        wh:
        while (true) {
            int n = random.nextInt(values.length);
            if (i == values.length) return null;
            str = values[n];
            for (String s : list) {
                //判断值是否被使用过
                if (s != null && s.equals(key + n)) {
                    //判断当前随机数是否出现
                    for (int randlist : randList) {
                        if (randlist == n) continue wh;
                    }
                    randList.add(n);
                    i++;
                    continue wh;
                }
            }
            list.add(key + n);
            break;
        }
        return str;
    }

    /**
     * 检查末章推送
     *
     * @return 出现检查失败返回0，出现检查成功返回1;未出现返回2;
     */
    private int chapterNotPush() {
        System.out.print("执行检查末章推送：");
        int i = 1;
        if (devices.isElementExsitAndroid(By.id(Unsealed_push_frame))) {
            devices.sleep(3000);
            if (!Unsealed_push_title.equals(devices.getText(By.className(Unsealed_push_title_class)))) i = 0;
            if (!devices.isElementExsitAndroid(By.id(Unsealed_push_title_right_view_id))) i = 0;
            if (!devices.isElementExsitAndroid(Unsealed_push_title_right_view_back)) i = 0;
            if (i == 0) {
                System.out.println("未章推送检测不成功:" + i);
                return i;
            }
            System.out.println("末章推送检查成功:" + i);
            return i;
        }
        i = 2;
        System.out.println("没有出现末章推送" + i);
        return i;
    }

    /**
     * 点击书架中的书籍
     *
     * @return
     */
    private boolean clickChapter() {
        devices.sleep(1500);
        System.out.println("点击书架中的书籍:" + CXBConfig.BOOK_NAME);
        if (!devices.clickfindElement("new UiSelector().text(\"" + CXBConfig.BOOK_NAME + "\")")) {
            print.print("检查书架中的" + CXBConfig.BOOK_NAME);
            return false;
        }
        return true;
    }

    /**
     * 翻页+设置+末页推送
     */
    private boolean pageTurning() {
        if (!style(setting)) return false;
        //统计向左向右滑动的次数，用于判断是否到达章节末尾
        int slideRight = 0;
        int slideLeft = 0;
        boolean listClear = false;
        while (true) {
            String styleStr = randValues(style.toString(), style);
            String characterStr = randValues(character.toString(), character);
            String pageModeStr = randValues(pageMode.toString(), pageMode);
            String volumeStr = randValues(volume.toString(), volume);
            if (styleStr == null &&
                    characterStr == null &&
                    pageModeStr == null &&
                    volumeStr == null) {
                System.out.println("清空了list");
                list.clear();
                listClear = true;
            }
            if (styleStr == null) styleStr = style[random.nextInt(style.length)];
            if (characterStr == null) characterStr = character[random.nextInt(character.length)];
            if (pageModeStr == null) pageModeStr = pageMode[random.nextInt(pageMode.length)];
            if (volumeStr == null) volumeStr = volume[random.nextInt(volume.length)];
            chcikSetting(styleStr,pageModeStr,characterStr,volumeStr);
            //点击屏幕中上方位置，关闭设置页
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.3));
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.3));
            int n = random.nextInt(8);
            switch (n) {
                case 0:
                    //滑动--->
                    devices.swipeToRight((random.nextInt(10) + 1) * 100);
                    slideLeft++;
                    System.out.println("swipeToLeft");
                    break;
                case 1:
                    //滑动<---
                    devices.swipeToLeft((random.nextInt(10) + 1) * 100);
                    slideRight++;
                    System.out.println("swipeToRight");
                    break;
                case 2:
                    //点击音量+
                    devices.clickVolume("+");
                    System.out.println("clickVolume:+");
                    if (!volumeBoolean) {
                        //滑动--->
                        devices.swipeToRight((random.nextInt(10) + 1) * 100);
                        slideLeft++;
                        System.out.println("swipeToRight");
                    }
                    break;
                case 3:
                    //点击音量-
                    devices.clickVolume("-");
                    System.out.println("clickVolume:-");
                    if (!volumeBoolean) {
                        //滑动<---
                        devices.swipeToLeft((random.nextInt(10) + 1) * 100);
                        slideRight++;
                        System.out.println("swipeToRight");
                    }
                    break;
                case 4:
                    //点击左滑动
                    devices.clickScreen((int) (devices.getWidth() * 0.1),
                            randHight);
                    slideLeft++;
                    System.out.println("点击左滑动");
                    break;
                default:
                    //滑动<---
                    devices.clickScreen((int) (devices.getWidth() * 0.9),
                            randHight);
                    slideRight++;
                    System.out.println("点击右滑动");
            }

            //用于提高速度
            if (listClear) {
                if (clickReadmore() == 0) {
                    print.print("VIP页检查");
                    return false;
                }
                for (int i = 0; i < (random.nextInt(10) + 10) * 10; i++) {
                    if (chapterNotPush() != 2) break;
                    //点击右滑动
                    if (random.nextInt(2) == 0) {
                        devices.clickScreen((int) (devices.getWidth() * 0.9),
                                randHight);
                        System.out.println("点击右滑动");
                    } else {
                        devices.swipeToLeft((random.nextInt(10) + 1) * 100);
                        System.out.println("swipeToRight");
                    }
                    slideRight++;
                    if (clickReadmore() == 0) {
                        print.print("VIP页检查");
                        return false;
                    }

                }
            }
            if (clickReadmore() == 0) {
                print.print("VIP页检查");
                return false;
            }
            int chapterNotPush = chapterNotPush();
            if (chapterNotPush == 0) {
                /*
                 * 以下代码为检查在末章推送页返回到书架时的三种方式是否正确
                 */
                print.print("检查末章推送");
                return false;
            } else if (chapterNotPush == 1) {
                //点击退格键返回到书架首页
                devices.backspace();
                //点击书架
                if (!clickChapter()) return false;
                devices.sleep(1000);
                //滑动<---
                devices.swipeToLeft((random.nextInt(10) + 1) * 100);
                devices.sleep(1000);
                //滑动<---
                devices.swipeToLeft((random.nextInt(10) + 1) * 100);
                if (chapterNotPush() != 1) {
                    print.print("检查末章推送");
                    return false;
                }
                //点击Unsealed_push_title_right_view_id
                devices.clickfindElement(By.id(Unsealed_push_title_right_view_id));
                devices.sleep(3000);
                devices.backspace();
                devices.sleep(1000);
                //点击书架中的书籍
                if (!clickChapter()) return false;
                devices.sleep(1000);
                //滑动<---
                devices.swipeToLeft((random.nextInt(10) + 1) * 100);
                devices.sleep(1000);
                //滑动<---
                devices.swipeToLeft((random.nextInt(10) + 1) * 100);
                if (chapterNotPush() != 1) {
                    print.print("检查末章推送");
                    return false;
                }
                devices.clickfindElement(Unsealed_push_title_right_view_back);
                if (!clickChapter()) return false;
                break;
            } else {
                if (!style(setting)) return false;
            }
            if ((slideRight - slideLeft) > 2000) {
                print.print("循环向右点击2000次后未达到章节末尾");
                return false;
            }
        }
        //随机向前翻1到10页
        for (int i = 0; i < (random.nextInt(100)) + 10; i++) {
            devices.clickScreen((int) (devices.getWidth() * 0.1),
                    randHight);
            int clickReadmore = clickReadmore();
            if (clickReadmore == 0) {
                print.print("点击上一章检查vip页");
                return false;
            } else if (clickReadmore == 1) {
                i--;
            }
        }
        if (!style(setting)) return false;
        return true;

    }

    //字号
    private void clickCharacterA(String Character) {
        int n = random.nextInt(50) + 1;
        if ("A+".equals(Character)) {
            if (random.nextInt(10) > 2) {
                System.out.println("点击字号A+:" + n);
                for (int i = 0; i < n; i++) {
                    devices.clickScreen((int) (devices.getWidth() * 0.9)
                            , (int) (devices.getHeight() * 0.7));
                }
            } else {
                System.out.println("点击字号A+:1");
                devices.clickScreen((int) (devices.getWidth() * 0.9)
                        , (int) (devices.getHeight() * 0.7));

            }

        } else {
            if (random.nextInt(10) > 2) {
                System.out.println("点击字号A-:" + n);
                for (int i = 0; i < n; i++) {
                    devices.clickScreen((int) (devices.getWidth() * 0.25)
                            , (int) (devices.getHeight() * 0.7));
                }
            } else {
                System.out.println("点击字号A-:1");
                devices.clickScreen((int) (devices.getWidth() * 0.25)
                        , (int) (devices.getHeight() * 0.7));
            }

        }
    }

    //翻页模式
    private void clickPageMode(String mode) {
        if ("仿真".equals(mode)) {
            System.out.println("点击仿真");
            devices.clickScreen((int) (devices.getWidth() * 0.3)
                    , (int) (devices.getHeight() * 0.78));
        } else if ("滑动".equals(mode)) {
            System.out.println("点击滑动");
            devices.clickScreen((int) (devices.getWidth() * 0.5)
                    , (int) (devices.getHeight() * 0.78));
        } else {
            System.out.println("点击移动");
            devices.clickScreen((int) (devices.getWidth() * 0.68)
                    , (int) (devices.getHeight() * 0.78));
        }
    }

    //音量键翻页
    private void clickVolumeTurning(String volume) {
        if ("关闭".equals(volume)) {
            System.out.println("点击音量键关闭");
            devices.clickScreen((int) (devices.getWidth() * 0.6)
                    , (int) (devices.getHeight() * 0.85));
            volumeBoolean = false;
        } else {
            System.out.println("点击音量键开启");
            devices.clickScreen((int) (devices.getWidth() * 0.35)
                    , (int) (devices.getHeight() * 0.85));
            volumeBoolean = true;
        }
    }

    /**
     * 风格
     */
    private void chcikSetting(String styleStr,String pageModeStr,String characterStr,String volumeStr) {
        //单独适配
        if(devices.getDevicesBrand().contains(CXBConfig.modelSM))devices.setHeight(devices.getHeight()+130);
        Style(styleStr);
        devices.resetWidth_Height();
        if(devices.getDevicesBrand().contains(CXBConfig.modelSM))devices.setHeight(devices.getHeight()+50);
        clickCharacterA(characterStr);
        clickPageMode(pageModeStr);
        devices.resetWidth_Height();
        if(devices.getDevicesBrand().contains(CXBConfig.modelSM))devices.setHeight(devices.getHeight()+40);
        clickVolumeTurning(volumeStr);
        devices.resetWidth_Height();

    }
    private void Style(String style) {
        startX = (int) (devices.getWidth() * 0.87);
        startY = (int) (devices.getHeight() * 0.6);
        endX = (int) (devices.getWidth() * 0.18);
        endY = startY;
        time = 500;
        //滑动风格到最左侧
        devices.customSlip(endX, startY, startX, endY, time);
        switch (style) {
            case "米色":
                devices.clickScreen((int) (devices.getWidth() * 0.1)
                        , (int) (devices.getHeight() * 0.6));
                System.out.println("点击米色");
                break;
            case "护眼":
                devices.clickScreen((int) (devices.getWidth() * 0.25)
                        , (int) (devices.getHeight() * 0.6));
                System.out.println("点击护眼");
                break;
            case "纸质":
                devices.clickScreen((int) (devices.getWidth() * 0.45)
                        , (int) (devices.getHeight() * 0.6));
                System.out.println("点击纸质");
                break;
            case "宣纸":
                devices.clickScreen((int) (devices.getWidth() * 0.6)
                        , (int) (devices.getHeight() * 0.6));
                System.out.println("点击宣纸");
                break;
            case "木纹":
                devices.clickScreen((int) (devices.getWidth() * 0.8)
                        , (int) (devices.getHeight() * 0.6));
                System.out.println("点击木纹");
                break;
            case "羊皮纸":
                devices.customSlip(startX, startY, endX, endY, time);
                devices.clickScreen((int) (devices.getWidth() * 0.57)
                        , (int) (devices.getHeight() * 0.6));
                System.out.println("点击羊皮纸");
                break;
            case "蓝灰":
                devices.customSlip(startX, startY, endX, endY, time);
                devices.clickScreen((int) (devices.getWidth() * 0.75)
                        , (int) (devices.getHeight() * 0.6));
                System.out.println("点击蓝灰");
                break;
            default:
                devices.customSlip(startX, startY, endX, endY, time);
                devices.clickScreen((int) (devices.getWidth() * 0.9)
                        , (int) (devices.getHeight() * 0.6));
                System.out.println("点击粉色");
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
        /*
         * 检查滑动进度条,滑动方向 <---
         * 滑动进度到第一页，然后点击最左侧的屏幕跳转到上一章
         * 检查跳转次数为LAST_CHAPTER_NUM的目录中的BOOK_CHAPTER_END_LAST是否存在
         */
        //滑动进度条由右到左
        style(schedule);
        time = 10000;
        for (int i = 0; i < CXBConfig.LAST_CHAPTER_NUM + 1; i++) {
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
            if (!style(schedule)) return false;
        }
        if (!style(catalog)) return false;
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
        if(devices.getDevicesBrand().contains(CXBConfig.modelSM)){
            devices.clickScreen(((int) (devices.getWidth() * 0.93)),
                    ((int) (devices.getHeight() * 0.85)));

        }else{
            devices.clickScreen(((int) (devices.getWidth() * 0.93)),
                    ((int) (devices.getHeight() * 0.83)));
        }
        devices.snapshot("请检查开启系统" + luminance + "按钮");
        return true;
    }

    /**
     * 打开设置
     */
    private boolean setting() {
        int i = 0;
        if (devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/RelativeLayout_Item"))) {
            devices.clickScreen((int) (devices.getWidth() / 2), (int) (devices.getHeight() / 2));
            devices.clickScreen((int) (devices.getWidth() / 2), (int) (devices.getHeight() / 2));
        }
        while (true) {
            if (!devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/RelativeLayout_Item"))) {
                devices.clickScreen((int) (devices.getWidth() / 2), (int) (devices.getHeight() / 2));
            } else {
                break;
            }
            if (i > 10) {
                print.print("找不到设置");
                return false;
            }
            i++;
        }
        return true;
    }


    public boolean checkChapterFirst() {
        if (!CXBConfig.BOOK_CHAPTER_FIRST.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/chapterlist_chaptertitle")))) {
            print.print("检查目录第一章节");
            return false;
        }
        return true;
    }

    /**
     * 外部调用检查目录
     * @return
     */
    public boolean checkCatalogueExternal(){
        devices.sleep(2000);
        //关闭视听框
        if (devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/btn_left"))) {
            devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/btn_left"));
            devices.sleep(1000);
            devices.clickScreen(devices.getWidth() / 2, devices.getHeight() / 2);
        }
        devices.sleep(1000);
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
        devices.sleep(2000);
        //检查第一个章节是否正确
        if (!checkChapterFirst()) return false;
        return true;

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
        devices.sleep(2000);
        //检查第一个章节是否正确
        if (!checkChapterFirst()) return false;
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
                chaptertitle = devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/chapterlist_chaptertitle"));
                //检查循环超过slideNumber次或者目录是否存在
                if (slideNumber > CXBConfig.BOOK_CATALOGUE_RESTRICT_SLIDE_SUM || !chaptertitle) {
                    print.print("检查" + catalog + "最后一章章节错误");
                    return false;
                }
            }
            //检查slideNumber是否在rd中，存在随机向上滑动
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
        int[] xy = devices.getXY(
                By.xpath("//android.widget.TextView[contains(@text,\"" + CXBConfig.BOOK_CHAPTER_END + "\")]"));
        devices.clickScreen(xy[0], xy[1]);
        devices.sleep(1000);
        devices.snapshot("请确认点击" + catalog + "的最后一章：" + CXBConfig.BOOK_CHAPTER_END);
        /**
         * 检查点击目录页目录页是否关闭
         */
        if (devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/left"))) {
            print.print("点击目录页章节后目录没有关闭");
            return false;
        }
        /**
         * 检查VIP页是否打开
         */
        if (clickReadmore("目录") == 0) {
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
    public boolean style(String name) {
        if(clickReadmore()==0)return false;
        if (!setting()) return false;
        if (catalog.equals(name)) {
            if (!name.equals(devices.getText(
                    By.xpath("//android.widget.RelativeLayout/android.widget.TextView")))) {
                print.print("检查设置中的" + name + "字样");
                return false;
            }
        } else {
            if (Read.bookmark.equals(name) || Read.download.equals(name)) {
                //点击+按钮
                if (!devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/btn_more"))) {
                    print.print("检查设置中的+按钮字样");
                    return false;
                }
                devices.sleep(300);
                if (Read.bookmark.equals(name)) {
                    if (!devices.clickfindElement("new UiSelector().text(\"添加书签\")")) {
                        print.print("检查设置中的" + name + "字样");
                        return false;
                    }
                } else {
                    if (!devices.clickfindElement("new UiSelector().text(\"下载\")")) {
                        print.print("检查设置中的" + name + "字样");
                        return false;
                    }
                }
            } else {
                if (!devices.isElementExsitAndroid(
                        "new UiSelector().text(\"" + name + "\")")) {
                    print.print("检查设置中的" + name + "字样");
                    return false;
                }
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
    public int clickReadmore() {
        if(devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/adtitle")))ad.setAd("GG-31",
                devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/adtitle")));
        if (devices.isElementExsitAndroid(By.id(VIP_rl_each_dialog))) {
            if (devices.isElementExsitAndroid(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_1))) {
                devices.clickfindElement(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_1));
                if (new Loging(this.caseName).checkWeChatLoginPage()) devices.backspace();
                return 1;
            } else if (devices.isElementExsitAndroid(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_2))) {
                devices.clickfindElement(By.id(CXBConfig.BOOK_VIP_CHAPTER_NEXT_CHAPTER_2));
                if (new Loging(this.caseName).checkWeChatLoginPage()) devices.backspace();
                return 1;
            } else {
                return 0;
            }

        }
        return 2;
    }

    /**
     * 点击继续阅读按钮
     *
     * @return 0为页面存在后检查失败，1为存在,2为不存在
     */
    private int clickReadmore(String name) {
        if (devices.isElementExsitAndroid(By.id(VIP_rl_each_dialog))) {
            if (!CXBConfig.BOOK_CHAPTER_END.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/chapter_name"))) &&
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
