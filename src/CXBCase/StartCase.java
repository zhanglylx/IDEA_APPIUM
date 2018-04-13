package CXBCase;

import AppTest.Devices;
import AppTest.Logs;

public abstract class StartCase {
    int startX, startY, endX, endY, time, slideNumber;
    Devices devices;
    PrintErr print;
    String caseName;

    public StartCase(String caseName) {
        this.caseName = caseName;
        devices = Devices.getDevices(caseName);
        print = new PrintErr(caseName);

    }

    public void startCase() {
        Logs.recordLogs(caseName, caseMap());
    }

    abstract public boolean caseMap();
}
