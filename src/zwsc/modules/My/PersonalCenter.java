package zwsc.modules.My;

import CXBCase.CaseFrame;
import ZLYUtils.JavaUtils;
import ZLYUtils.WindosUtils;
import org.openqa.selenium.By;
import zwsc.modules.FixedCoordinateConfig;

import java.util.Random;

import static ZLYUtils.JavaUtils.getRandomChinese;

/**
 * 我的页面
 * 切换账号未实现
 * 修改头像未实现
 * 绑定手机号未实现
 * 切换其他账号未实现
 * <p>
 * 实现:修改昵称、性别、生日
 */
public class PersonalCenter extends CaseFrame {
    //返回按钮
    public static final By BACK = By.className("android.widget.ImageButton");
    //头像
    public static final By HEADER = By.id("com.chineseall.singlebook:id/iv_user_info_header");
    //绑定手机，赠送待机卷
    public static final By BIND_NUM_FOR_GIFT = By.id("com.chineseall.singlebook:id/tv_user_info_bind_num_for_gift");
    //支付宝
    public static final By ALI_PAY = By.id("com.chineseall.singlebook:id/btn_ali_pay");
    //微信
    public static final By WX_PAY = By.id("com.chineseall.singlebook:id/btn_wx_pay");
    //账号
    public static final By USER_ID = By.id("com.chineseall.singlebook:id/tv_user_info_account");
    //昵称
    public static final By USER_NAME = By.id("com.chineseall.singlebook:id/tv_user_info_nickname");
    //绑定账号
    public static final By BIND_NUM = By.id("com.chineseall.singlebook:id/rl_user_info_bind_num");
    //性别
    public static final By SEX = By.id("com.chineseall.singlebook:id/tv_user_info_sex");
    //生日
    public static final By BIRTHDAY = By.id("com.chineseall.singlebook:id/tv_user_info_bind_birthday");
    //设置密码
    public static final By SETTING_PWD = By.id("com.chineseall.singlebook:id/tv_user_info_setting_pwd");
    //切换账号
    public static final By CHECKOUT_NUM = By.id("com.chineseall.singlebook:id/tv_user_info_checkout_num");
    /**
     * 修改昵称
     */
    //修改昵称title
    public static final By USER_NAME_UPDATE_TITLE = By.className("android.widget.TextView");
    //修改昵称文本框
    public static final By USER_NAME_UPDATE = By.id("com.chineseall.singlebook:id/et_upd_nickname");
    //保存按钮
    public static final By SAVE_BUTTON = By.id("com.chineseall.singlebook:id/btn_upd_nickname");
    /**
     * 修改性别
     */
    //性别男
    public static final By SEX_MAN = By.id("com.chineseall.singlebook:id/tv_dlg_two_item_one");
    //性别女
    public static final By SEX_WOMAN = By.id("com.chineseall.singlebook:id/tv_dlg_two_item_two");
    /**
     * 生日
     */
    //生日确定按钮
    public static final String BIRTHDAY_DETERMINE = "new UiSelector().text(\"确定\")";


    private String id;
    private String name;
    private String sex;
    private String birthday;
    private MyHomepage myHomepage;//用于检查个人中心页面中的昵称与我的页面中的昵称是否相同


    public PersonalCenter(String caseName, MyHomepage myHomepage) throws RuntimeException {
        super(caseName);
        this.myHomepage = myHomepage;
        setKey();
    }

//    public PersonalCenter(String caseName, String name,
//                          String sex, String birthday, MyHomepage myHomepage) throws RuntimeException {
//        super(caseName);
//        setKey();
//        this.personalCenterInitialize = checkPersonalCenterDefault(name, sex, birthday);
//        this.myHomepage = myHomepage;
//    }


    public void setKey() {
        this.name = devices.getText(USER_NAME);
        this.id = devices.getText(USER_ID).trim();
        this.sex = devices.getText(SEX);
        this.birthday = devices.getText(BIRTHDAY);
    }

    /**
     * 检查个人中心页是否正确
     *
     * @return
     */
    public boolean checkPersonalCenterDefault() {
        if (!devices.isElementExsitAndroid(HEADER)) {
            print.printErr("检查图片是否存在");
            return false;
        }
        if (!devices.isElementExsitAndroid(CHECKOUT_NUM)) {
            print.printErr("检查切换其他账号");
        }
        if (this.id == null || !this.id.matches("^z[0-9]{8}$")) {
            print.printErr("默认账号检查:" + this.id);
            return false;
        }
        if (!this.name.equals(myHomepage.getUserName())) {
            print.printErr(errorFormat("检查个人中心名称与我的页面中的名称", this.name, devices.getText(USER_NAME)));
            return false;
        }
//        if (!("书友" + this.id.substring(1, this.id.length()))
//                .equals(this.name)) {
//            print.printErr(errorFormat(
//                    "检查个人中心名称是否为默认",
//                    "书友" + this.id.substring(1, this.id.length()),
//                    devices.getText(USER_NAME)));
//            return false;
//        }

//        if (!checkSexAndBirthdayDefault(this.sex)) return false;
//        if (!checkSexAndBirthdayDefault(this.birthday)) return false;

        return true;
    }

