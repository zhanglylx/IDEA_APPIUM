package Utlis;

import java.text.SimpleDateFormat;

/**
 * 系统时间类
 */
public class Date {
    //系统时间
    public static String systemTime;
    static{
        java.util.Date date = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        systemTime = sdf.format(date);
    }
}
