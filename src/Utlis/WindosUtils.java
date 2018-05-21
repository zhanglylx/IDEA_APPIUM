package Utlis;

import AppTest.Devices;
import AppiumMethod.Tooltip;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WindosUtils {
    /**
     * 打开文件
     *
     * @param file
     * @throws IOException
     */
    public static void openFile(String file) {
        File fe = new File(file);
        String err = file;
        if (!fe.exists() && (file = getPuth(file)) == null) {
            TooltipUtil.errTooltip(err + "没有找到");
            return;
        }
        try {
            cmd("cmd /c explorer " + file);
            System.out.println(file);
        } catch (IOException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            TooltipUtil.errTooltip("打开" + file + "失败，请联系管理员");
        }
    }
    public static void copyFile(JDialog jdialog, String filePath,JButton jbutton){
        if(copyFile(jdialog,filePath)){
            jbutton.setIcon(new ImageIcon(("image/succeed.png" )));
        }else{
            jbutton.setIcon(new ImageIcon(("image/err.png" )));
        }
    }

    /**
     * 复制文件
     * @param jdialog
     * @param filePath
     */
    public static boolean copyFile(JDialog jdialog, String filePath){
        File fe = new File(filePath);
        String err = filePath;
        if (!fe.exists() && (filePath = getPuth(filePath)) == null) {
            TooltipUtil.errTooltip(err + "没有找到");
            return false;
        }
        fe = new File(filePath);
        //文件名称
        String fileName =filePath.substring(filePath.lastIndexOf(File.separator)+1,filePath.length());
        String copyPath=null;
        try {
            copyPath=FrameUtils.saveFileFrame(jdialog, new File(filePath));
        }catch (IllegalArgumentException e){
            SaveCrash.save(e.toString());
        }
        if(copyPath == null)return false;
        copyPath +=fileName;
        if(new File(copyPath).exists()){
            TooltipUtil.errTooltip("文件已存在:"+filePath);
            return false;
        }
        InputStream ips = null;
        OutputStream ops = null;
        try{
            ips = new FileInputStream(fe);
            byte[] ipsBuffer = new byte[ips.available()];
            ips.read(ipsBuffer);
            ops = new FileOutputStream(copyPath);
            ops.write(ipsBuffer);
            ops.flush();
            TooltipUtil.generalTooltip("保存成功:"+copyPath);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(ops!=null){
                try {
                    ops.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(ips!=null){
                try {
                    ips.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 选中文件
     *
     * @param File
     * @throws IOException
     */
    public static void selectFile(String File) throws IOException {
        cmd("cmd /c explorer  /select, " + File);
    }

    /**
     * 执行windows系统dos命令
     *
     * @param code
     * @return
     */
    public static String[] cmd(String code) throws IOException {
        Process pro;
        BufferedReader br = null;
        String[] arr = new String[0];
        try {
            pro = Runtime.getRuntime().exec(code);
            br = new BufferedReader(new InputStreamReader(pro.getInputStream(), Charset.forName("GBK")));
            String str = null;
            while ((str = br.readLine()) != null) {
                arr = Arrays.copyOf(arr, arr.length + 1);
                arr[arr.length - 1] = new String(str.getBytes(), "GBK");
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return arr;
    }

    /**
     * 获取本机IP地址
     *
     * @return
     */
    public static String getLocalIP() {
        String ip = "";
        try {
            String[] str = cmd("ipconfig");
            String title = "";
            String index = "";
            for (String s : str) {
                if ((s.contains(":") && !s.contains(".")) || s.contains("适配器")) {
                    title = s;
                }
                if (s.contains("IPv4") || s.toUpperCase().contains("IPv4".toUpperCase())) {
                    if (!title.equals(index)) {
                        ip += title + "\n";
                        index = title;
                    }
                    ip += s + "\n";
                    ip += "\n";
                }

            }
        } catch (IOException e) {
            SaveCrash.save(e.toString());
        }
        return ip;
    }

    /**
     * 自动搜索文件地址
     *
     * @return
     */
    public static String getPuth(String fileName) {
        String msg = null;
        String name = fileName;
        String file = fileName.substring(fileName.lastIndexOf(File.separator) + 1
                , fileName.length());
        fileName = fileName.substring(0, fileName.lastIndexOf(File.separator) + 1);
        file = "*" + file + "*";
        fileName += file;
        try {
            Process pro = Runtime.getRuntime().exec("cmd /c dir/s/a/b " + fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream(), Charset.forName("GBK")));
            while ((msg = br.readLine()) != null) {
                if (msg.contains(name)) {
                    return msg;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
