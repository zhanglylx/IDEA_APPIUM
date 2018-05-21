package Squirrel;

import SquirrelFrame.Pane;

import java.io.File;

/**
 * 工作流程配置文件
 */
public final class FlowConfig {
    private FlowConfig(){}
    public static final String[] projectFlow=new String[]{"项目执行流程"};
    public static final String fileSit = "File"+ File.separator;
    public static final String[] administrativeProcessFlow = new String[]{"用章、合同归档及财务单据相关流程",
                        "提交集团法务合同归档登记表【模板】","提交集团财务单据登记表【模板】"};
    public static final String [] staffLevelFlow = {Pane.staffLevel};
    public static final String [] developmentFlow = {Pane.development};

}