    /**
     * 检查生日和性别默认选择项
     *
     * @return
     */
    public boolean checkSexAndBirthdayDefault(String key) {
        if (!"未设置".equals(key)) {
            print.printErr(errorFormat(
                    "检查生日和性别默认选择项", "未设置", key));
            return false;
        }
        return true;
    }


    /**
     * 检查用户昵称和我的页面昵称是否相同
     *
     * @return
     */
    public boolean checkUserName() {
        this.devices.clickfindElement(BACK);
        this.myHomepage.setKey();
        if (!this.myHomepage.getUserName().equals(this.name)) {
            print.printErr(errorFormat(
                    "检查个人中心用户名称与我的页面中用户名称",
                    this.name, this.myHomepage.getUserName()));
            return false;
        }
        //再次回到个人中心页
        this.devices.clickfindElement(MyHomepage.USER_HEADER);
        return true;
    }

    /**
     * 检查修改生日
     * 只更改年份
     * 1.随机生成向前或向后滑动年份
     * 2.检查年份修改后再我的页面是否修改成功
     * @return
     */
    public boolean checkBirthday() {
        int yearRand = new Random().nextInt(10) + 1;
        //用于向前或向后选择年份
        if (new Random().nextInt(2) == 1) {
            yearRand = Integer.parseInt("-" + yearRand);
        }
        int year = Integer.parseInt(WindosUtils.getDate("yyyy"));
        devices.clickfindElement(BIRTHDAY);
        int[] rand = FixedCoordinateConfig.getPersonalCenterUpdateBirthday(yearRand);
        for (int i = 0; i < Integer.parseInt(String.valueOf(yearRand).replace("-", ""));
             i++) {
            devices.customSlip(rand[0], rand[1], rand[2], rand[3], rand[4]);
        }
        devices.clickfindElement(BIRTHDAY_DETERMINE);
        String update = (String.valueOf(year + yearRand) + WindosUtils.getDate("-MM-dd"));
        if (!update.equals(devices.getText(BIRTHDAY)
        )) {
            print.printErr(errorFormat("检查修改日期", update, devices.getText(BIRTHDAY)));
            return false;
        }
        setKey();
        return true;
    }

    /**
     * 修改性别
     * 修改男和女，检查个人中心页修改成功
     *
     * @return
     */
    public boolean checkUpdateSex() {
        if (!updateSex("男", SEX_MAN)) return false;
        if (!updateSex("女", SEX_WOMAN)) return false;
        return true;
    }

    /**
     * 修改性别
     *
     * @param sex
     * @return
     */
    public boolean updateSex(String sex, By sexButton) {
        devices.clickfindElement(SEX);
        if (!devices.isElementExsitAndroid(SEX_MAN) ||
                !devices.isElementExsitAndroid(SEX_WOMAN)) {
            print.printErr("检查修改男女");
            return false;
        }
        devices.clickfindElement(sexButton);
        if (!sex.equals(devices.getText(SEX))) {
            print.printErr(
                    errorFormat("检查修改性别", sex, devices.getText(SEX)));
            return false;
        }
        setKey();
        return true;
    }

    /**
     * 检查修改用户名
     * 1.进入昵称修改页面
     * 2.删除原昵称
     * 3.随机生成8位中文字符串
     * 4.保存字符串
     * 5.检查个人中心与我的页面昵称修改是否成功
     *
     * @return
     */
    public boolean checkUpdateUserName() {
        devices.clickfindElement(USER_NAME);
        if (!"修改昵称".equals(devices.getText(USER_NAME_UPDATE_TITLE))) {
            print.printErr(errorFormat(
                    "修改昵称", "修改昵称", devices.getText(USER_NAME_UPDATE_TITLE)));
            return false;
        }
        if (!this.name.equals(devices.getText(USER_NAME_UPDATE))) {
            print.printErr(errorFormat(
                    "修改昵称中的名字", this.name, devices.getText(USER_NAME_UPDATE)));
            return false;
        }
        if (!"保存".equals(devices.getText(SAVE_BUTTON))) {
            print.printErr(errorFormat(
                    "修改昵称保存按钮", "保存", devices.getText(SAVE_BUTTON)));
            return false;
        }
        devices.clickScreen(FixedCoordinateConfig.
                getPersonalCenterUpdateName());
        String name = JavaUtils.getRandomChinese(8);
        //输入随机生成的名称
        devices.inputCharacter(USER_NAME_UPDATE, name);
        //点击保存按钮后客户端默认回到个人中心页
        devices.clickfindElement(SAVE_BUTTON);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!name.equals(devices.getText(USER_NAME))) {
            print.printErr(errorFormat(
                    "检查修改昵称后再个人中心页同步错误", name, devices.getText(USER_NAME)));
            return false;
        } else {
            setKey();
        }
        if (!checkUserName()) {
            return false;
        }
        return true;
    }

    /**
     * 在个人中心页退出
     */
    public void backPersonalCenter() {
        devices.clickfindElement(BACK);
    }

    @Override
    public boolean caseMap() {
        if (!checkPersonalCenterDefault()) {
            print.printErr("检查个人页面");
            return false;
        }
        if (!checkUpdateUserName()) return false;
        if (!checkUpdateSex()) return false;
        if (!checkBirthday()) return false;
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public MyHomepage getMyHomepage() {
        return myHomepage;
    }

    public void setMyHomepage(MyHomepage myHomepage) {
        this.myHomepage = myHomepage;
    }
}
