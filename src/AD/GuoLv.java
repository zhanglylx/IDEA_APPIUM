package AD;

import java.io.*;
import java.util.Random;

public class GuoLv {
    public static void main(String[] args) throws IOException {
        BufferedReader bis = null;
        BufferedWriter bos = null;
        BufferedWriter bos1 = null;
        BufferedWriter bos2 = null;
        BufferedWriter bos22 = null;
        BufferedWriter bos3 = null;
        BufferedWriter bos33 = null;
        BufferedWriter bos4 = null;
        BufferedWriter bos44 = null;
        System.out.println(System.getProperty("user.dir"));
        try {
            bis = new BufferedReader(
                    new FileReader(new File("ad.txt"))
            );
            bos = new BufferedWriter(
                    new FileWriter(new File("cxbDataPingBiCount.txt"))
            );
            bos1 = new BufferedWriter(
                    new FileWriter(new File("cxbDataCount.txt")));
//            bos2 = new BufferedWriter(
//                    new FileWriter(new File("cxbDataPingBi1.txt")));
//            bos22 = new BufferedWriter(
//                    new FileWriter(new File("cxbData1.txt")));
//            bos3 = new BufferedWriter(
//                    new FileWriter(new File("cxbDataPingBi2.txt")));
//            bos33 = new BufferedWriter(
//                    new FileWriter(new File("cxbData2.txt")));
//            bos4 = new BufferedWriter(
//                    new FileWriter(new File("cxbDataPingBi3.txt")));
//            bos44 = new BufferedWriter(
//                    new FileWriter(new File("cxbData3.txt")));

            String msg = null;
            int i=0;
            while ((msg = bis.readLine()) != null) {
                if ((msg.startsWith("cnid=1062") || msg.startsWith("cnid=1063")) && (msg.contains("4.0.0") || msg.contains("4.0.1"))) {
                        bos.write(msg.replaceAll(" ", "-").substring(0, msg.length()));
                        bos.write("\n");
                    } else {
                        bos1.write(msg.replaceAll(" ", "-"));
                        bos1.write("\n");
                    }
//                if(i<2000) {
//                    if ((msg.startsWith("cnid=1062") || msg.startsWith("cnid=1063")) && (msg.contains("4.0.0") || msg.contains("4.0.1"))) {
//                        bos.write(msg.replaceAll(" ", "-").substring(0, msg.length()));
//                        bos.write("\n");
//                    } else {
//                        bos1.write(msg.replaceAll(" ", "-"));
//                        bos1.write("\n");
//                    }
//                }else if(i<40000){
//                    if ((msg.startsWith("cnid=1062") || msg.startsWith("cnid=1063")) && (msg.contains("4.0.0") || msg.contains("4.0.1"))) {
//                        bos2.write(msg.replaceAll(" ", "-").substring(0, msg.length()));
//                        bos2.write("\n");
//                    } else {
//                        bos22.write(msg.replaceAll(" ", "-"));
//                        bos22.write("\n");
//                    }
//                }else if (i<60000){
//                    if ((msg.startsWith("cnid=1062") || msg.startsWith("cnid=1063")) && (msg.contains("4.0.0") || msg.contains("4.0.1"))) {
//                        bos3.write(msg.replaceAll(" ", "-").substring(0, msg.length()));
//                        bos3.write("\n");
//                    } else {
//                        bos33.write(msg.replaceAll(" ", "-"));
//                        bos33.write("\n");
//                    }
//                }else{
//                    if ((msg.startsWith("cnid=1062") || msg.startsWith("cnid=1063")) && (msg.contains("4.0.0") || msg.contains("4.0.1"))) {
//                        bos4.write(msg.replaceAll(" ", "-").substring(0, msg.length()));
//                        bos4.write("\n");
//                    } else {
//                        bos44.write(msg.replaceAll(" ", "-"));
//                        bos44.write("\n");
//                    }
//                }
//                i++;
            }
//            bos1.flush();
//            bos.flush();
//            bos2.flush();
//            bos22.flush();
//            bos3.flush();
//            bos33.flush();
//            bos4.flush();
//            bos44.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) bis.close();
            if (bos != null) bos.close();
            if (bos1 != null) bos.close();
        }
    }
    public static String baoguangAnddianji(String msg){
        String str = "";
        str +=msg.substring(msg.indexOf("appname="),msg.indexOf("brand="));
        str += msg.substring(msg.indexOf("packname="),msg.indexOf("oscode="));
        str +=msg.substring(msg.indexOf("version="),msg.indexOf("vercode="));
        str +=msg.substring(msg.indexOf("cnid="),msg.indexOf("umeng="));
        str +=msg.substring(msg.indexOf("advId="),msg.indexOf("userId="));
        str +=randNum();
        str +="&";
        str +="id"+id(msg);
        return str;
    }
    public static int randNum(){
        Random rd = new Random();
        return rd.nextInt(2);
    }
    public static String id(String msg){
        Random rd = new Random();
        int i = rd.nextInt(4);
           if(msg.contains("GG-1") && msg.contains("com.mianfeia.book")){
                if(i==0){
                    return "-1";
                }
                if(i==1){
                    return "2321";
                }
                if(i==2){
                    return "";
                }
           }
            return null;
    }
}
