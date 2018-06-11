package Squirrel;

import javax.swing.*;
import java.awt.*;


/**
 * 录屏与截图
 */
public class VideoRecordingScreenshot extends JDialog {
    private JButton screenshot;
    private JButton recordVideo;
    public static final String SCREENSHOT = "截屏";
    public VideoRecordingScreenshot(String title, JDialog jDialog) {
        super(jDialog, false);
        setTitle(title);
        setLayout(null);
        screenshot = new JButton(SCREENSHOT);
        screenshot.setSize(60,40);
        screenshot.setLocation(0,0);
        buttonMouseListener(screenshot);
        add(screenshot);
        setLocationRelativeTo(null);//设置中间显示
        setSize(500, 500);

        setVisible(true);


    }
    /**
     * 设置鼠标监听
     *
     * @param f
     */
    public void buttonMouseListener(JButton f) {
        f.addActionListener(e -> {
            String text = f.getText();

        });
    }
}

/**
 * 截取图片并实时刷新
 */
class RefreshTheImage implements Runnable {

    @Override
    public void run() {
        //adb shell screencap -p /sdcard/1.png
                String[] adb = Utlis.Adb.operationAdb("shell screencap - p /sdcard/Squirrel.png");
                if(adb == null)return;

    }
}