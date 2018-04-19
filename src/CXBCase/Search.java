package CXBCase;
/**
 * 搜索
 * 1.删除书架配置文件的书籍
 * 2.进入到搜索页，检查搜索页
 * 3.搜索书籍，检查搜索结果页
 * 4.进入在线阅读页，检查阅读页中的目录第一章
 * 5.检查阅读中的喜欢就加入书架
 * 6.搜索页加入书架，检查结果
 */

import AppTest.AppXmlUtil;
import AppTest.Logs;
import org.openqa.selenium.By;

import java.util.HashMap;
import java.util.Map;

public class Search extends StartCase {
    //加入书架按钮
    public static final By search_result_to_add_btn = By.id("com.mianfeia.book:id/search_result_to_add_btn");
    //搜索文本框
    public static final By EditText = By.className("android.widget.EditText");
    //在线阅读按钮
    public static final By search_result_to_read_btn =  By.id("com.mianfeia.book:id/search_result_to_read_btn");

    //书架是否存在书籍
    private Map<String, Boolean> bookRackExistsBookName = new HashMap<>();
    //是否点击过阅读页中的加入书架取消按钮
    public boolean cancel = false;

    public Search(String caseName) {
        super(caseName);
    }

    public boolean caseMap() {
        //删除书架书籍，用于判断加入书架的书籍是否加入到书籍
        if (!deleteBook(CXBConfig.BOOK_NAME)) return false;
        //进入到搜索页
        if (!search()) return false;
        //搜索书籍
        if (!searchBook(CXBConfig.BOOK_NAME)) return false;
        //搜索结果页和检查在线阅读
        if (!searchResult(CXBConfig.BOOK_NAME, CXBConfig.BOOK_AUTHOR)) return false;
        return true;
    }

    /*
     *搜索书籍
     */
    public boolean searchBook(String searchBookName) {
        System.out.println("执行搜索书籍");
        //检查搜索文本框中应存在搜索候选词
        String str = devices.getText(Search.EditText);
        if (str == null || str.length() < 1) {
            print.print("检查搜索文本框中应存在搜索候选词");
            return false;
        }
        //输入搜索词
        devices.inputCharacter(Search.EditText, searchBookName);
        //com.mianfeia.book:id/title_right_view搜索按钮
        devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view"));
        devices.sleep(1000);
        System.out.println("搜索书籍成功");
        return true;
    }
    public void setBookRackExistsBookName(String bookName,boolean b){
        this.bookRackExistsBookName.put(bookName,b);
    }
    /**
     * 检查搜索页结果页
     *
     * @param searchBookName
     * @param author
     * @return
     */
    public boolean checkSearchResult(String searchBookName, String author) {
        System.out.println("执行检查搜索页结果页:" + searchBookName);
        if (!searchBookName.equals(devices.getText(Search.EditText))
                || !searchBookName.equals(devices.getText(By.id("com.mianfeia.book:id/search_result_title_view")))
                ) {
            print.print("检查搜索结果页中的:" + searchBookName);
            return false;
        }
        if (bookRackExistsBookName.get(searchBookName) == false) {
            //com.mianfeia.book:id/search_result_to_add_btn加入书架按钮
            if (!devices.isElementExsitAndroid(Search.search_result_to_add_btn) ||
                    !"加入书架".equals(devices.getText(search_result_to_add_btn))) {
                print.print("检查在线阅读和加入书架按钮");
                return false;
            }
            if (!"在线阅读".equals(devices.getText(Search.search_result_to_read_btn))) {
                print.print("检查在线阅读按钮");
                return false;
            }
        } else {
            if (devices.isElementExsitAndroid(Search.search_result_to_add_btn) ||
                    "加入书架".equals(devices.getText(Search.search_result_to_add_btn))) {
                print.print("检查在线阅读不存在加入书架");
                return false;
            }
            if (!"在线阅读".equals(devices.getText(Search.search_result_to_read_btn))) {
                print.print("检查在线阅读按钮");
                return false;
            }
        }
        if (!author.equals(devices.getText(By.id("com.mianfeia.book:id/search_result_author_view")))) {
            print.print("检查搜素结果中的作者");
            return false;
        }
        System.out.println("检查搜索页结果页:" + searchBookName + "：成功");
        return true;
    }

