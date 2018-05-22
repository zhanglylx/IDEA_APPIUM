package SquirrelFrame;

import Squirrel.FlowFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Arrays;

import Squirrel.*;
import Utlis.FrameUtils;
import Utlis.WindosUtils;

/**
 * 窗格类
 */
public class Pane extends JDialog {
    public static final String testFlow = "测试流程";
    public static final String approvalProcess = "审批流程";
    public static final String staffLevel = "组织架构";
    public static final String development = "产品开发流程";
    public static final String administrativeProcess = "行政部流程";
    private static String[] flow;
    public static final String getADLog = "获取广告日志";

    /**
     * 流程二级页面创建方法:在File中创建一个目录，系统会自动加在二级页面中，
     * 在三级页面中加入相应文件即可加入到三级页面中
     */
    static {
//        flow = new String[]{
//                testFlow, approvalProcess, staffLevel, development, administrativeProcess
//        };
        flow = FrameUtils.addFilesShiftArrays(WindosUtils.getDirectoryFilesName(FlowConfig.fileSit),flow);
    }


    public Pane(String buttonText, JDialog frame) {
        super(frame, true);
        setTitle(buttonText);
    }

    public static boolean windowsClose = false;

    /**
     * 设置子窗格
     *
     * @param buttonText
     * @param frame
     */
    public Pane(String buttonText, JFrame frame) {
        super(frame, true);
        setTitle(buttonText);
        switch (buttonText) {
            case HomePage.workFlow:
                setLayout(new GridLayout(3, 3));
                for (String s : flow) {
                    setButton(s);
                }
                break;
            case HomePage.testTools:
                setLayout(new GridLayout(3, 3));
                setButton(getADLog);
        }
        setJDialog();
    }

    /**
     * 设置鼠标监听
     *
     * @param f
     */
    public void buttonMouseListener(JButton f) {
        f.addActionListener(e -> {
            String text = f.getText();
            switch (text) {
                case getADLog:
                    new GetADLog(text, this);
                    break;
              default:
                    new FlowFrame(f.getText(), this);
                    break;

            }
        });
    }

    /**
     * 设置窗口
     */
    public void setJDialog() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowsClose = true;
                super.windowClosing(e);
                setDefaultCloseOperation(2);

            }
        });
        setSize(400, 250);
        setLocation(400, 200);
        setLocationRelativeTo(null);
        setVisible(true);

    }


    /**
     * 设置按钮
     *
     * @param text
     */
    public void setButton(String text) {
        JButton testFlowButton = new JButton(text);
        buttonMouseListener(testFlowButton);
        if (approvalProcess.equals(text)) testFlowButton.setEnabled(false);
        add(testFlowButton);
    }

    public void openFile(JButton j, String file) {
        j.setEnabled(false);
        Utlis.WindosUtils.openFile(FlowConfig.fileSit + file);
    }
}
