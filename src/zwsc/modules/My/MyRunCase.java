package zwsc.modules.My;

import CXBCase.CaseFrame;

/**
 * 运行我的页面检查
 */
public class MyRunCase extends CaseFrame {
    private MyHomepage myHomepage;
    private PersonalCenter personalCenter;

    public MyRunCase(String caseName) {
        super(caseName);
        this.myHomepage = new MyHomepage("我的主页");
    }

    @Override
    public boolean caseMap() {
        if(!this.myHomepage.startCase())return false;
        return true;
    }
}
