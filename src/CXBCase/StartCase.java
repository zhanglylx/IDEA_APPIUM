package CXBCase;

import AppTest.Devices;
import AppTest.Logs;
import org.apache.commons.lang.StringUtils;

/**
 * 抽象类，用于定义用例类格式
 */
public abstract class StartCase {
    public int startX, startY, endX, endY, time, slideNumber;
    public Devices devices;
    public PrintErr print;
    public String caseName;
    public int width;
    public int height;
    RecordAd ad;
    public StartCase(String caseName) {
        if(this.caseName==null || this.caseName.equals(caseName) ){
            System.out.println("开始执行:"+caseName+ StringUtils.repeat("=", 100).toString());
        }else{
            System.out.println("跳转:"+caseName+ StringUtils.repeat("=", 100).toString());
        }
        this.caseName = caseName;
        this.devices = Devices.getDevices(caseName);
        this.print = new PrintErr(caseName);
        this.width = devices.getWidth();
        this.height = devices.getHeight();
        this.ad = RecordAd.getRecordAd();
    }

    public void startCase() {
        Logs.recordLogs(caseName, caseMap());
    }

    abstract public boolean caseMap();
}
