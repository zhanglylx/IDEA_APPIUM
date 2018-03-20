package Print;

import java.io.*;

import java.util.*;

import javax.swing.*;



public class Print {



    public static void main(String[] args) throws IOException {

        new ReadSelectedLine("测试");//新建界面写到了for循环里,就会出现很多的文本框

        //只需要一个界面,界面需要一个参数就是字符串参数显示到文本框 

    }

    //把读取文件内容的文本的代码提取成一个方法,用于读取文字 





}

//界面 

class ReadSelectedLine extends JFrame {

    private static final long serialVersionUID = 1L;

    JTextArea ta = null;

    JScrollPane jsp = null;

    //只要一个参数 

    ReadSelectedLine(String txt) throws IOException {

        this.setVisible(true);

        this.setBounds(500, 200, 800, 600);

        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        ta = new JTextArea();

        jsp = new JScrollPane(ta);

//        ta.setText(txt);
        ta.append(txt);
        for(int i=0;i<10;i++) {

            add(jsp);

            validate();
        }
    }

}