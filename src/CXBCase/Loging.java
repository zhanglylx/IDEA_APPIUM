package CXBCase;

import org.openqa.selenium.By;

public class Loging extends CaseFrame {
    //登录微信标题
    private final By title = By.id("android:id/text1");
    //登录微信标题名称
    protected static final String title_Text = "登录微信";
    //微信登录提示标题
    private final By title_hint = By.id("android:id/text1");
    //微信登录提示标题名称
    private final String title_hint_Text = "微信号/QQ/邮箱登录";
    //微信登录账号
    private final By account = By.id("com.tencent.mm:id/ga");
    //微信登录账号名称
    private final String accountText = "帐号";
    //微信登录用户名
    private final By userName = By.id("com.tencent.mm:id/ht");
    //微信登录密码
    private final By password = By.xpath("//android.widget.EditText");
    //微信登录密码名称
    protected static final String passwordName = "密码";
    //个人资料中的切换其他账号登录
    protected static final String switch_account = "切换其他账号登录";
    //微信登录按钮
    private final By loging = By.id("com.tencent.mm:id/byd");
    //H5登录
    protected static final String H5LogingUserName = "登录账户";
    //H5登录
    protected static final String H5LogingPassword = "登录密码";
    //H5登录
    protected static final String H5Loging = "登录";

    public Loging(String caseName) {
        super(caseName);
    }

    @Override
    public boolean caseMap() {


        return true;
    }

    /**
     * 检查微信登录页面
     *
     * @return
     */
    public boolean checkWeChatLoginPage() {
        if ((this.title_Text).equals(devices.getText(this.title)) ||
                title_hint_Text.equals(devices.getText(this.title_hint)) ||
                accountText.equals(devices.getText(this.accountText))) {
            System.out.println("登录页面:true");
            return true;
        }
        System.out.println("登录页面:false");
        return false;
    }

    /**
     * 微信登录
     */
    public boolean WeChatRegister(String userName, String password) {
        if (!checkWeChatLoginPage()) return false;
        devices.clickScreen(CXBConfig.slideXY(this.width, this.height, passwordName));
        devices.inputCharacter(userName, userName);
        devices.sleep(500);
        devices.adbInput(password);
        devices.clickfindElement(loging);
        return true;
    }

}
