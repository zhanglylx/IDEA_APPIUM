package CXBCase;

import org.openqa.selenium.By;

/**
 * 元素属性
 */
public class ElementAttributes {
    //首页中的书库
    public static final By STACK_ROOM = By.id(AppiumMethod.Config.APP_PACKAGE+":id/tab_ranks_view");
    //首页中的精品
    public static final By BOUTIQUE = By.id(AppiumMethod.Config.APP_PACKAGE+":id/tab_comp_view");
    //首页中的赚钱
    public static final By MAKE_MONEY = By.id(AppiumMethod.Config.APP_PACKAGE+":id/tab_me_view");


}
