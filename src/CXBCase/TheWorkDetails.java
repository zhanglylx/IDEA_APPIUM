package CXBCase;

import AppTest.AppXmlUtil;
import org.openqa.selenium.By;

/**
 * 作品详情页
 * 1.检查详情页是否展示正确
 * 2.检查在线阅读、下载、目录和分享
 */
public class TheWorkDetails extends CaseFrame {
    //在线阅读
    public static final By ONLINE_READING = By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_to_read_view");
    //判断是否为分享调用
    public static  boolean Share_the_call = false;
    //展开按钮
    public static boolean unfold ;
    //类型
    public static boolean WorkType ;
    static{
        unfold = true;
        WorkType = true;
    }
    public TheWorkDetails(String caseName) {
        super(caseName);
    }

    public boolean caseMap() {
        return checkTheWorkDetails(CXBConfig.BOOK_NAME, CXBConfig.BOOK_AUTHOR,2);
    }
    public boolean checkTheWorkDetails(String bookName, String author, boolean back_true) {
        if (back_true) return checkTheWorkDetails(bookName, author, 0);
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
            print.printErr("booName:" + bookName + " 或者author:" + author + " 为空");
            return false;
        }
        devices.sleep(3000);
        if (!"作品详情".equals(devices.getText(
                By.xpath("//android.widget.TextView[contains(@index,1)]")))
                || !bookName.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_name_view")))
                || !devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/item_board_title_view"))
                || !author.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_author_view")))
                || !"在线阅读".equals(devices.getText(TheWorkDetails.ONLINE_READING))
                || !"下载".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_to_download_view")))
                || !"目录".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_to_volume_view")))
                //返回按钮
                || !devices.isElementExsitAndroid(By.className("android.widget.ImageButton"))
                ) {
            print.printErr("点击书籍:" + bookName + "跳转到书籍详情页不正确");
            return false;
        }
        if(devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/adv_plaque_view"))) RecordAd.getRecordAd().setAd("GG-14",
                devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/banner_txt_title")));
       if(devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/adv_notice_content")))
           RecordAd.getRecordAd().setAd("GG-65",devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/adv_notice_content")));
        //分享
        if(!Share_the_call && CXBConfig.CHECK_SHARE)if(!checkShare(bookName,author))return false;
        Share_the_call = false;
        if(WorkType) {
            WorkType=false;
            if (!checkType("书籍介绍")) return false;
            //书籍介绍内容
            if (devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/etv")).length() < 1) {
                print.printErr("检查书籍介绍内容");
                return false;
            }
            if (CXBConfig.BOOK_NAME.equals(bookName)) {
                if (!CXBConfig.BOOK_SYNOPSIS.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/expand_text_view")))) {
                    print.printErr("检查书籍介绍中的内容与config不匹配");
                    return false;
                }
                //展开
                if (unfold) {
                    unfold = false;
                    if (!"展开".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/expand_open_view")))) {
                        print.printErr("检查展开按钮");
                        return false;
                    }
                    devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/expand_open_view"));
                    if (!CXBConfig.BOOK_SYNOPSISOPEN.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/expand_text_view")))) {
                        print.printErr("检查展开后的简介内容不正确");
                    }

                }
            }

            //向下滑动页面到底部
            for (int i = 0; i < 4; i++) {
                devices.swipeToUp(2000);
            }
            if (!checkType("大家都在看")) return false;
            if (!"换一换".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/item_board_title_action_view")))) {
                print.printErr("检查换一换");
                return false;
            }
            if (!checkType("V章补贴声明")) return false;
            if (!"亲爱的用户，本书为VIP图书，本书所有VIP章节费用已由我司为您承担，您可免费阅读本书。".equals(AppXmlUtil.getXMLElement(
                    "//android.widget.TextView(text=亲爱的用户，本书为VIP图书，本书所有VIP章节费用已由我司为您承担，您可免费阅读本书。;)",
                    devices.getPageXml(), "text"
            ))) {
                print.printErr("检查V章补贴声明内容");
                return false;
            }
            if ("".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/item_search_author_tv")))) {
                RecordAd.getRecordAd().setAd("GG-37", devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/item_search_recommend_tv")));
            }
        }
        return true;
    }

    /**
     * 检查作者详情页中的各个类型是否存在
     * @param name
     * @return
     */
    private boolean checkType(String name){
        if(!name.equals(AppXmlUtil.getXMLElement("android.widget.TextView(text="+name+";)",
                devices.getPageXml(),"text"))){
            print.printErr("检查"+name);
            return false;
        }
        return true;

    }
    /**
     * 检查展开按钮
     * @return
     */
    private boolean checkUnfold(){
        //获取书籍介绍
        String unfold = devices.getText("");
        if(unfold==null){
            print.printErr("获取书籍介绍为空");
            return false;
        }
        if(!unfold.endsWith("...")){
            return true;
        }else{
            //点击展开按钮
            devices.clickfindElement("");
        }
        //获取展开后的书籍介绍
        String chickUnfolded = devices.getText("");
        if(chickUnfolded==null){
            print.printErr("点击展开按钮后获取的text为空");
            return false;
        }else{
            if(chickUnfolded.length()<unfold.length()){
                print.printErr("点击展开按钮后获取的text长度小于展开后的长度，展开前:"
                        +unfold.length()+" 展开后:"+chickUnfolded.length());
                return false;
            }
            if(!chickUnfolded.contains(unfold.substring(0,unfold.length()-2))){
                print.printErr("点击展开按钮后获取的tex不包含展开前的介绍"
                        +unfold.length()+" 展开后:"+chickUnfolded.length());
                return false;
            }
        }





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
        devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_download_book_btn"));
        if (!new Read(this.caseName).checkCatalogueExternal()) {
            print.printErr("检查下载后的开始阅读失败");
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
            CXBRunCase.initialize(devices);
            Search search = new Search(this.caseName);
            if (!search.deleteBook(bookName)) return false;
            if (!search.search()) return false;
            if (!search.searchBook(bookName)) return false;
            if (!search.checkSearchResult(bookName, author)) return false;
            System.out.println("    //点击搜索结果页简介");
            devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/search_result_summary_view"));
            devices.sleep(3000);
            if (!checkDetails(bookName, author)) return false;
            System.out.println("    //点击目录按钮");
            devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_to_volume_view"));
            devices.sleep(2000);
            if (!devices.isElementExsitAndroid(By.className("android.widget.ImageButton")) ||
                    !bookName.equals(devices.getText(By.className("android.widget.TextView"))) ||
                    !"目录".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/tabIndicatorView1"))) ||
                    !"书签".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/tabIndicatorView2"))) ||
                    !CXBConfig.BOOK_CHAPTER_FIRST.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/chapterlist_chaptertitle")))
                    ) {
                print.printErr("检查作者详情页中的目录");
                return false;
            }
            System.out.println("    //点击目录中的返回按钮");
            devices.clickfindElement(By.className("android.widget.ImageButton"));
            devices.sleep(2000);
            if (!checkDetails(bookName, author)) return false;
            System.out.println("    //点击下载");
            devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_to_download_view"));
            devices.sleep(2000);
            int downloadTheBox = downloadTheBox(bookName);
            //判断下载框是积分不足还是积分充足
            if (downloadTheBox == 0) {
                return false;
            } else if (downloadTheBox == 1) {
                print.printErr("当前账号积分不充足，请手动检测下载或切换账号后从新运行脚本");
                if (!checkDetails(bookName, author)) return false;
                return true;
            } else {
                System.out.println("    //点击确定兑换按钮");
                devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_buy_book_submit_view"));
                devices.sleep(2000);
                if (!"已加入书架，正在下载中…".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_download_book_title_view"))) ||
                        //com.mianfeia.book:id/dlg_download_book_prg_view进度条
                        !devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_download_book_prg_view")) ||
                        !"开始阅读".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_download_book_btn"))) ||
                        !devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_download_book_prg_tv")).endsWith("%")
                        ) {
                    print.printErr("检查正在下载框失败");
                    return false;
                }
                int num = 0;
                devices.sleep(500);
                long timeStart = (System.currentTimeMillis()) + (1000 * 60 * 10);
                for (int i = 0; ; i++) {
                    if (System.currentTimeMillis() > timeStart) {
                        print.printErr("下载超时10分钟");
                        return false;
                    }
                    String dlg_download_book_prg_tv = devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_download_book_prg_tv"));
                    if (dlg_download_book_prg_tv == null) {
                        print.printErr("获取下载进度:" + dlg_download_book_prg_tv);
                        return false;
                    }
                    num = Integer.parseInt(dlg_download_book_prg_tv.replace("%", ""));
                    if (num == 100) {
                        if (!"下载成功".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_download_book_title_view")))) {
                            print.printErr("下载为100%时，下载状态检查");
                            return false;
                        }
                        if (!checkRead()) return false;
                        break;
                    }
                }
                CXBRunCase.initialize(devices);
                if (!new Search(this.caseName).bookcase_is_Book(bookName)) {
                    print.printErr("下载完成后书架中没有加入书籍：" + bookName);
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
        System.out.println("检查作者详情页成功");
        return true;
    }
    /**
     * 检查分享框
     */
    private boolean checkShare(String bookName,String author){
        //检查分享给好友
        if(!devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/share_activity_title"))){
            devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/title_right_view"));
        }
        if(!"分享给好友".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/share_activity_title"))) ||
                !"微信".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/umeng_share_wechat")))||
                !"朋友圈".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/umeng_share_wechat_wxcircle")))
                ){
            print.printErr("检查分享给好友框架");
            return false;
        }
        //点击微信
        devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/umeng_share_wechat"));
        devices.sleep(5000);
        if(!"微信号/QQ/邮箱登录".equals(devices.getText(By.id("com.tencent.mm:id/k5")))&&
                !"创建新聊天".equals(devices.getText(By.id("com.tencent.mm:id/amo")))){
            print.printErr("检查分享到微信");
            return false;
        }
        devices.backspace();
        Share_the_call = true;
        if (!checkDetails(bookName, author)) return false;
        //点击分享框
        devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/title_right_view"));
        //点击朋友圈
        devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/umeng_share_wechat_wxcircle"));
        devices.sleep(5000);
        if("微信号/QQ/邮箱登录".equals(devices.getText(By.id("com.tencent.mm:id/k5")))){
            devices.backspace();
        }else{
            if(!"这一刻的想法...".equals(devices.getText(By.id("com.tencent.mm:id/dez")))||
                    !("《"+bookName+"》").equals(devices.getText(By.id("com.tencent.mm:id/ddi")))
                    ){
                print.printErr("检查分享到朋友圈");
                return false;
            }
            //点击发送按钮
            devices.clickfindElement(By.id("com.tencent.mm:id/hd"));
            print.printErr("发送了朋友圈分享，书籍名称:"+bookName+" 请到朋友圈中手动检测");
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
        if ( (AppiumMethod.Config.APP_PACKAGE).contains("com.mianfeizs.book"))return 2;
        //所需积分
        String dlg_buy_book_price_view = devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_buy_book_price_view"));
        //已有积分
        String dlg_buy_book_balance_view = devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_buy_book_balance_view"));
        if (Integer.parseInt(dlg_buy_book_price_view.substring(dlg_buy_book_price_view.indexOf("：") + 1, dlg_buy_book_price_view.length()))
                >
                Integer.parseInt(dlg_buy_book_balance_view.substring(dlg_buy_book_price_view.indexOf("：") + 1, dlg_buy_book_balance_view.length()))) {
            if (!AppXmlUtil.getXMLElement("android.widget.TextView(index=3;)",
                    devices.getPageXml(), "text").equals("当前积分余额不足")) {

                print.printErr("检查当前积分余额不足;" + "所需积分:" + dlg_buy_book_price_view + " 已有积分:" + dlg_buy_book_balance_view);
                return 0;
            }
            if (!"去赚积分".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_buy_book_submit_view")))) {
                print.printErr("当前积分余额不足显示去赚积分按钮");
                return 0;
            }
            //点击去赚积分
            devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_buy_book_submit_view"));
            devices.sleep(2000);
            //验证跳转到赚积分页
            if (!new Sidebar(this.caseName).chickShow(Sidebar.ToEarnPoints)) return 0;
            //返回到作者详情页
            devices.backspace();
            return 1;
        } else {
            if (dlg_buy_book_balance_view == null ||
                    !booKName.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_buy_book_name_view"))) ||
                    !CXBConfig.BOOK_NAME_INTEGRAL.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_buy_book_price_view"))) ||
                    !dlg_buy_book_balance_view.startsWith("已有积分") ||
                    !AppXmlUtil.getXMLElement("android.widget.TextView(index=3;)",
                            devices.getPageXml(), "text").equals("下载后无需流量，可离线阅读") ||
                    !"确认兑换".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/dlg_buy_book_submit_view")))
                    ) {
                print.printErr("检查作者详情页中的下载框");
                return 0;
            }
            return 2;
        }
    }


    /**
     * 检查目录
     * @return
     */
    public boolean checkCatalogue(String bookName,String chaptertitle){
        if(bookName==null){
            print.printErr("bookName为空");
            return false;
        }
        if(chaptertitle==null){
            print.printErr("chaptertitle为空");
            return false;
        }
        if(!devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/tabIndicatorView1"))){
            if(!devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_to_volume_view"))){
                print.printErr("目录不存在");
            }else{
                devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/book_detail_to_volume_view"));
            }
        }
        //返回按钮
        if(!devices.isElementExsitAndroid(By.className("android.widget.ImageButton"))||
                !bookName.equals(devices.getText(By.className("android.widget.TextView")))||
                !"目录".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/tabIndicatorView1")))||
                !"书签".equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/tabIndicatorView2")))||
                !chaptertitle.equals(devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/chapterlist_chaptertitle")))
                ){
            print.printErr("作者详情页中的目录页检查");
            return false;
        }
        if(devices.isElementExsitAndroid(By.id(AppiumMethod.Config.APP_PACKAGE+":id/adv_plaque_view")))
            ad.setAd("GG-27",devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/banner_txt_title")));
        return true;
    }
    /**
     * 检查目录中的书签
     * @return
     */
    public boolean checkCatalogueBookmark(String chaptertitile,String addDate){
        devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/tabIndicatorView2"));
        if (chaptertitile == null)
        {
            print.printErr("chaptertitile为空");
            return false;
        }
        if (addDate == null)   {
            print.printErr("addDate为空");
            return false;
        };
        if (chaptertitile.length() > 6) chaptertitile = chaptertitile.substring(0, 6);
        String mark_chapter_title = devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/mark_chapter_title"));
        if (mark_chapter_title == null || !mark_chapter_title.contains(chaptertitile)) {
            print.printErr("检查书签:" + chaptertitile);
            return false;
        }
        String marksDate = devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/mark_date"));
        if (!marksDate.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")) {
            print.printErr("获取书签日期格式检查:" + marksDate);
            return false;
        }
        marksDate = marksDate.substring(0, marksDate.indexOf(":"));
        if (!marksDate.contains(addDate)) {
            print.printErr("检查书签的日期与加入的日期:addDate:" + addDate + " marksDate:" + marksDate);
            return false;
        }
        String mark_content = devices.getText(By.id(AppiumMethod.Config.APP_PACKAGE+":id/mark_content"));
        if (mark_content == null ||mark_content.length()<1) {
            print.printErr("获取的书签描述:mark_content：" + mark_content+"  阅读页中的简介:"+mark_content);
            return false;
        }
        //点击保存的章节
        devices.clickfindElement(By.id(AppiumMethod.Config.APP_PACKAGE+":id/mark_chapter_title"));
        devices.sleep(3000);
        if(!new Read(this.caseName).style(Read.catalog))return false;
        if (!devices.isElementExsitAndroid(By.xpath("//android.widget.TextView[contains(@text,\"" + chaptertitile + "\")]"))) {
            print.printErr("点击书签后，目录没有跳转到相应的章节");
            return false;
        }
        devices.backspace();
        devices.backspace();
        if(new Read2(this.caseName).readAdd_a_bookcase("取消")==0)return false;
        return true;
    }


}
