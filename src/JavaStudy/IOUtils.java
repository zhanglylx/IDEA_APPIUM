package JavaStudy;

import java.io.*;

public class IOUtils {
    /**
     * 通过字节流copy文件
     *
     * @param src
     * @param des
     */
    public static void copyChar(File src, File des) throws IOException {
        if (src == null) throw new IllegalArgumentException("src为空");
        if (!src.exists()) throw new IllegalArgumentException("src路径不存在");
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            byte[] isChar = new byte[is.available()];
            is.read(isChar);
            os = new FileOutputStream(des);
            os.write(isChar);
            os.flush();
            print();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) is.close();
            if (os != null) os.close();
        }
    }

    /**
     * 使用字节缓冲流复制文件
     *
     * @param src
     * @param des
     */
    public static void copyBuffered(File src, File des) throws IOException {
        if (src == null) throw new IllegalArgumentException("src为空");
        if (!src.exists()) throw new IllegalArgumentException("src路径不存在");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(des));
            int i = -1;
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
            bos.flush();
            print();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) bis.close();
            if (bos != null) bos.close();
        }
    }

    public static void copyTxt(File src, File des) throws IOException {
        if (src == null) throw new IllegalArgumentException("src为空");
        if (!src.exists()) throw new IllegalArgumentException("src路径不存在:" + src.getPath());
        BufferedReader br = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        try {
            br = new BufferedReader(new FileReader(src));
            bw = new BufferedWriter(new FileWriter(des));
            String str = null;
            while ((str = br.readLine()) != null) {
                bw.write(str);
                bw.newLine();
            }
            bw.flush();
            pw = new PrintWriter(des);
            str = null;
            while ((str = br.readLine()) != null) {
                pw.println(str);
            }
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) br.close();
            if (bw != null) bw.close();
            if (pw != null) pw.close();
        }

    }

    private static void print() {
        System.out.println("完成");
    }
}
