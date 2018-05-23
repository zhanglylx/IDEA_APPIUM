package Utlis;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;

public class FrameUtils {
    /**
     * 设置Jbutton按钮中的图片
     * @param imageName
     * @return
     */
    public static JButton jbuttonImage(String imageName) {
        JButton jbutton = new JButton();     //添加刷新按钮
//        jbutton.setBorder(BorderFactory.createRaisedBevelBorder());//设置凸起来的按钮
        jbutton.setContentAreaFilled(false);//透明的设置
        ImageIcon icon1 = new ImageIcon(("image/" + imageName));  // 设置按钮背景图像
        jbutton.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮边框与边框内容之间的像素数
        jbutton.setIcon(icon1);
        jbutton.setBorderPainted(false);// 不绘制边框
        jbutton.setFocusable(true);  // 设置焦点控制
        return jbutton;
    }

    /**
     * 保存文件选择框
     * @return
     */
    public static String saveFileFrame(Component parent,File fileName) throws IllegalArgumentException{
        if(!fileName.exists()){
           throw new IllegalArgumentException("fileName不存在:"+fileName);
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setBackground(Color.black);
        FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
        fileChooser.setDialogTitle("请选择要保存的路径");
        fileChooser.setApproveButtonText("确定");
        fileChooser.setSelectedFile(fileName);
        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());//默认桌面
//        fileChooser.setAcceptAllFileFilterUsed(false);//去掉所有文件选项
        int ch = fileChooser.showDialog(parent, "保存文件");
        if (JFileChooser.APPROVE_OPTION == ch) {
            String path = fileChooser.getSelectedFile().getPath();
            path = path.substring(0,path.lastIndexOf(File.separator)+1);
            return path;
        }
        return null;
    }

    /**
     * 将file中文件名称转换成数组
     * @param files
     * @param arrays
     * @return
     */
    public static String[] addFilesShiftArrays(File[] files,String[] arrays){
        if(arrays==null)arrays=new String[0];
        for (File file : files) {
            arrays = Arrays.copyOf(arrays, arrays.length + 1);
            arrays[arrays.length-1]=file.getName();
        }
        return arrays;
    }

    /**
     * 添加JDialog窗口关闭监听器
     * @param jDialog
     */
    public static void jdialogClose(JDialog jDialog){
        jDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                jDialog.setDefaultCloseOperation(2);

            }
        });
    }

}
