package zhibo;

import AppTest.Devices;
import AppTest.Logs;

public class RunZhiBoCase {
    public static void main(String[] args) {
        Devices devices = Devices.getDevices("开启直播");
        devices.sleep(5000);
        new Attention("关注页").startCase();
        Logs.prrLogs();
    }
}
