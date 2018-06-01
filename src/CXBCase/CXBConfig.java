package CXBCase;

/**
 * 当前支持的免费电子书版本信息:4.0.2
 */

import java.util.Random;

public class CXBConfig {
    //发现的bug，找回密码服务器随机生成的密码不能作为新密码使用，提示密码错误
    //兼容手机屏幕坐标,阅读页设置
    public static final String modelSM = "model:SM_C5000";
    /**
     * 登录用户名和密码
     */
    public static final String USER_NAME = "17710893436";
    public static final String PASSWORD = "613089";
    public static final String USER = "张连宇";
    /**
     * 用于搜索和进入阅读时选择的书籍
     */
    public static final String BOOK_NAME = "闪婚老公太凶猛";
    //书籍第一章节
    public static final String BOOK_CHAPTER_FIRST = "第1章 再傻也懂了";
    //书籍简介
    public static final String BOOK_SYNOPSIS  = "结婚前一天，她亲眼目睹男友和他的准大嫂在办公室上演限制级戏码，心碎之时，一个沉稳的男人出现在她身边。“跟我结婚，这样，他们两个无耻的男女就要每天叫你大嫂？…";
    public static final String BOOK_SYNOPSISOPEN = "结婚前一天，她亲眼目睹男友和他的准大嫂在办公室上演限制级戏码，心碎之时，一个沉稳的男人出现在她身边。“跟我结婚，这样，他们两个无耻的男女就要每天叫你大嫂？怎样？”就这样，领了证。可是，谁能想到，原本坐在轮椅上的老公不仅不无能，还动不动就把她吃干抹净……";
    //书籍作者
    public static final String BOOK_AUTHOR = "温煦依依";
    //请配置目录页最后一页中的章节,不要配置最后一个章节，尽量配置最后最后一页中的中间部分章节
    public static final String BOOK_CHAPTER_END = "老公太凶猛1418";
    //点击上一章次数,与BOOK_CHAPTER_END_LAST关联,因上一章和下一章按钮存在重页问题，顾将参数配置余量
    public static int LAST_CHAPTER_NUM = 4 + 3;
    //BOOK_CHAPTER_END的上LAST_CHAPTER_NUM 关联
    public static final String BOOK_CHAPTER_END_LAST = "老公太凶猛1414";
    //BOOK_CHAPTER_END的下一章一章
    public static final String BOOK_CHAPTER_END_NEXT = "老公太凶猛1419";
    //目录限制滑动总数
    public static final int BOOK_CATALOGUE_RESTRICT_SLIDE_SUM = 2000;
    //目录滑动限制次数开始检查BOOK_CHAPTER_END
    public static final int BOOK_CATALOGUE_SLIDE_COUNT = 200;
    //目录滑动限制次数达到BOOK_CATALOGUE_SLIDE_COUNT后每个多少次检查一次
    public static final int BOOK_CATALOGUE_SLIDE_COUNT_index = 2;
    //阅读页VIP页中的提示文字
    public static final String BOOK_VIP_CHAPTER_NEXT_CHAPTER_1 = AppiumMethod.Config.APP_PACKAGE+":id/btn_nextChapter";
    public static final String BOOK_VIP_CHAPTER_NEXT_CHAPTER_2 = AppiumMethod.Config.APP_PACKAGE+":id/each_btn_nextChapter";
    /**
     * 作者详情页
     */
    //BOOK_NAME的所需积分
    public static final String BOOK_NAME_INTEGRAL = "所需积分：66";
    //是否检查分享
    public static final boolean CHECK_SHARE = false;


    /**
     * 元素滑动坐标
     *
     * @return {startX,startY,endX，endY，time}
     */

    public static int[] slideXY(int width, int height, String element) {
        if (element == null) return null;
        switch (element) {
            //目录右上到下滑动坐标
            case (Read.catalog):
                return new int[]{(int) (width * 0.7), (int) (height * 0.9),
                        (int) (width * 0.7), (int) (height * 0.3), (new Random().nextInt(8) + 3) * 100};
            //微信登录密码文本框坐标
            case (Loging.passwordName):
                return new int[]{(int) (width * 0.6), (int) (height * 0.39)};
            default:
                return null;
        }

    }

    /**
     * 元素点击坐标
     *
     * @return {x,y}
     */
    public static int[] chickXY(int width, int height, String element) {
        if (element == null) return null;
        switch (element) {
            //微信登录密码文本框坐标
            case (Loging.passwordName):
                return new int[]{(int) (width * 0.6), (int) (height * 0.39)};
            //个人资料中的切换账号
            case (Loging.switch_account):
                return new int[]{(int) (width * 0.5), (int) (height * 0.85)};
            //微信登录
            case (Loging.title_Text):
                return new int[]{(int) (width * 0.7), (int) (height * 0.7)};
            //H5登录
            case (Loging.H5LogingUserName):
                return new int[]{(int) (width * 0.3), (int) (height * 0.215)};
            case (Loging.H5LogingPassword):
                return new int[]{(int) (width * 0.3), (int) (height * 0.3)};
            case (Loging.H5Loging):
                return new int[]{(int) (width * 0.5), (int) (height * 0.46)};
            //侧边栏按钮
            case (Sidebar.BOOK_SHELF_SIDEBAR):
                return new int[]{(int) (width * 0.07), (int) (height * 0.06)};

            default:
                return null;
        }

    }
}