    /**
     * 检查阅读页中加入书架按钮
     *
     * @return
     */
    public boolean readAdd_a_bookcase(String searchBookName, String author) {
        System.out.println("执行检查阅读页中加入书架按钮");
        if(new Read2(this.caseName).readAdd_a_bookcase("")!=1)return false;
        if (cancel == false) {
            //点击取消按钮
            if(new Read2(this.caseName).readAdd_a_bookcase("取消")!=1)return false;
            devices.sleep(1000);
            if (!checkSearchResult(searchBookName, author)) return false;
            devices.clickfindElement(By.className("android.widget.ImageButton"));
            devices.sleep(1000);
            if (!checkHistory(searchBookName)) return false;
            devices.clickfindElement(By.className("android.widget.ImageButton"));
            devices.sleep(1000);
            if (bookcase_is_Book(searchBookName)) {
                print.print("检查取消加入书架后，书架不存在书籍");
                return false;
            }
            //点击精品
            devices.clickfindElement(ElementAttributes.BOUTIQUE);
            System.out.println("点击精品");
            //进入搜索页
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.06));
            cancel = true;
            if (!checkSearch()) return false;
            if (!checkHistory(searchBookName)) return false;
            if (!searchBook(searchBookName)) return false;
            if (!checkSearchResult(searchBookName, author)) return false;
        } else {

            System.out.println("点击确定按钮");
            if(new Read2(this.caseName).readAdd_a_bookcase("确定")!=1)return false;
            if (!new TheWorkDetails(this.caseName).checkTheWorkDetails(searchBookName, author, false)) return false;
            System.out.println("点击作者详情页中的回到精品按钮");
            int[] xy = devices.getXY(By.id("com.mianfeia.book:id/title_right_view"));
            devices.clickScreen(xy[0], xy[1]);
            devices.sleep(3000);
            devices.backspace();
            if (!bookcase_is_Book(searchBookName)) {
                print.print("检查取消加入书架后，书架不存在书籍");
                return false;
            }
            if (!deleteBook(searchBookName)) return false;
            //点击书库
            devices.clickfindElement(ElementAttributes.STACK_ROOM);
            devices.sleep(2000);
            //进入搜索页
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.06));
            if (!checkSearch()) return false;
            if (!checkHistory(searchBookName)) return false;
            if (!searchBook(searchBookName)) return false;
            if (!checkSearchResult(searchBookName, author)) return false;
        }
        System.out.println("检查阅读页中加入书架按钮成功");
        return true;
    }


    /**
     * 搜索结果页
     */
    public boolean searchResult(String searchBookName, String author) {
        System.out.println("执行搜索结果页");
        if (!checkSearchResult(searchBookName, author)) return false;
        if (bookRackExistsBookName.get(searchBookName) == false) {
            //com.mianfeia.book:id/search_result_to_add_btn加入书架按钮
            if (!devices.isElementExsitAndroid(Search.search_result_to_add_btn) ||
                    !"加入书架".equals(devices.getText(Search.search_result_to_add_btn))) {
                print.print("检查在线阅读和加入书架按钮");
                return false;
            }
            if (!"在线阅读".equals(devices.getText(Search.search_result_to_read_btn))) {
                print.print("检查在线阅读按钮");
                return false;
            }
            //点击在线阅读按钮
            devices.sleep(2000);
            devices.clickfindElement(Search.search_result_to_read_btn);
            if (!new Read(this.caseName).caseMap()) return false;
            devices.backspace();
            devices.sleep(300);
            devices.backspace();
            if (!readAdd_a_bookcase(searchBookName, author)) return false;
            System.out.println(" //点击作者简介");
            devices.clickfindElement(By.id("com.mianfeia.book:id/search_result_title_view"));
            devices.sleep(2000);
            System.out.println(" //点击在线阅读");
            devices.clickfindElement(TheWorkDetails.ONLINE_READING);
            devices.sleep(2000);
            if (!new Read(this.caseName).caseMap()) return false;
            System.out.println("点击屏幕右侧，关闭目录页");
            devices.clickScreen((int) (devices.getWidth() * 0.9),
                    (int) (devices.getHeight() * 0.5));
            devices.sleep(300);
            devices.backspace();
            if (!readAdd_a_bookcase(searchBookName, author)) return false;
            if (!checkSearchResult(searchBookName, author)) return false;
            //点击加入书架按钮
            devices.clickfindElement(Search.search_result_to_add_btn);
            bookRackExistsBookName.put(searchBookName, true);
            devices.sleep(2000);
            if (devices.isElementExsitAndroid(Search.search_result_to_add_btn)) {
                print.print("点击加入书架后按钮应隐藏");
                return false;
            }
            if (!"在线阅读".equals(devices.getText(Search.search_result_to_read_btn))) {
                print.print("检查在线阅读按钮");
                return false;
            }
            //检查点击书籍后进入到作者详情页
            devices.clickfindElement(By.id("com.mianfeia.book:id/search_result_title_view"));
            if (!new TheWorkDetails(this.caseName).checkTheWorkDetails(searchBookName, author, true)) return false;
            //点击在线阅读按钮
            devices.sleep(2000);
            devices.clickfindElement(Search.search_result_to_read_btn);
            if (!new Read(this.caseName).caseMap()) return false;
            devices.backspace();
            devices.sleep(300);
            devices.backspace();
            if (!checkSearchResult(searchBookName, author)) return false;
            //点击返回按钮
            devices.clickfindElement(By.className("android.widget.ImageButton"));
            devices.sleep(2000);
            if (!checkSearch()) return false;
            if (!checkHistory(searchBookName)) return false;
            //点击返回按钮
            devices.clickfindElement(By.className("android.widget.ImageButton"));
            devices.sleep(2000);
            devices.backspace();
            if (!bookcase_is_Book(searchBookName)) {
                print.print("检查搜索加入书架后，书架中的书籍:" + searchBookName);
                return false;
            }
            //点击赚钱按钮
            devices.clickfindElement(ElementAttributes.MAKE_MONEY);
            devices.sleep(2000);
            //进入搜索页
            devices.clickScreen((int) (devices.getWidth() * 0.5),
                    (int) (devices.getHeight() * 0.06));
            if (!checkSearch()) return false;
            if (!checkHistory(searchBookName)) return false;
            if (!searchBook(searchBookName)) return false;
            if (!searchResult(searchBookName, author)) return false;
        } else {
            if (devices.isElementExsitAndroid(Search.search_result_to_add_btn)) {
                print.print("已加入书架的书籍检查搜索结果页不存在加入书架按钮:" + searchBookName);
                return false;
            }
        }
        System.out.println("搜索结果页成功");
        return true;
    }

    /**
     * 检查书架是否存在查询的书籍
     *
     * @param bookName
     * @return
     */
    public boolean bookcase_is_Book(String bookName) {
        System.out.println("执行检查书架是否存在查询的书籍:" + bookName);
        boolean bookIs = devices.isElementExsitAndroid("new UiSelector().text(\"" + bookName + "\")");
        bookRackExistsBookName.put(bookName, bookIs);
        System.out.println("查询的书籍" + bookName + "=" + bookIs);
        return bookIs;
    }

    /**
     * 删除书架中的书籍
     *
     * @param bookName
     * @return
     */
    public boolean deleteBook(String bookName) {
        System.out.println("执行删除书籍");
        //判断配置中的书籍是否显示在书架中
        if (bookcase_is_Book(bookName)) {
            int[] xy = devices.getXY("new UiSelector().text(\"" + bookName + "\")");
            devices.customSlip(xy[0], xy[1], xy[0], xy[1], 1000);
            if (!"删除(1)".equals(devices.getText(By.id("com.mianfeia.book:id/tab_delete_tv"))) ||
                    !"移动(1)".equals(devices.getText(By.id("com.mianfeia.book:id/tab_move_tv"))) ||
                    //com.mianfeia.book:id/title_right_view右侧全选按钮id
                    !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/title_right_view")) ||
                    ////android.widget.ImageButton[contains(@index,0)]完成按钮
                    !devices.isElementExsitAndroid(Sidebar.BOOK_SHELF_SIDEBAR)
                    ) {
                print.print("检查书架书籍删除时的按钮");
                return false;
            }
            //点击删除按钮
            devices.clickfindElement(By.id("com.mianfeia.book:id/tab_delete_tv"));
            devices.sleep(1000);
            //检查删除书籍提示框
            if (!"删除所选书籍".equals(devices.getText(By.id("com.mianfeia.book:id/ygz_common_delete_title"))) ||
                    !"删除源文件".equals(devices.getText(By.id("com.mianfeia.book:id/ygz_common_bottom_check"))) ||
                    !"取消".equals(devices.getText(By.id("com.mianfeia.book:id/ygz_common_bottom_cancel"))) ||
                    !"确定".equals(devices.getText(By.id("com.mianfeia.book:id/ygz_common_bottom_sure")))
                    ) {
                print.print("检查删除书籍提示框");
                return false;
            }
            //点击删除源文件
            devices.clickfindElement(By.id("com.mianfeia.book:id/ygz_common_bottom_check"));
            devices.snapshot("请检查点击删除源文件后是否被选中");
            //点击确定按钮
            devices.clickfindElement(By.id("com.mianfeia.book:id/ygz_common_bottom_sure"));
            devices.sleep(2000);
            xy = devices.getXY("new UiSelector().text(\"" + bookName + "\")");
            if ((xy[0] != 0 || xy[1] != 0)) {
                print.print("书籍" + bookName + "删除");
                return false;
            }
            if (bookcase_is_Book(bookName)) {
                print.print("书籍" + bookName + "删除");
                return false;
            }
            //com.mianfeia.book:id/ygz_common_bottom_sure删除提示框确定按钮
            if (devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/ygz_common_bottom_sure")) ||
                    //com.mianfeia.book:id/tab_move_tv删除框中的移动按钮
                    devices.isElementExsitAndroid(By.className("com.mianfeia.book:id/tab_move_tv"))) {
                print.print("删除书籍后，删除编辑框没有关闭");
                return false;
            }
        }
        System.out.println("删除书籍成功");
        return true;
    }

    /**
     * 检查搜索历史
     *
     * @return
     */
    private boolean checkHistory(String searchBookName) {
        System.out.println("执行检查搜索历史");
        String historyStr = AppXmlUtil.getXMLElement(
                "android.support.v7.widget.RecyclerView//android.widget.LinearLayout(index=1;)" +
                        "//android.widget.TextView(index=0;)",
                devices.getPageXml(), "text");
        if (searchBookName.length() > 4) {

            if (historyStr.length() < 4 || !searchBookName.substring(0, 4).equals(historyStr.substring(0, 4))) {
                print.print("检查搜索历史:" + searchBookName);
                return false;
            }
        } else {
            if (!searchBookName.equals(historyStr)) {
                print.print("检查搜索历史:" + searchBookName);
                return false;
            }
        }
        System.out.println("检查搜索历史成功");
        return true;
    }

    /**
     * 检查搜索页
     *
     * @return
     */
    private boolean checkSearch() {
        System.out.println("执行检查搜索页");
        if (!devices.isElementExsitAndroid("new UiSelector().text(\"搜索关键词\")") ||
                //com.mianfeia.book:id/item_search_keyword_view搜索关键词中的词
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/item_search_keyword_view")) ||
                !devices.isElementExsitAndroid("new UiSelector().text(\"大家都在搜\")") ||
                //com.mianfeia.book:id/search_result_title_view大家都在搜中的title
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_title_view")) ||
                //com.mianfeia.book:id/search_result_summary_view大家都在搜中的描述
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_summary_view")) ||
                //com.mianfeia.book:id/search_result_author_view大家都在搜中的作者
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_author_view")) ||
                //com.mianfeia.book:id/search_result_words_view大家都在搜中的字数
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_words_view")) ||
                //com.mianfeia.book:id/search_result_type_view大家都在搜索中的类型
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_type_view"))

                ) {
            print.print("检查搜索页面显示是否正确");
            return false;
        }
        System.out.println("检查搜索页成功");
        return true;
    }

    public boolean search() {
        System.out.println("执行搜索");
        //点击书架中的搜索按钮
        devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view_2"));
        devices.sleep(3000);
        //检查搜索页面
        if (!checkSearch()) return false;
        String bookName = devices.getText(By.id("com.mianfeia.book:id/search_result_title_view"));
        String bookAuthor = devices.getText(By.id("com.mianfeia.book:id/search_result_author_view"));
//        int[] xy = devices.getXY("new UiSelector().text(\"" + bookName + "\")");
//        devices.clickScreen(xy[0], xy[1]);
        devices.clickfindElement(By.id("com.mianfeia.book:id/search_result_summary_view"));
        devices.sleep(2000);
        if (!new TheWorkDetails(this.caseName).checkTheWorkDetails(bookName, bookAuthor, true)) return false;
        System.out.println("搜索成功");
        return true;
    }
}
