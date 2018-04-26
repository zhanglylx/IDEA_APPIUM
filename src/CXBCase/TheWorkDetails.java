package CXBCase;

import AppTest.AppXmlUtil;
import AppTest.Devices;
import AppTest.Logs;
import org.openqa.selenium.By;

import java.util.Random;
import java.util.concurrent.RecursiveTask;

/**
 * 作品详情页
 * 1.检查详情页是否展示正确
 * 2.检查在线阅读、下载、目录和分享
 */
public class TheWorkDetails extends StartCase {
    //在线阅读
    public static final By ONLINE_READING = By.id("com.mianfeia.book:id/book_detail_to_read_view");
    //判断是否为分享调用
    public static  boolean Share_the_call = false;
    public TheWorkDetails(String caseName) {
        super(caseName);
    }

    public boolean caseMap() {
        return checkTheWorkDetails(CXBConfig.BOOK_NAME, CXBConfig.BOOK_AUTHOR,2);
    }
    public boolean checkTheWorkDetails(String bookName, String author, boolean back_trueOrHome_false) {
        if (back_trueOrHome_false) return checkTheWorkDetails(bookName, author, 0);
        return checkTheWorkDetails(bookName, author, 1);
    }

    /**
     * 检查作品详情页
     *
     * @param bookName
     * @param author
     * @return
     */
    public boolean checkDetails(String bookName, String author) {
        if (bookName == null || author == null) {
            print.print("booName:" + bookName + " 或者author:" + author + " 为空");
            return false;
        }
        if (!"作品详情".equals(devices.getText(
                By.xpath("//android.widget.TextView[contains(@index,1)]")))
                || !bookName.equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_name_view")))
                || !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/item_board_title_view"))
                || !author.equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_author_view")))
                || !"在线阅读".equals(devices.getText(TheWorkDetails.ONLINE_READING))
                || !"下载".equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_to_download_view")))
                || !"目录".equals(devices.getText(By.id("com.mianfeia.book:id/book_detail_to_volume_view")))
                //返回按钮
                || !devices.isElementExsitAndroid(By.className("android.widget.ImageButton"))
                ) {
            print.print("点击书籍:" + bookName + "跳转到书籍详情页不正确");
            return false;
        }
        if(!Share_the_call)if(!checkShare(bookName,author))return false;
        Share_the_call = false;
        return true;
    }

    /**
     * 检查开始阅读
     *
     * @return
     */
    private boolean checkRead() {
        System.out.println("执行检查开始阅读");
        System.out.println("    //点击开始阅读按钮");
        devices.clickfindElement(By.id("com.mianfeia.book:id/dlg_download_book_btn"));
        if (!new Read(this.caseName).caseMap()) {
            print.print("检查下载后的开始阅读失败");
            return false;
        }
        devices.backspace();
        return true;
    }

