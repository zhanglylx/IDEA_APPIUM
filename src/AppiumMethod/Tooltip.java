package AppiumMethod;

import org.apache.poi.util.SystemOutLogger;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.*;
import java.util.Collections;

public class Tooltip {
    static{
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            ((Throwable) e).printStackTrace();
        }
    }
    /**
     * 完成提示框
     * @param Hint
     */
    public static void finishHint(String Hint){
        JOptionPane.showMessageDialog(null, Hint);
        Toolkit.getDefaultToolkit().beep();
        System.exit(0);
    }
    /**
     * 错误提示框
     */
    public static void errHint(String Hint){
        JOptionPane.showMessageDialog(null, Hint, "错误，程序停止",JOptionPane.ERROR_MESSAGE);
        Toolkit.getDefaultToolkit().beep();
        System.exit(0);
    }
    public static void errHint(String Hint,String[] str){
        if(Hint==null){
            Hint = "没有给出错误原因";
        }
        for(String s : str){
        System.out.println("错误信息:"+s);
    }
        Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(null, Hint, "错误，程序停止",JOptionPane.ERROR_MESSAGE);
        System.exit(0);

    }
    public static int userSelection(String text){
        Toolkit.getDefaultToolkit().beep();
        return  JOptionPane.showConfirmDialog(null,
                text, "温馨提示", JOptionPane.YES_NO_OPTION);

    }

}
