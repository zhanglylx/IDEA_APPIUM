package zwsc.modules.My;

import CXBCase.CaseFrame;
import org.openqa.selenium.By;

/**
 * 我的主页
 */
public class MyHomepage extends CaseFrame {
    //用户名称
    public static final By USER_NAME = By.id("com.chineseall.singlebook:id/tv_mine_nickname");
    //签到
    public static final By SIGN = By.id("com.chineseall.singlebook:id/btn_mine_sign_in");
    //广告语
    public static final By AD = By.id("com.chineseall.singlebook:id/tv_mine_ad");
    //用户头像
    public static final By USER_HEADER = By.id("com.chineseall.singlebook:id/rl_mine_header");
    //用户铜币
    public static final By COPPER = By.id("com.chineseall.singlebook:id/tv_mine_copper_coin_count");
    //代金券
    public static final By VOUCHER = By.id("com.chineseall.singlebook:id/tv_mine_tokens_volume_count");
    //vip
    public static final By VIP = By.id("com.chineseall.singlebook:id/ll_mine_vip");
    //领福利
    public static final By WELFARE = By.id("com.chineseall.singlebook:id/ll_mine_welfare");
    //设置
    public static final By SETTING = By.id("com.chineseall.singlebook:id/ll_mine_setting");
    //帮助与反馈
    public static final By HELP = By.id("com.chineseall.singlebook:id/ll_mine_help");
    //用户名称
    private String userName;
    //我的账户
    private String myAccount;
    //铜币
    private int copper;
    //代金券
    private int voucher;
    //VIP特权
    private String vip;
    //领福利
    private String welfare;
    //设置
    private String setting;
    //帮助与反馈
    private String help;

    private PersonalCenter personalCenter;

    public MyHomepage(String caseName) {
        super(caseName);
        setKey();

    }

    /**
     * 获取我的页面中的text
     */
    public void setKey() {
        this.userName = devices.getText(USER_NAME);
        this.copper = getMoney(COPPER);
        this.voucher = getMoney(VOUCHER);
        this.myAccount = devices.getText("text","我的账户");
        this.vip = devices.getText("text","VIP特权");
        this.welfare = devices.getText("text","领福利");
        this.setting = devices.getText("text","设置");
        this.help = devices.getText("text","帮助与反馈");
    }

    public int getMoney(By by) {
        int key;
        String text = devices.getText(by);
        if (text == null) {
            key = -1;
        } else {
            key = Integer.parseInt(text);
        }
        return key;
    }

    public boolean checkMy() {
        if (this.userName == null || this.userName.length() < 1) {
            print.printErr(errorFormat(
                    "用户名检查", "不为空，长度大于等于1", this.userName));
            return false;
        }
        if (!checkText("我的账户", this.myAccount)) return false;
        if (!checkText("VIP特权", this.vip)) return false;
        if (!checkText("领福利", this.welfare)) return false;
        if (!checkText("设置", this.setting)) return false;
        if (!checkText("帮助与反馈", this.help)) return false;
        if (!checkText(this.copper)) return false;
        if (!checkText(this.voucher)) return false;
        return true;
    }

    /**
     * 检查文本是否正确
     *
     * @param
     * @return
     * @throws
     */
    private boolean checkText(String expected, String practical) {
        if (!expected.equals(practical)) {
            print.printErr(errorFormat(
                    "检查模块名称", expected, practical));
            return false;
        }
        return true;
    }

    private boolean checkText(int practical) {
        if (practical == -1) {
            print.printErr(errorFormat(
                    "检查钱财", String.valueOf("大于等于0"), String.valueOf(practical)));
            return false;
        }
        return true;
    }

    @Override
    public boolean caseMap() {
        if (!checkMy()) {
            print.printErr("检查我的页面");
            return false;
        }
        if (!checkPersonalCenter()) return false;

        return true;
    }

    /**
     * 检查个人中心
     *
     * @return
     */
    public boolean checkPersonalCenter() {
        devices.clickfindElement(USER_HEADER);
        this.personalCenter =
                new PersonalCenter("个人中心", this);
        boolean b = this.personalCenter.startCase();
        this.personalCenter.backPersonalCenter();
        return b;
    }

    public String getUserName() {
        return this.userName;
    }
}
