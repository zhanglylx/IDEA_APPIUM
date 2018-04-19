package CXBCase;

import AppTest.AppXmlUtil;
import org.openqa.selenium.By;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 检查阅读页中的日夜间切换、评论、添加书签
 */
public class Read2 extends StartCase {
    String bookName;
    String author;

    public Read2(String caseName) {
        super(caseName);
    }

    @Override
    public boolean caseMap() {
        /**
         * 进入精品页，滑动到精品页最底部，进入底部中的第一本书籍
         */
        if (!boutique()) return false;
        /**
         * 检查书签
         * 1.进入到阅读页，在目录中随机向下滑动
         * 2.随机选择一个章节，添加书签
         * 3.进入目录页中的书签，检查添加的章节
         */
        if (!bookMark()) return false;
        /**
         * 检查在作者详情页的目录中添加书签，在书库中选择一个书籍
         * 1.在作者详情页进入目录
         * 2.随机向下滑动，选择一个章节
         * 3.添加书签
         * 4.返回到作者详情页，进入目录，检查书签
         */
        if (!DirectoryPageBookmarks()) return false;
        return true;
    }

    private boolean DirectoryPageBookmarks() {
        return true;
    }


    /**
     * 检查书签
     *
     * @return boolean
     */
    private boolean bookMark() {
        //进入目录
        if (!new Read(this.caseName).style(Read.catalog)) return false;
        /**
         * 滑动目录由上而下到底部
         */
        int n = (new Random().nextInt(10) + 1) * 10;
        devices.sleep(3000);
        for (int i = 0; i < n; i++) {
            devices.customSlip(CXBConfig.slideXY(width, height, Read.catalog));
        }
        devices.sleep(3000);
        String chaptertitile = devices.getText(By.id("com.mianfeia.book:id/chapterlist_chaptertitle"));
        //点击第一个章节
        devices.clickfindElement(By.id("com.mianfeia.book:id/chapterlist_chaptertitle"));
        devices.sleep(1000);
        if (new Read(this.caseName).clickReadmore() == 0) {
            print.print("点击书籍中的:" + chaptertitile + "章节后的检查VIP页");
            return false;
        }
        //点击书签
        if (!new Read(this.caseName).style(Read.bookmark)) return false;
        String addDate = devices.getIphoneDate();
        //进入目录
        if (!new Read(this.caseName).style(Read.catalog)) return false;
        //点击书签
        devices.clickfindElement(By.id("com.mianfeia.book:id/tabIndicatorView1"));
        //检查书签
        if (!CheckBookmarks(chaptertitile, addDate)) return false;
        devices.backspace();
        devices.backspace();
        int i = readAdd_a_bookcase("确定");
        Search search = new Search(this.caseName);
        search.setBookRackExistsBookName(chaptertitile,true);
        if(i==0)return false;
        RunCase.initialize(devices);
        if(!search.bookcase_is_Book(chaptertitile)){
            print.print("检查从阅读页添加到书架的书籍:"+chaptertitile);
        }
        return true;
    }

    /**
     * 检查书签
     *
     * @param chaptertitile
     * @return
     */
    private boolean CheckBookmarks(String chaptertitile, String addDate) {
        if (chaptertitile == null) return false;
        if (addDate == null) return false;
        if (chaptertitile.length() > 6) chaptertitile = chaptertitile.substring(0, 6);
        String mark_chapter_title = devices.getText(By.id("com.mianfeia.book:id/mark_chapter_title"));
        if (mark_chapter_title == null || !mark_chapter_title.contains(chaptertitile)) {
            print.print("检查书签:" + chaptertitile);
            return false;
        }
        String marksDate = devices.getText(By.id("com.mianfeia.book:id/mark_date"));
        if (!marksDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
            print.print("获取书签日期格式检查:" + marksDate);
            return false;
        }
        marksDate = marksDate.substring(0, marksDate.indexOf(":"));
        if (!addDate.equals(marksDate)) {
            print.print("检查书签的日期与加入的日期:addDate:" + addDate + " marksDate:" + marksDate);
            return false;
        }
        String mark_content = devices.getText(By.id("com.mianfeia.book:id/mark_content"));
        if (mark_content == null || mark_content.length() < 1) {
            print.print("获取的书签描述:mark_content：" + mark_content);
            return false;
        }
        return true;
    }

    /**
     * 从精品页进入阅读页
     *
     * @return
     */
    private boolean boutique() {
        //点击精品按钮
        devices.clickfindElement(ElementAttributes.BOUTIQUE);
        devices.sleep(3000);
        long startTime = System.currentTimeMillis() + (1000 * 60 * 10);
        for (; ; ) {
            devices.swipeToUp((int) ((Math.random() * 9 + 1) * 100));
            if ("暂无更多内容".equals(devices.getText(By.id("com.mianfeia.book:id/item_load_more_view")))) {
                break;
            }
            if (System.currentTimeMillis() > startTime) {
                print.print("滑动精品页到页面最底层超过10分钟");
                return false;
            }
        }
        this.bookName = AppXmlUtil.getXMLElement("android.view.View//android.support.v7.widget.RecyclerView//android.widget.LinearLayout(index=1;)//" +
                "android.widget.TextView(resource-id=com.mianfeia.book:id/search_result_title_view;)" +
                "(index=0;)", devices.getPageXml(), "text");
        this.author = AppXmlUtil.getXMLElement("android.view.View//android.support.v7.widget.RecyclerView//android.widget.LinearLayout(index=1;)//" +
                "android.widget.TextView(resource-id=com.mianfeia.book:id/search_result_author_view;)" +
                "(index=2;)", devices.getPageXml(), "text");
        //点击书籍
        devices.clickScreen(devices.getXY(By.xpath("//android.widget.TextView[contains(@text,\"" +
                "" + bookName + "\")]")));
        devices.sleep(3000);
        if (!new TheWorkDetails(this.caseName).checkDetails(bookName, author)) return false;
        //进入在线阅读
        devices.clickfindElement(TheWorkDetails.ONLINE_READING);
        devices.sleep(3000);
        return true;
    }

    /**
     * @return 0为检查失败，1为检查成功，2为没有出现
     * @code 取消，确定，其他
     */
    public int readAdd_a_bookcase(String code) {
        if ("喜欢就加入书架吧！".equals(devices.getText(By.id("com.mianfeia.book:id/message_two_tip")))) {
            if (!"喜欢就加入书架吧！".equals(devices.getText(By.id("com.mianfeia.book:id/message_two_tip")))
                    || !"取消".equals(devices.getText(By.id("com.mianfeia.book:id/btn_left")))
                    || !"确定".equals(devices.getText(By.id("com.mianfeia.book:id/btn_right")))
                    ) {
                print.print("检查阅读页中加入书架");
                return 0;
            }
            if ("取消".equals(code)) {
                devices.clickfindElement(By.id("com.mianfeia.book:id/btn_left"));
            } else if ("确定".equals(code)) {
                devices.clickfindElement(By.id("com.mianfeia.book:id/btn_right"));
            }
            return 1;
        }
        return 2;
    }

}