    private boolean checkTheWorkDetails(String bookName, String author, int index) {
        if (index == 2) {
            /**
             * 删除书架存在的书籍
             * 通过搜索
             * 进入到作者详情页
             * 点击目录，检查目录
             * 点击下载，如果存在积分兑换框，检查积分兑换框
             * 积分充足检查下载框
             * 检查书架存在书籍
             */
            System.out.println("执行检查作者详情页中的下载");
            RunCase.initialize(devices);
            Search search = new Search(this.caseName);
            if (!search.deleteBook(bookName)) return false;
            if (!search.search()) return false;
            if (!search.searchBook(bookName)) return false;
            if (!search.checkSearchResult(bookName, author)) return false;
            System.out.println("    //点击搜索结果页简介");
            devices.clickfindElement(By.id("com.mianfeia.book:id/search_result_summary_view"));
            devices.sleep(3000);
            if (!checkDetails(bookName, author)) return false;
            System.out.println("    //点击目录按钮");
            devices.clickfindElement(By.id("com.mianfeia.book:id/book_detail_to_volume_view"));
            devices.sleep(2000);
            if (!devices.isElementExsitAndroid(By.className("android.widget.ImageButton")) ||
                    !bookName.equals(devices.getText(By.className("android.widget.TextView"))) ||
                    !"目录".equals(devices.getText(By.id("com.mianfeia.book:id/tabIndicatorView1"))) ||
                    !"书签".equals(devices.getText(By.id("com.mianfeia.book:id/tabIndicatorView2"))) ||
                    !CXBConfig.BOOK_CHAPTER_FIRST.equals(devices.getText(By.id("com.mianfeia.book:id/chapterlist_chaptertitle")))
                    ) {
                print.print("检查作者详情页中的目录");
                return false;
            }
            System.out.println("    //点击目录中的返回按钮");
            devices.clickfindElement(By.className("android.widget.ImageButton"));
            devices.sleep(2000);
            if (!checkDetails(bookName, author)) return false;
            System.out.println("    //点击下载");
            devices.clickfindElement(By.id("com.mianfeia.book:id/book_detail_to_download_view"));
            devices.sleep(2000);
            int downloadTheBox = downloadTheBox(bookName);
            //判断下载框是积分不足还是积分充足
            if (downloadTheBox == 0) {
                return false;
            } else if (downloadTheBox == 1) {
                print.print("当前账号积分不充足，请手动检测下载或切换账号后从新运行脚本");
                if (!checkDetails(bookName, author)) return false;
                return true;
            } else {
                System.out.println("    //点击确定兑换按钮");
                devices.clickfindElement(By.id("com.mianfeia.book:id/dlg_buy_book_submit_view"));
                devices.sleep(2000);
                if (!"已加入书架，正在下载中…".equals(devices.getText(By.id("com.mianfeia.book:id/dlg_download_book_title_view"))) ||
                        //com.mianfeia.book:id/dlg_download_book_prg_view进度条
                        !devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/dlg_download_book_prg_view")) ||
                        !"开始阅读".equals(devices.getText(By.id("com.mianfeia.book:id/dlg_download_book_btn"))) ||
                        !devices.getText(By.id("com.mianfeia.book:id/dlg_download_book_prg_tv")).endsWith("%")
                        ) {
                    print.print("检查正在下载框失败");
                    return false;
                }
                int num = 0;
                devices.sleep(500);
                long timeStart = (System.currentTimeMillis()) + (1000 * 60 * 10);
                for (int i = 0; ; i++) {
                    if (System.currentTimeMillis() > timeStart) {
                        print.print("下载超时10分钟");
                        return false;
                    }
                    String dlg_download_book_prg_tv = devices.getText(By.id("com.mianfeia.book:id/dlg_download_book_prg_tv"));
                    if (dlg_download_book_prg_tv == null) {
                        print.print("获取下载进度:" + dlg_download_book_prg_tv);
                        return false;
                    }
                    num = Integer.parseInt(dlg_download_book_prg_tv.replace("%", ""));
                    if (num == 100) {
                        if (!"下载成功".equals(devices.getText(By.id("com.mianfeia.book:id/dlg_download_book_title_view")))) {
                            print.print("下载为100%时，下载状态检查");
                            return false;
                        }
                        if (!checkRead()) return false;
                        break;
                    }
                }
                RunCase.initialize(devices);
                if (!new Search(this.caseName).bookcase_is_Book(bookName)) {
                    print.print("下载完成后书架中没有加入书籍：" + bookName);
                    return false;
                }
                return true;
            }

        }
        /**
         * 检查作者详情页
         *
         */
        System.out.println("执行检查作者详情页");
        if (!checkDetails(bookName, author)) return false;

        //点击返回按钮
        if (index == 0) devices.clickfindElement(By.className("android.widget.ImageButton"));
        //返回到书架
        if (index == 1) RunCase.initialize(devices);

        System.out.println("检查作者详情页成功");
        return true;
    }
    /**
     * 检查分享框
     */
    private boolean checkShare(String bookName,String author){
        //检查分享给好友
        if(!devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/share_activity_title"))){
            devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view"));
        }
        if(!"分享给好友".equals(devices.getText(By.id("com.mianfeia.book:id/share_activity_title"))) ||
                !"微信".equals(devices.getText(By.id("com.mianfeia.book:id/umeng_share_wechat")))||
                !"朋友圈".equals(devices.getText(By.id("com.mianfeia.book:id/umeng_share_wechat_wxcircle")))
                ){
            print.print("检查分享给好友框架");
            return false;
        }
        //点击微信
        devices.clickfindElement(By.id("com.mianfeia.book:id/umeng_share_wechat"));
        devices.sleep(5000);
        if(!"微信号/QQ/邮箱登录".equals(devices.getText(By.id("com.tencent.mm:id/k5")))&&
                !"创建新聊天".equals(devices.getText(By.id("com.tencent.mm:id/amo")))){
            print.print("检查分享到微信");
            return false;
        }
        devices.backspace();
        Share_the_call = true;
        if (!checkDetails(bookName, author)) return false;
        //点击分享框
        devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view"));
        //点击朋友圈
        devices.clickfindElement(By.id("com.mianfeia.book:id/umeng_share_wechat_wxcircle"));
        devices.sleep(5000);
        if("微信号/QQ/邮箱登录".equals(devices.getText(By.id("com.tencent.mm:id/k5")))){
            devices.backspace();
        }else{
            if(!"这一刻的想法...".equals(devices.getText(By.id("com.tencent.mm:id/dez")))||
                    !("《"+bookName+"》").equals(devices.getText(By.id("com.tencent.mm:id/ddi")))
                    ){
                print.print("检查分享到朋友圈");
                return false;
            }
            //点击发送按钮
            devices.clickfindElement(By.id("com.tencent.mm:id/hd"));
            print.print("发送了朋友圈分享，书籍名称:"+bookName+" 请到朋友圈中手动检测");
            devices.sleep(2000);
        }
        Share_the_call = true;
        if (!checkDetails(bookName, author)) return false;
        return true;
    }


