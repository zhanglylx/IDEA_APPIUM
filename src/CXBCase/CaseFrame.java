package CXBCase;

import AppTest.Devices;
import AppTest.AppiumRuningLogs;
import org.apache.commons.lang.StringUtils;

import java.io.FileNotFoundException;

/**
 * 抽象类，用于定义用例类格式
 */
public abstract class CaseFrame {
    public int startX, startY, endX, endY, time, slideNumber;
    public Devices devices;
    public PrintErr print;
    public String caseName;
    public int width;
    public int height;
    RecordAd ad;

    public CaseFrame(String caseName) {
        if (this.caseName == null || this.caseName.equals(caseName)) {
            System.out.println("开始执行:" + caseName + StringUtils.repeat("=", 100).toString());
        } else {
            System.out.println("跳转:" + caseName + StringUtils.repeat("=", 100).toString());
        }
        this.caseName = caseName;
        this.devices = Devices.getDevices(caseName);
        this.print = new PrintErr(caseName);
        this.width = devices.getWidth();
        this.height = devices.getHeight();
        this.ad = RecordAd.getRecordAd();
    }

    public boolean startCase() {
        boolean b = false;
        try {
            b = caseMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppiumRuningLogs.recordLogs(caseName, b);
        return b;
    }

    /**
     * 将错误日志字符串格式化
     *
     * @param checkInfo 检查内容
     * @param expected  预期结果
     * @param practical 实际结果
     * @return
     */
    public String errorFormat(String checkInfo, String expected, String practical) {
        return checkInfo + "{预期结果[" + expected + "],实际结果[" + practical + "]}";
    }

    abstract public boolean caseMap() throws Exception;
}
