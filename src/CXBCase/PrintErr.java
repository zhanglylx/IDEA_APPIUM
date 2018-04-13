package CXBCase;

import AppTest.Devices;
import com.gargoylesoftware.htmlunit.javascript.host.event.DeviceStorageChangeEvent;

public class PrintErr {
    String caseName;
    Devices devices;
    public  PrintErr(String caseName){

        this.caseName = caseName;
        devices = Devices.getDevices(caseName);
    }
    public void print(String err){
        try{
            p(err);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void p(String err) throws Exception {
        System.out.println(caseName+"["+err+"]：失败");
        devices.snapshot(caseName+"["+err+"]：失败");
        throw new Exception(err+" : 错误日志");
    }
}
