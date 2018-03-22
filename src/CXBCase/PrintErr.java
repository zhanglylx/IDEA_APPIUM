package CXBCase;

import AppTest.Devices;
import com.gargoylesoftware.htmlunit.javascript.host.event.DeviceStorageChangeEvent;

public class PrintErr {
    String caseName;
    Devices devices;
    public  PrintErr(String caseName){

        this.caseName = caseName;
        devices = Devices.getDevices("错误");
    }
    public void print(String err){
        System.out.println(caseName+"["+err+"]：失败");
        devices.snapshot(caseName+"["+err+"]：失败");
    }
}
