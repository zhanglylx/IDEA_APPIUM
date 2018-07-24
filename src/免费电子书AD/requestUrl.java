package 免费电子书AD;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.swing.JOptionPane;

import net.sf.json.JSONObject;

public class requestUrl {
    public  static String appname;
    public  static String packname;
    public  static String version;
    public  static String cnid;
    public static String getAdRequest(int gG, int userIdData) {
        return "cnid=" + config.CNID + "&umeng=test_test&version=" + config.VERSION + "&vercode=64&imei=865736034224322&imsi=&uid=19355701&packname=com.mianfeia.book&oscode=25&model=MIX+2&other=a&vcode=64&channelId=1062&mac=a3d76e202891719e398052c0a29e272e&platform=android&appname=cxb&brand=Xiaomi&"
                + "advId=GG-" + gG + "&userId=" + userIdData + "&" + "currId=-1&net=WIFI";
    }

    public static String getAdRequestCurrId(int gG, int userIdData, int CurrId) {
        return "cnid=" + config.CNID + "&umeng=test_test&version=" + config.VERSION + "&vercode=64&imei=865736034224322&imsi=&uid=19355701&packname=com.mianfeia.book&oscode=25&model=MIX+2&other=a&vcode=64&channelId=1062&mac=a3d76e202891719e398052c0a29e272e&platform=android&appname=cxb&brand=Xiaomi&"
                + "advId=GG-" + gG + "&userId=" + userIdData + "&" + "currId=" + CurrId + "&net=WIFI";
    }

    //性能测试数据
    public static String getAdPerformance(int gG, int userIdData, int CurrId) {
        String i=randVercode();
        String cnid = randCnid();
        String mode =randModel();
        String randPackname = randPackname();
        requestUrl.appname = randAppName(randPackname);
        requestUrl.version = randVersions();
        requestUrl.packname = randPackname;
        requestUrl.cnid = cnid;
        return "cnid=" + cnid + "&umeng=test_test&version=" + version + "&vercode="+i+
                "&imei="+randImei()+"&imsi=&uid="+userIdData+
                "&packname="+randPackname+"&oscode="+randOscode()+
                "&model="+mode+"&other=a&vcode="+i+"&channelId="+cnid+
                "&mac="+randMac()+"&platform=android&appname="+requestUrl.appname +"&brand="+mode.substring(0,3)+"&"
                + "advId=GG-" + gG + "&userId=" + userIdData + "&" + "currId=" + CurrId + "&net=WIFI";
    }

    public static String exposureRequest(JSONObject jsonObj, int type, int id, int userIdData) {
        if (jsonObj.get("advId") == null) {
            errorPopupWindow("返回的advId为空");
        }
        if (jsonObj.get("adId") == null) {
            errorPopupWindow("返回的adId为空");
        }
        return "appname="+requestUrl.appname+"&packname="+requestUrl.packname+"&version=" +requestUrl.version+ "&cnid=" + requestUrl.cnid + "&advId="
                + jsonObj.get("advId") + "&type=" + type + "&adId=" + jsonObj.get("adId") + "&id=" + id + "&userId="
                + userIdData + "&platform=android";
    }

    public static void errorPopupWindow(String err) {
        Point list = Point.getPoint();
        JOptionPane.showMessageDialog(null, err, "发现运行错误，程序即将关闭", JOptionPane.ERROR_MESSAGE);
        list.printlnList();
    }

    //随机生成渠道号
    public static String randCnid() {
        Random rd = new Random();
        String str="1061,1062,1063,1064,1065,1066,1067,1068,1069,1070,1071,1072,1073,1074,1075,1076,1077,1078,1079,1080,1081,1082,1083,1084,1085,1086,1087,1089,1090";
        String[] str1 = str.split(",");
        return str1[rd.nextInt(str1.length)];

    }
    public static String randAppName(String packageName){
        if(!("com.mianfeia.book".equals(packageName))&&!("com.mianfeizs.book").equals(packageName)){
            System.out.println("randAppName错误");
            System.exit(0);
        }
        if("com.mianfeia.book".equals(packageName)){
            return "cxb";
        }else{
            return "mfzs";
        }
    }
    //随机选取版本
    public static String randVersions() {
        Random rd = new Random();
        int i = rd.nextInt(4);
        switch (i) {
            case 0:
                return "3.9.3";
            case 1:
                return "4.0.0";
            case 2:
                return "4.1.0";
            default:
                return "4.0.1";
        }
    }
    public static String randImei(){
        return ((int) ((Math.random() * 9 + 1) * 100000000))+""+(int) ((Math.random() * 9 + 1) * 100000);
    }
    //随机生成code
    public static String randVercode(){
        Random rd = new Random();
        int i = rd.nextInt(3);
        switch (i) {
            case 0:
                return "64";
            case 1:
                return "65";
            default:
                return "66";
        }
    }
    public static String randPackname(){
        Random rd = new Random();
        int i = rd.nextInt(2);
        switch (i) {
            case 0:
                return "com.mianfeia.book";
            case 1:
                return "com.mianfeizs.book";
            default:
                return "com.mianfeia.book";
        }
    }

    public  static int randOscode(){
        return (int) ((Math.random() * 9 + 1) * 10);
    }
    public static String randModel(){
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String str1= "_-";
        char[] ch = str.toCharArray();
        char[] ch1 = str1.toCharArray();
        int max=2000000000;
        int min=10000;
        Random random = new Random();
        String s=Integer.toString((int)random.nextInt(max)%(max-min+1)+min);
        String msg="";
        for(int i=0;i<s.length();i++){
            msg=msg+String.valueOf(ch[rand(ch.length)]);
            if(i==4){
                msg=msg+String.valueOf(ch1[rand(ch1.length)]);
            }
            if(i==6){
                msg=msg+String.valueOf(ch1[rand(ch1.length)]);
            }
            if(i==8){
                msg=msg+String.valueOf(ch1[rand(ch1.length)]);
            }
        }
        return msg;
    }
    public static int rand(int i){
        Random rd = new Random();
        return rd.nextInt(i);
    }

    public static String randMac(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }


















}
