package CXBCase;

import AppTest.Devices;
import org.openqa.selenium.By;

/**
 * 作品详情页
 */
public class TheWorkDetails extends Sidebar{
    public TheWorkDetails(String caseName) {
        super(caseName);
    }

    public  boolean checkTheWorkDetails(String bookName, String author) {
        if (bookName == null || author == null)return false;
        if (!"作品详情".equals(devices.getText(
                By.xpath("//android.widget.TextView[contains(@index,1)]")))
                || !bookName.equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_name_view")))
                ||!devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/item_board_title_view"))
                ||!author.equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_author_view")))
                ||!"在线阅读".equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_to_read_view")))
                ||!"下载".equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_to_download_view")))
                ||!"目录".equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_to_volume_view")))
                //返回按钮
                ||!devices.isElementExsitAndroid(By.className("android.widget.ImageButton"))
                ) {
            print.print("点击书籍:"+bookName+"跳转到书籍详情页不正确");
            return false;
        }
        //点击返回按钮
        devices.clickfindElement(By.className("android.widget.ImageButton"));

        return true;
    }
}
