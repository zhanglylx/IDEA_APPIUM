package zwsc.modules;

import AppTest.Devices;
import CXBCase.CaseFrame;
import org.openqa.selenium.By;

/**
 * 我的页面
 */
public class s extends CaseFrame {
    //头像
    public static final By HEADER = By.id("com.chineseall.singlebook:id/iv_user_info_header");
    //支付宝
    public static final By ALI_PAY = By.id("com.chineseall.singlebook:id/btn_ali_pay");
    //微信



    public s(String caseName) {
        super(caseName);
    }

    /**
     * 个人中心
     *
     * @return
     */
    public boolean personalCenter(Devices devices, String name,
                                  String sex, String birthday) {
        if (!devices.isElementExsitAndroid(
                By.id("com.chineseall.singlebook:id/iv_user_info_header"))) {
            print.printErr("检查图片是否存在");
            return false;
        }
        if (!devices.isElementExsitAndroid(
                By.id("com.chineseall.singlebook:id/tv_user_info_checkout_num")
        )) {
            print.printErr("检查切换其他账号");
        }
        String pId = devices.getText(By.id("com.chineseall.singlebook:id/tv_user_info_account"));
        String pName = devices.getText(By.id("com.chineseall.singlebook:id/tv_user_info_nickname"));
        String pSex = devices.getText(By.id("com.chineseall.singlebook:id/tv_user_info_sex"));
        String pBirthday = devices.getText(By.id("com.chineseall.singlebook:id/tv_user_info_bind_birthday"));
        if (pId == null || !pId.matches("^z[0-9]{7}$")) {
            print.printErr("默认账号检查:" + pId);
            return false;
        }
        if (!checkPersonalCenter(pName, name, "书城" + pId.substring(1, pId.length()))) return false;
        if (!checkPersonalCenter(pSex, sex, "未设置")) return false;
        if (!checkPersonalCenter(pBirthday, birthday, "未设置")) return false;


        return false;
    }

    /**
     * 检查个人中心中的text与预期是否相同
     *
     * @return
     */
    public boolean checkPersonalCenter(String pStr, String str, String defaultStr) {
        if (pStr == null) {
            print.printErr("个人中心:未能获取到app中对应的值");
            return false;
        }
        if (str == null) {
            if (!defaultStr.equals(pStr)) {
                print.printErr("个人中心:预期[" + defaultStr + "],实际[" + pStr + "]");
                return false;
            }
        } else {
            if (!pStr.equals(str)) {
                print.printErr("个人中心:预期[" + pStr + "],实际[" + str + "]");
                return false;
            }
        }

        return true;
    }


    @Override
    public boolean caseMap() {
        return false;
    }
}
