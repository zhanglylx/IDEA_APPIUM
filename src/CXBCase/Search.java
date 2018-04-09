package CXBCase;
/**
 * 搜索
 */

import AppTest.Logs;
import org.openqa.selenium.By;

public class Search extends Sidebar {

    public Search(String caseName) {
        super(caseName);
    }

    public void startCase() {
        Logs.recordLogs(caseName, runSearch());
    }

    private boolean runSearch() {
        //删除书架书籍，用于判断加入书架的书籍是否加入到书籍
        if (!deleteBook()) return false;
        //进入到搜索页
        if (!search()) return false;


        return true;
    }

    public boolean deleteBook() {
        //判断配置中的书籍是否显示在书架中
        if (devices.isElementExsitAndroid("new UiSelector().text(\"" + CXBConfig.BOOK_NAME + "\")")) {
            int[] xy = devices.getXY("new UiSelector().text(\"" + CXBConfig.BOOK_NAME + "\")");
            devices.customSlip(xy[0], xy[1], xy[0], xy[1], 1000);
            if (!"删除(1)".equals(devices.getText(By.id("com.mianfeia.book:id/tab_delete_tv"))) ||
                    !"移动(1)".equals(devices.getText(By.id("com.mianfeia.book:id/tab_move_tv"))) ||
                    //com.mianfeia.book:id/title_right_view右侧全选按钮id
                    !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/title_right_view")) ||
                    ////android.widget.ImageButton[contains(@index,0)]完成按钮
                    !devices.isElementExsitAndroid(By.xpath("//android.widget.ImageButton[contains(@index,0)]"))
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
            devices.sleep(1000);
            xy = devices.getXY("new UiSelector().text(\"" + CXBConfig.BOOK_NAME + "\")");
            if (xy[0] != 0 || xy[1] != 0) {
                print.print("书籍" + CXBConfig.BOOK_NAME + "删除");
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
        return true;
    }
    private boolean search(){
        //点击书架中的搜索按钮
        devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view_2"));
        devices.sleep(3000);
        //检查搜索页面
        if(!devices.isElementExsitAndroid("new UiSelector().text(\"搜索关键词\")")||
                !"换一换".equals(devices.getText(By.id("com.mianfeia.book:id/item_search_title_action_view")))||
                //com.mianfeia.book:id/item_search_keyword_view搜索关键词中的词
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/item_search_keyword_view")) ||
                !devices.isElementExsitAndroid("new UiSelector().text(\"大家都在搜\")")||
                //com.mianfeia.book:id/search_result_title_view大家都在搜中的title
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_title_view"))||
                //com.mianfeia.book:id/search_result_summary_view大家都在搜中的描述
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_summary_view"))||
                //com.mianfeia.book:id/search_result_author_view大家都在搜中的作者
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_author_view")) ||
                //com.mianfeia.book:id/search_result_words_view大家都在搜中的字数
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_words_view")) ||
                //com.mianfeia.book:id/search_result_type_view大家都在搜索中的类型
                !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/search_result_type_view"))

                ){
                print.print("检查搜索页面显示是否正确");
                return false;
        }
        String bookName = devices.getText(By.id("com.mianfeia.book:id/search_result_title_view"));
        String bookAuthor = devices.getText(By.id("com.mianfeia.book:id/search_result_author_view"));
        int [] xy = devices.getXY("new UiSelector().text(\""+bookName+"\")");
        devices.clickScreen(xy[0],xy[1]);
        devices.sleep(2000);
        if(!new TheWorkDetails("搜索").checkTheWorkDetails(bookName,bookAuthor)) return false;






        return true;
    }
}
