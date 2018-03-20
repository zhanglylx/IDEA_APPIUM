package AppTest;

import AppiumMethod.Tooltip;
import java.io.*;
import java.util.ArrayList;

/**
 * 读取case用例
 */
public class GetCase {
    private ArrayList<String> runATestCaseList = new ArrayList<String>();
    private ArrayList<String> caseList = new ArrayList<String>();
    private static StringBuffer s;
    private File fileConfig;
    private static final String userDirConfigPuth = System.getProperty("user.dir") + File.separator + "case.txt";
    private ArrayList<Integer> num = new ArrayList<Integer>();
    private ArrayList<Integer> fistNum = new ArrayList<Integer>();

    public GetCase() {
        fileConfig = new File(userDirConfigPuth);
        //检测是否存在config文件
        createConfigFile();
        //获取config文件
        getCase();

    }

    private void createConfigFile() {
        if (!fileConfig.exists() || !fileConfig.isFile()) {
            System.out.println("没有找到case文件，创建一个新的case文件到当前目录:" + System.getProperty("user.dir"));
            configValues();
            try {
                if (fileConfig.createNewFile()) {
                    createConfigData();
                } else {
                    System.out.println("config文件创建失败，请手动创建到：" + System.getProperty("user.dir"));
                    System.out.println("格式：");
                    System.out.println(s.toString());
                    Tooltip.errHint("请按照打印的格式进行创建");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Tooltip.errHint("创建config文件发生异常");
            }
        }
    }

    /**
     * 创建config文件默认格式
     */
    private void createConfigData() {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(fileConfig));
            br.write(s.toString());
            br.flush();
            br.close();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("cmd /c " + fileConfig);
            Tooltip.finishHint("case配置文件创建完毕");
        } catch (IOException e) {
            e.printStackTrace();
            Tooltip.errHint("写入config默认文件发生未知错误");
        }
    }

    /**
     * 获取case用例并赋值list
     */
    private void getCase() {

        try (BufferedReader br = new BufferedReader(new FileReader(fileConfig))) {
            String msg = null;
            boolean runAtest = false;
            int n = 1;
            while ((msg = br.readLine()) != null) {
                if (msg.trim().startsWith("runATest")) {
                    runAtest = true;
                }
                if (runAtest && msg.trim().startsWith("caseName")) runAtest = false;
                if (runAtest) {
                    if (!msg.startsWith("-") && !"".equals(msg.trim())) {
                        runATestCaseList.add(msg);
                        fistNum.add(n);
                    }
                }
                if (!msg.startsWith("-") && !"".equals(msg.trim()) ) {
                    caseList.add(msg);
                    num.add(n);
                }
                check(msg);
                n++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查获取到的格式
     */
    private static void check(String msg) {
        if (msg.startsWith("-") || !msg.contains("=")  ) {
            return;
        }
        String text = msg.substring(0, msg.indexOf("=")).trim();
        if (!("clickId".equals(text) || "runATestCaseName".equals(text)
                || "caseName".equals(text) || "existsId".equals(text)
                || "existsClass".equals(text) || "equalsId".equals(text)
                || "equalsClass".equals(text) || "equalsXpath".equals(text)
                || "containsClass".equals(text) || "containsXpath".equals(text)
                || "containsId".equals(text) || "clickClass".equals(text)
                || "wait".equals(text) || "snapshot".equals(text)
                || "clickXpath".equals(text) || "clickExistsXpath".equals(text)
                || "clickExistsId".equals(text) || "clickExistsClass".equals(text)
                || "inputXpath".equals(text) || "inputClass".equals(text)
                || "inputId".equals(text) || "slideDown".equals(text)
                || "slideUp".equals(text) || "slideLift".equals(text)
                || "slideRight".equals(text) || "coordinatesClick".equals(text)
        )) {
            Tooltip.errHint("用例中的事件名称格式不正确:" + text);
        }
    }

    /**
     * case默认格式
     */
    public void configValues() {
        s = new StringBuffer();
        s = sbAppEnd(s, "-请在每一个=后填写，手机请关闭锁屏和密码解锁，注释请在行头加-,调试模式按行单一执行，自定义模式每一次执行case中未执行的行");
        s = sbAppEnd(s, "-点击元素 clickId/Class/Xpath = XXXXXXX");
        s = sbAppEnd(s, "-如果只需要元素存在的时候点击，不存在的时候继续运行其他事件，语法为:clickExistsXXXX=XXX");
        s = sbAppEnd(s, "-如果只需要运行一条用例，caseName前缀填写:runATest,如：runATestCaseName=测试免费电子书");
        s = sbAppEnd(s, "-用例名称格式为:caseName = XXXXXX");
        s = sbAppEnd(s, "-判断元素是否显示在页面中请填写:existsId/Class/Xpath=XXX");
        s = sbAppEnd(s, "-判断元素的text是否正确，格式为:equalsId/Class/Xpath=XXXX;判断的字符");
        s = sbAppEnd(s, "-判断元素的text是否包含case中的结果，请参照equals格式并且将equals替换为:contains");
        s = sbAppEnd(s, "-重新启动APP:restart");
        s = sbAppEnd(s, "-截图：snapshot = XXXXXX          -截图默认地址为桌面_log,运行log保存在本地运行目录");
        s = sbAppEnd(s, "-程序暂停：wait = (时间ms)  如：wait= 2000   等待2秒，所有元素默认等待5秒，注意：等待时间不要超过60秒，超过60秒appium自动停止");
        s = sbAppEnd(s, "-退格：backspace");
        s = sbAppEnd(s, "-输入字符：inputID/Class/Xpath = XXXX;输入的字符");
        s = sbAppEnd(s, "-滑动：slideDown/Up/Lift/Right = 毫秒");
        s = sbAppEnd(s, "-坐标点击：coordinatesClick = x , y");
    }

    /**
     * 给默认config文件添加格式
     *
     * @param s   StringBuffer
     * @param str 需要添加的文件
     * @return StringBuffer
     */
    public static StringBuffer sbAppEnd(StringBuffer s, String str) {
        s.append(str);
        s.append(System.lineSeparator());
        s.append(System.lineSeparator());
        return s;
    }

    public ArrayList<String> getRunATestCaseList() {
        return runATestCaseList;
    }

    public ArrayList<String> getCaseList() {
        return caseList;
    }

    public ArrayList<Integer> getNum() {
        return num;
    }

    public ArrayList<Integer> getFistNum() {
        return fistNum;
    }
}
