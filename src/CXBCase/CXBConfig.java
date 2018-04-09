package CXBCase;

public class CXBConfig {
    /**
     * 用于搜索和进入阅读时选择的书籍
     */
    public static final String BOOK_NAME = "闪婚老公太凶猛";
    //请配置目录页最后一页中的章节
    public static final String BOOK_CHAPTER_END = "老公太凶猛1418";//18
    //点击上一章次数,与BOOKCHAPTERENDLAST关联,因上一章和下一章按钮存在重页问题，顾将参数配置余量
    public static  int LAST_CHAPTER_NUM = 4+3;
    //BOOKCHAPTEREND的上一章，与LASTCHAPTERNUM关联
    public static final String BOOK_CHAPTER_END_LAST = "老公太凶猛1414";
    //BOOKCHAPTEREND的下一章一章
    public static final String BOOK_CHAPTER_END_NEXT = "老公太凶猛1419";
    //目录限制滑动总数
    public static final int BOOK_CATALOGUE_RESTRICT_SLIDE_SUM = 2000;
    //目录滑动限制次数开始检查BOOK_CHAPTER_END
    public static final int BOOK_CATALOGUE_SLIDE_COUNT = 200;
    //目录滑动限制次数达到BOOK_CATALOGUE_SLIDE_COUNT后每个多少次检查一次
    public static final int BOOK_CATALOGUE_SLIDE_COUNT_index = 2;
    //阅读页VIP页中的提示文字
    public static final String BOOK_VIP_CHAPTER_NEXT_CHAPTER_1 = "com.mianfeia.book:id/btn_nextChapter";
    public static final String BOOK_VIP_CHAPTER_NEXT_CHAPTER_2 = "com.mianfeia.book:id/each_btn_nextChapter";

}
