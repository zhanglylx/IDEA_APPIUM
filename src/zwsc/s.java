package zwsc;


import AppTest.AppiumRuningLogs;
import AppTest.Devices;
import ZLYUtils.JavaUtils;
import org.openqa.selenium.By;
import zwsc.modules.FixedCoordinateConfig;
import zwsc.modules.My.MyRunCase;
import zwsc.modules.ZWSCUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Random;

public class s {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            Devices devices = Devices.getDevices("打开中文");
            Thread.sleep(8000);
            ZWSCUtils.returnToTheBookshelfStartPage(devices);
            devices.clickfindElement(By.id("com.chineseall.singlebook:id/index_tab_fenlei"));
            if(!new MyRunCase("我的").startCase())break;
            AppiumRuningLogs.printLogs();
        }
    }


    static{
        boolean b=false;
        for(String model : FixedCoordinateConfig.MODELArr){
            if(Devices.getDevices("获取手机品牌").getDevicesBrand().equals(model)){
                b=true;
                break;
            }
        }
        if(!b)throw new IllegalArgumentException("不是指定机型");
    }
}
