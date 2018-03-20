package zhibohuodong;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class main {
    public static ArrayList<String[]> user = getExcelXlsx.getExcelXlsx();
    private final static String ENCODE = "utf-8";
    public static int num=17355;
    static String video = "11828";
    static    int numMax = 24022;
    public static void main(String[] args) {
        String[] token = excelInfo("token");
        System.out.println(token.length);
        String[] userId = excelInfo("user_id");
        System.out.println(userId.length);
        String[] userName = excelInfo("user_name");
        System.out.println(userName.length);

        for (int i = 320; i < userId.length; i++) {
            StringBuffer url = new StringBuffer();
            StringBuffer sbff = new StringBuffer();
            String nonce = Nonce.nonce();
            url.append("pushType=0");
            url.append("&");
            url.append("userId=");
            url.append(userId[i]);
            url.append("&");
            url.append("platform=android");
            url.append("&");
            url.append("version=");
            url.append(randVersion());
            url.append("&");
            url.append("nonce=");
            url.append(nonce);
            url.append("&");
            url.append("model=");
            url.append(randModel());
            url.append("&");
            url.append("nickname=");
            url.append(getURLEncoderString(userName[i]));
            url.append("&");
            url.append("deviceToken=");
            url.append("&");
            url.append("cnid=1062");
            url.append("&");
            url.append("IMEI=");
            url.append(getIMEI());
            url.append("&");
            sbff.append(userId[i]);
            sbff.append(nonce);
            String UUID = uuid.createUUID();
            sbff.append(UUID);
            url.append("coverKey=");
            url.append(md5(sbff.toString()));
            url.append("&");
            url.append("token=");
            url.append(token[i]);
            url.append("&");
            url.append("reKey=");
            url.append(getRekey());
            url.append("&");
            url.append("videoId=");
            url.append(video);
            url.append("&");
            url.append("requestId=");
            url.append(UUID);
            url.append(System.lineSeparator());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("E:\\zhibo\\data100-"+video+".txt",true);
            String info = url.toString();
            byte[] data = info.getBytes("UTF-8");
            fos.write(data);//将字符串转换的字节写出
            fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(i==630){
                i=320;
            }
            if(num==numMax){
                break;
            }

        }


    }
    private static String getRekey(){
        String s = "video_re_gate_"+video+"_"+num;
        num++;
        return s;
    }
    private static String getApp() {
        String[] str = {"dl", "cj"};
        Random rand = new Random();
        return str[rand.nextInt(2)];
    }

    private static String getIMEI() {
        String s = Integer.toString((int) ((Math.random() * 9 + 1) * 10000000));
        s += Integer.toString((int) ((Math.random() * 9 + 1) * 10000000));
        return s;
    }

    /**
     * URL 转码
     *
     * @return StringText
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        str = str.replace("\n", "\r\n");
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String randModel() {
        return "ceshi-" + (int) ((Math.random() * 9 + 1) * 100000);
    }

    public static String randVersion() {
        String[] str = {"3.2.1", "3.2.2", "3.2.3"};
        Random rand = new Random();
        return str[rand.nextInt(3)];
    }

    public static String[] excelInfo(String str) {
        int i = 0;
        int j = 0;
        String[] st = new String[0];
        for (String[] s : user) {
            if (s[0].contains(str)) {
                for (String t : s) {
                    if (getExcelXlsx.getNullXls(j)) {
                        j++;
                        continue;
                    }
                    st = Arrays.copyOf(st, st.length + 1);
                    st[i] = t;
                    j++;
                    i++;
                }
            }
        }
        return st;
    }

    private static boolean errUser(int n) {
        String[] str = user.get(0);
        if (n >= str.length) return false;
        if ("".equals(str[n])) return false;
        return true;
    }

    public static String md5(String url) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes("UTF-8"));
            byte messageDigest[] = md5.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String t = Integer.toHexString(0xFF & messageDigest[i]);
                if (t.length() == 1) {
                    hexString.append("0" + t);
                } else {
                    hexString.append(t);
                }
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
