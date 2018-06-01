package SquirrelFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.TimerTask;

import Utlis.Adb;
import Utlis.FrameUtils;
import Utlis.JavaUtils;
import Utlis.WindosUtils;
import com.worm.StratWorm;

/**
 * 首页
 */
public class HomePage extends JFrame {
    //选择的项目
    private String projectName;
    public static final String ZWSC = "中文书城";
    public static final String CXB = "免费电子书";
    public static final String ZHIBO = "直播";
    public static final String clearIphone = "清理手机环境";
    public static final String worm = "贪食蛇";
    public static final String getLocalIP = "获取本机IP地址";
    public static final String workFlow = "工作流程";
    public static final String testTools = "测试工具";
    private JButton clearIphoneButtpm;
    private JButton getLocalIPButton;
    private static HomePage homePage;
    public static TextArea textArea;
    private JButton refresh;
    //动画
    public  static JButton cartoon;
    private HomePage() {
        super(Config.TOOLSTITLE);
        JPanel jpanel = new JPanel();       //添加项目
        jpanel.setLayout(new GridLayout(6, 1));//设置布局
        jpanel.add(new Menubar());    //添加菜单条
        jpanel.add(panelProject()); //添加项目
        jpanel.add(panelClearIphone());
        jpanel.add(panelGetLocalIp());
        jpanel.add(addButton(testTools, ""));
//        jpanel.add(panelBottom());    //添加关于按钮
       setSize(300, 100);//设置窗体宽高
//        setLocationRelativeTo( null);//设置窗体位置,中间显示
        setLocation(100,50);
        setIconImage(
                Toolkit.getDefaultToolkit().getImage("image/logo.png")
        );
        setLayout(new GridLayout(2, 1));//设置框架布局
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { //设置退出监听器
                super.windowClosing(e);
                WindosUtils.closeProcess("adb.exe");
                System.exit(0);
            }
        });
        add(jpanel);
        add(panelLogo());
        pack();//自适应
        setVisible(true);//设置窗口可见
        Thread t = new Thread(new Cartoon());
        t.start();
    }

    public static HomePage getHomePage() {
        if (homePage == null) homePage = new HomePage();

        return homePage;
    }

    /**
     * 设置JRadioButton监听器
     *
     * @param f
     */
    private void jRadioButtonMouseListener(JRadioButton f) {
        f.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = f.getText();
                if (ZWSC.equals(text) ||
                        ZHIBO.equals(text) ||
                        CXB.equals(text)) {
                    projectName = f.getText();
                    clearIphoneButtpm.setEnabled(true);
                }
            }
        });
    }

    /**
     * 设置按钮监听器
     *
     * @param f
     */
    private void buttonMouseListener(JButton f) {
        f.addActionListener(e -> {
            //判断是否为刷新按钮
            if(refresh.equals(f)){
                textArea.setText("正在刷新");
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Adb.setDevices();
                    }
                });
                t.start();
                return;
            }
            String text = f.getText();
            switch (text) {
                case worm:
                    new StratWorm().start();
                    break;
                case clearIphone:
                    handleClickEvents(f);
                    clearIphoneButtpm.setEnabled(false);
                    break;
                case workFlow:
                    new Pane(text, this);
                    break;
                case testTools:
                    new Pane(text, this);
                    break;
                default:
                    new WindowsText(text, this);

            }
        });
    }

    /**
     * 处理点击事件
     *
     * @param f
     */
    private void handleClickEvents(JButton f) {
        String text = f.getText();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (clearIphone.equals(text)) {
                    new ClearIphone(projectName);
                    clearIphoneButtpm.setEnabled(true);
                }
            }
        });
        t.start();
    }

    /**
     * logo面板
     *
     * @return
     */
    private JButton panelLogo() {

        JPanel jp = new JPanel();
//        cartoon = FrameUtils.jbuttonImage("image/test.png");
        cartoon = new JButton();
        cartoon.setBorderPainted(false);// 不绘制边框
        cartoon.setContentAreaFilled(false);//透明的设置
        jp.setLayout(new FlowLayout());
        jp.setPreferredSize(new Dimension(200, 100));
        jp.setBorder(BorderFactory.createEtchedBorder());
        jp.add(cartoon);
        return cartoon;
    }







    /**
     * 页面最底部按钮
     *
     * @return
     */
    private JPanel panelBottom() {
        JPanel p4 = new JPanel();
        JButton regardsButton = new JButton(Menubar.regards);
        p4.setLayout(new BorderLayout());
        p4.add(regardsButton, BorderLayout.EAST);
        buttonMouseListener(regardsButton);
        return p4;
    }

    /**
     * 项目面板
     *
     * @return
     */
    private JPanel panelProject() {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("请选择一个项目:"));
        JRadioButton zwsc = new JRadioButton("中文书城");
        JRadioButton cxb = new JRadioButton("免费电子书");
        JRadioButton zhibo = new JRadioButton("直播");
        jRadioButtonMouseListener(zwsc);
        jRadioButtonMouseListener(cxb);
        jRadioButtonMouseListener(zhibo);
        // 单选按钮组,同一个单选按钮组的互斥.
        ButtonGroup group = new ButtonGroup();
        group.add(zwsc);
        group.add(cxb);
        group.add(zhibo);
        p1.add(zwsc);
        p1.add(cxb);
        p1.add(zhibo);
        p1.setLayout(new FlowLayout(0));
        p1.add(new JLabel("当前设备名称:"));
        textArea = new TextArea(200, 100);
        Adb.setDevices();
        setRefresh();
        p1.add(refresh);
        p1.add(textArea);
        return p1;
    }

    /**
     * 刷新按钮
     */
    private void setRefresh() {

        refresh = new JButton();     //添加刷新按钮
//        refresh.setBorder(BorderFactory.createRaisedBevelBorder());//设置凸起来的按钮
        refresh.setContentAreaFilled(false);//透明的设置
        ImageIcon icon1 = new ImageIcon(("image/refresh.png"));  // 设置按钮背景图像
        refresh.setMargin(new Insets(0, 0, 0, 0)); // 设置按钮边框与边框内容之间的像素数
        refresh.setIcon(icon1);
//      refresh.setBorderPainted(false);// 不绘制边框
        refresh.setFocusable(true);  // 设置焦点控制
        buttonMouseListener(refresh);
    }

    /**
     * 清理手机面板
     *
     * @return
     */
    private JPanel panelClearIphone() {
        //添加clearIphone按钮和worm按钮
        JPanel p2 = new JPanel();
        clearIphoneButtpm = new JButton(clearIphone);
        clearIphoneButtpm.setEnabled(false);
        JButton wormButton = new JButton(worm);
        p2.add(clearIphoneButtpm);
        buttonMouseListener(clearIphoneButtpm);
        p2.add(wormButton);
        buttonMouseListener(wormButton);
        p2.setLayout(new GridLayout(1, 2));
        return p2;
    }


    /**
     * 添加按钮
     *
     * @return
     */

    private JPanel addButton(String buttonText1, String buttonText2) {
        //添加clearIphone按钮和worm按钮
        JPanel p2 = new JPanel();
        JButton jb = new JButton(buttonText1);
        JButton wormButton = new JButton(buttonText2);
        p2.add(jb);
        buttonMouseListener(jb);
        p2.add(wormButton);
        buttonMouseListener(wormButton);
        p2.setLayout(new GridLayout(1, 2));
        return p2;
    }

    /**
     * 获取本机IP地址
     *
     * @return
     */
    private JPanel panelGetLocalIp() {
        JPanel p = new JPanel();
        getLocalIPButton = new JButton(getLocalIP);
        p.add(getLocalIPButton);
        buttonMouseListener(getLocalIPButton);
        JButton work = new JButton(workFlow);
        p.add(work);
        buttonMouseListener(work);
        p.setLayout(new GridLayout(1, 2));
        return p;

    }


    public static void main(String[] args) {
         HomePage.getHomePage();
    }


}

/**
 * 动画类
 */
class Cartoon implements Runnable {

    int i= 0;
    int tab=1;
    @Override
    public void run() {
       HomePage.cartoon.setIcon(new ImageIcon("image/test.png"));
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File fileImage = null;
        File fileTab = null;
        String imagePath = "image" + File.separator + "Cartoon" + File.separator + "tab";
        while (true) {
            if ((fileTab = new File(
                    imagePath + tab)).isDirectory()
                    ) {
                if ((fileImage = new File(fileTab.getPath() +File.separator+ i + ".png")).exists()
                        ) {
                    HomePage.cartoon.setIcon(new ImageIcon(fileImage.getPath()));
                    i++;
                } else {
                    JavaUtils.sleep(5000l);
                    i=0;
                    tab++;
                }
            }else{

                break;
            }
            JavaUtils.sleep(100);
        }
    }
}
