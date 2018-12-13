package zhibo;

import AppTest.AppXmlUtil;
import CXBCase.CaseFrame;
import org.openqa.selenium.By;

import java.util.Random;

/**
 * 检查关注
 * 1.在首页点击第一个主播
 * 2.进入到直播页
 * 3.进行关注和取消关注的检查
 */
public class Attention extends CaseFrame {
    //直播间名称
    private String liveName;
    //粉丝数
    private int tv_contribute_num;

    public Attention(String caseName) {
        super(caseName);
    }

    @Override
    public boolean caseMap() {
        //进入直播间
        if (!EnterLive()) return false;
        //打开关注页
        if (!openAttention(this.liveName)) return false;
        //检查未关注
        if (!checkDidNotConcern()) return false;
        //检查已关注
        if (!EnterLive()) return false;
        if (!openAttention(this.liveName)) return false;
        if (!checkFollowed()) return false;
        //检查取消关注
        if (!EnterLive()) return false;
        if (!openAttention(this.liveName)) return false;
        if (!checkUnfollow()) return false;
        System.out.println("发地方");
        return true;
    }

    /**
     * 进入首页中的第一个直播间
     *
     * @return
     */
    public boolean EnterLive() {
        ZhiBoUtils.home(this.devices, this.print);
        //第一个直播间的名字
        this.liveName = devices.getText(By.id("com.chineseall.youzi:id/item_home_page_anchor_view"));
        System.out.println("点击第一个直播间");
        devices.clickfindElement(By.id("com.chineseall.youzi:id/item_home_page_brief_view"));
        System.out.println("等待7秒");
        devices.sleep(7000);
        //检查直播页
        if (!ZhiBoUtils.checkLive(this.devices, this.print, this.liveName)) return false;
        return true;
    }

    /**
     * 打开关注页并检查关注
     *
     * @return
     */
    public boolean openAttention(String liveName) {
        System.out.println("打开关注页");
        //点击直播名称
        devices.clickfindElement(By.id("com.chineseall.youzi:id/wgt_anchor_name_view"));
        if (!checkAttention(liveName)) return false;
        return true;
    }

    /**
     * 检查未关注
     *
     * @return
     */
    private boolean checkDidNotConcern() {
        System.out.println("检查未关注");
        if (checkAttentionText("已关注")) {
            if(!clickAttention())return false;
        }
        if (!checkAttentionText("＋关注")) {
            print.printErr("关注字样不正确");
            return false;
        }
        if (!checkDidNotConcern(true, liveName, false)) return false;
        return true;
    }

    /**
     * 检查关注页关注字样
     *
     * @param text
     * @return
     */
    private boolean checkAttentionText(String text) {
        if (text == null) throw new IllegalArgumentException("text为空");
        return text.equals(devices.getText(By.id("com.chineseall.youzi:id/tv_add_to_fav")));
    }

    /**
     * 检查已关注
     *
     * @return
     */
    private boolean checkFollowed() {
        System.out.println("检查已关注");
        if (checkAttentionText("已关注")) {
            if(!clickAttention())return false;
            if (!checkAttentionText("＋关注")) {
                print.printErr("关注字样不正确");
                return false;
            }
        }
        if(!clickAttention())return false;
        if (!checkAttentionText("已关注")) {
            print.printErr("关注字样不正确");
            return false;
        }
        if (checkDidNotConcern(true, liveName, true)) return false;
        return true;
    }

