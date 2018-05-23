package SquirrelFrame;

import javax.swing.*;
import java.awt.*;

/**
 * 文本输出框
 */
public class OutputText {
    private JTextArea logPaint = null;
    private JScrollPane jsc = null;
    public OutputText(){
        logPaint = new JTextArea();
        logPaint.setLineWrap(true);
        logPaint.setWrapStyleWord(true);
        logPaint.setEditable(false);
        jsc = new JScrollPane(logPaint);
        logPaint.setFont(new Font("标楷体", Font.BOLD, 20));

    }
    public void setText(String text){
        this.logPaint.setText(text);
    }
    public void addText(String text){
        this.logPaint.append(text);
        //下面的代码就是移动到文本域的最后面
        logPaint.selectAll();
        if (logPaint.getSelectedText() != null) {
            logPaint.setCaretPosition(logPaint.getSelectedText().length());
            logPaint.requestFocus();
        }
    }

    public JScrollPane getJsc() {
        return jsc;
    }
}
