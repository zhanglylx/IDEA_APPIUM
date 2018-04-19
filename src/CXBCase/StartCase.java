package CXBCase;

import AppTest.Devices;
import AppTest.Logs;

/**
 * 抽象类，用于定义用例类格式
 */
public abstract class StartCase {
    int startX, startY, endX, endY, time, slideNumber;
    Devices devices;
    PrintErr print;
    String caseName;
    int width;
    int height;
    public StartCase(String caseName) {
        this.caseName = caseName;
        this.devices = Devices.getDevices(caseName);
        this.print = new PrintErr(caseName);
        this.width = devices.getWidth();
        this.height = devices.getHeight();
    }

    public void startCase() {
        Logs.recordLogs(caseName, caseMap());
    }

    abstract public boolean caseMap();
}