    /**
     * 检查取消关注
     *
     * @return
     */
    public boolean checkUnfollow() {
        int index = (new Random().nextInt(10) + 1) * 5;
        //循环点击关注查看字样，最后取消关注，检查功能正确性
        for (int i = 0; i < index; i++) {
            if (checkAttentionText("＋关注")) {
                if(!clickAttention())return false;
                if (!checkAttentionText("已关注")) {
                    print.printErr("已关注字样不正确");
                    return false;
                }
            } else if (checkAttentionText("已关注")) {
                if(!clickAttention())return false;
                if (!checkAttentionText("＋关注")) {
                    print.printErr("＋关注字样不正确");
                    return false;
                }
            } else {
                print.printErr("关注字样不正确");
                return false;
            }
            if (i == (index - 1)) {
                if (checkAttentionText("已关注")) {
                    if(!clickAttention())return false;
                    if (!checkAttentionText("＋关注")) {
                        print.printErr("＋关注字样不正确");
                        return false;
                    }
                }
            }
        }
        return checkDidNotConcern(true, liveName, false);

    }


    /**
     * 检查关注首页已关注或未关注
     *
     * @param liveAttention    是否从直播页开始检查
     * @param attentionName    直播间名称
     * @param WhetherToFocusOn 是否关注
     * @return
     */
    public boolean checkDidNotConcern(boolean liveAttention, String attentionName, boolean WhetherToFocusOn) {
        System.out.println("检查未关注");
        if (attentionName == null) throw new IllegalArgumentException("attentionName为空");
        if (devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_anchor_dialog_layout"))) {
            //关闭关注页
            devices.backspace();
        }
        if (liveAttention) {
            if (!WhetherToFocusOn) {
                if (!"关注".equals(devices.getText(By.id("com.chineseall.youzi:id/wgt_anchor_attention_view")))) {
                    print.printErr("直播页关注字样不正确");
                    return false;
                }
            } else {
                if ("关注".equals(devices.getText(By.id("com.chineseall.youzi:id/wgt_anchor_attention_view")))) {
                    print.printErr("已关注主播，直播页关注字样未隐藏");
                    return false;
                }

            }
            //进入关注首页
            ZhiBoUtils.home(this.devices, this.print);
            devices.clickfindElement(By.id("com.chineseall.youzi:id/tab_attention_layout"));
            devices.sleep(3000);
            if (WhetherToFocusOn) {
                if (!ZhiBoUtils.checkAttentionDisplayedHomePage(this.devices, this.print, attentionName)) {
                    print.printErr("关注首页没有找到已关注的主播:" + attentionName);
                    return false;
                }
                //点击已关注的直播
                devices.clickScreen(AppXmlUtil.getXMLElement(
                        "android.widget.ListView//android.view.View(text=" + attentionName + ";)", devices.getPageXml(), "bounds")
                );
                devices.sleep(4000);
                System.out.println("检查点击关注的直播后可以进入到直播间");
                if (!ZhiBoUtils.checkLive(this.devices, this.print, attentionName)) return false;
                devices.sleep(5000);
                devices.backspace();
                devices.sleep(2000);
                if (!ZhiBoUtils.checkAttentionDisplayedHomePage(this.devices, this.print, attentionName)) {
                    print.printErr("关注首页没有找到已关注的主播:" + attentionName);
                    return false;
                }
            } else {
                if (ZhiBoUtils.checkAttentionDisplayedHomePage(this.devices, this.print, attentionName)) {
                    print.printErr("关注首页找到已经取消关注的主播:" + attentionName);
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * 检查关注页
     *
     * @return
     */
    public boolean checkAttention(String liveName) {
        //未打开关注页时打开关注页
        if (!devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_anchor_dialog_layout"))) {
            devices.clickfindElement(By.id("com.chineseall.youzi:id/wgt_anchor_name_view"));
        }
        System.out.println("检查关注页");
        if (liveName == null) throw new IllegalArgumentException("liveName为空");
        if (!liveName.equals(devices.getText(By.id("com.chineseall.youzi:id/tv_user_name")))) {
            print.printErr("关注页中的直播房间名称不正确");
            return false;
        }
        System.out.println("检查年龄和星座");
        //年龄
        String tv_user_age = devices.getText(By.id("com.chineseall.youzi:id/tv_user_age"));
        //星座
        String tv_user_constellation = devices.getText(By.id("com.chineseall.youzi:id/tv_user_constellation"));
        if (tv_user_age == null || tv_user_constellation == null) {
            print.printErr("年龄或星座为null");
            return false;
        }
        if (!tv_user_age.matches("^(年龄: )\\d+(岁)$") ||
                !tv_user_constellation.matches("^(星座:).+(座)$")
                ) {
            print.printErr("年龄或星座检查");
            return false;
        }
        System.out.println("检查视频数和粉丝数");
        //视频数
        int tv_follows = Integer.parseInt(devices.getText(By.id("com.chineseall.youzi:id/tv_attention_num")));
        //粉丝数
        setTv_contribute_num();
        if (tv_follows < 1 || tv_contribute_num < 1) {
            print.printErr("视频数和粉丝数不正确");
            return false;
        }
        if (!"视频数".equals(devices.getText(By.id("com.chineseall.youzi:id/tv_follows"))) ||
                !"粉丝数".equals(devices.getText(By.id("com.chineseall.youzi:id/tv_contributes")))
                ) {
            print.printErr("视频数或粉丝数文本不正确");
            return false;
        }
        System.out.println("检查ta说");
        //她说
        String tv_signature = devices.getText(By.id("com.chineseall.youzi:id/tv_signature"));
        if (tv_signature == null || tv_signature.length() < 1) {
            print.printErr("检查ta说");
            return false;
        }
        System.out.println("检查type");
        if (!"发私信".equals(devices.getText(By.id("com.chineseall.youzi:id/tv_send_mail"))) ||
                !"个人主页".equals(devices.getText(By.id("com.chineseall.youzi:id/tv_main_page"))) ||
                !"举报".equals(devices.getText(By.id("com.chineseall.youzi:id/wgt_anchor_panel_report_view")))
                ) {
            print.printErr("检查type");
            return false;
        }
        System.out.println("检查关注字样");
        if (!"＋关注".equals(devices.getText(By.id("com.chineseall.youzi:id/tv_add_to_fav"))) &&
                !"已关注".equals(devices.getText(By.id("com.chineseall.youzi:id/tv_add_to_fav")))) {
            print.printErr("检查关注字样");
        }
        return true;
    }

    /**
     * 点击关注按钮
     */
    private boolean clickAttention() {
        //未打开关注页时打开关注页
        if (!devices.isElementExsitAndroid(By.id("com.chineseall.youzi:id/wgt_anchor_dialog_layout"))) {
            devices.clickfindElement(By.id("com.chineseall.youzi:id/wgt_anchor_name_view"));
        }
        setTv_contribute_num();
        if (checkAttentionText("已关注")) {
            devices.clickfindElement(By.id("com.chineseall.youzi:id/tv_add_to_fav"));
            devices.sleep(3000);
            if (!checkAttentionText("＋关注")) {
                print.printErr("关注字样不正确");
                return false;
            }
            int num = this.tv_contribute_num;
            setTv_contribute_num();
            if ((num - 1) != this.tv_contribute_num) {
                print.printErr("取消关注后粉丝数没有-1,num:" + num + " tv_contribute_num:" + this.tv_contribute_num);
                return false;
            }
        } else if (checkAttentionText("＋关注")) {
            devices.clickfindElement(By.id("com.chineseall.youzi:id/tv_add_to_fav"));
            devices.sleep(3000);
            if (!checkAttentionText("已关注")) {
                print.printErr("关注字样不正确");
                return false;
            }
            int num = this.tv_contribute_num;
            setTv_contribute_num();
            if ((num + 1) != this.tv_contribute_num) {
                print.printErr("关注后粉丝数没有+1,num:" + num + " tv_contribute_num:" + this.tv_contribute_num);
                return false;
            }
        } else {
            print.printErr("关注字样不正确");
            return false;
        }

        return true;
    }

    private void setTv_contribute_num() {
        this.tv_contribute_num = Integer.parseInt(devices.getText(By.id("com.chineseall.youzi:id/tv_contribute_num")));
    }
}