    /**
     * 检查下载框
     *
     * @return 0验证失败，1积分不足验证成功，2积分充足验证成功
     */
    private int downloadTheBox(String booKName) {
        //所需积分
        String dlg_buy_book_price_view = devices.getText(By.id("com.mianfeia.book:id/dlg_buy_book_price_view"));
        //已有积分
        String dlg_buy_book_balance_view = devices.getText(By.id("com.mianfeia.book:id/dlg_buy_book_balance_view"));
        if (Integer.parseInt(dlg_buy_book_price_view.substring(dlg_buy_book_price_view.indexOf("：") + 1, dlg_buy_book_price_view.length()))
                >
                Integer.parseInt(dlg_buy_book_balance_view.substring(dlg_buy_book_price_view.indexOf("：") + 1, dlg_buy_book_balance_view.length()))) {
            if (!AppXmlUtil.getXMLElement("android.widget.TextView(index=3;)",
                    devices.getPageXml(), "text").equals("当前积分余额不足")) {

                print.print("检查当前积分余额不足;" + "所需积分:" + dlg_buy_book_price_view + " 已有积分:" + dlg_buy_book_balance_view);
                return 0;
            }
            if (!"去赚积分".equals(devices.getText(By.id("com.mianfeia.book:id/dlg_buy_book_submit_view")))) {
                print.print("当前积分余额不足显示去赚积分按钮");
                return 0;
            }
            //点击去赚积分
            devices.clickfindElement(By.id("com.mianfeia.book:id/dlg_buy_book_submit_view"));
            devices.sleep(2000);
            //验证跳转到赚积分页
            if (!new Sidebar(this.caseName).chickShow(Sidebar.ToEarnPoints)) return 0;
            //返回到作者详情页
            devices.backspace();
            return 1;
        } else {
            if (dlg_buy_book_balance_view == null ||
                    !booKName.equals(devices.getText(By.id("com.mianfeia.book:id/dlg_buy_book_name_view"))) ||
                    !CXBConfig.BOOK_NAME_INTEGRAL.equals(devices.getText(By.id("com.mianfeia.book:id/dlg_buy_book_price_view"))) ||
                    !dlg_buy_book_balance_view.startsWith("已有积分") ||
                    !AppXmlUtil.getXMLElement("android.widget.TextView(index=3;)",
                            devices.getPageXml(), "text").equals("下载后无需流量，可离线阅读") ||
                    !"确认兑换".equals(devices.getText(By.id("com.mianfeia.book:id/dlg_buy_book_submit_view")))
                    ) {
                print.print("检查作者详情页中的下载框");
                return 0;
            }
            return 2;
        }
    }
}
