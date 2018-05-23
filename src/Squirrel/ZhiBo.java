package Squirrel;

import SquirrelFrame.FrameSqiorrel;
import SquirrelFrame.OutputText;
import Utlis.FrameUtils;
import Utlis.SaveCrash;
import Utlis.TooltipUtil;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.Visibility;
import java.io.BufferedReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static Squirrel.Backpack_gift.refresh;
import static Squirrel.Backpack_gift.refreshEndTherad;
import static Squirrel.ZhiBo.backpack_gift;
import static Utlis.FrameUtils.jdialogClose;

/**
 * 直播工具类
 */
public class ZhiBo extends FrameSqiorrel {
    public static final String backpack_gift = "获取背包礼物";
    public static final String[] zhiboArrays;

    static {
        zhiboArrays = new String[]{backpack_gift};
    }

    public ZhiBo(String title, JDialog jdialog) {
        super(title, jdialog);
        addJButton(zhiboArrays);
        setVisible(true);
    }

    @Override
    public void addButtonMouseListener(JButton f) {
        f.addActionListener(e -> {
            String text = f.getText();
            switch (text) {
                case backpack_gift:
                    new Backpack_gift(this);
            }
        });
    }
}

/**
 * 背包礼物
 */
class Backpack_gift extends JDialog {
    //输出框
    private OutputText opt;
    private JTextField useridText;
    private JButton submit;
    private String userID;
    private ZhiBoDataBase zbdb;
    public static JButton refresh;//刷新按钮
    public  static volatile boolean refreshEndTherad ;//判断线程是否结束
    static{
        refreshEndTherad = false;
    }
    public Backpack_gift(JDialog jDialog) {
        super(jDialog, true);
        setTitle(backpack_gift);
        setResizable(false);//禁止拖拽大小
        setLayout(null);//手动设置布局
        setSize(900, 500);
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel1.add(new JLabel("请输入userid:"));
        userID = null;
        useridText = new JTextField(8);
        jPanel1.add(useridText);
        jPanel1.add((submit = new JButton("提交")));
        refresh = FrameUtils.jbuttonImage("refresh/0.png");
        addJButtonMonitor(refresh);//给刷新按钮添加监听器
        jPanel1.add(refresh);
        addJButtonMonitor(submit);//给提交按钮添加监听器
        jPanel1.setSize(new Dimension(this.getWidth(), 35));
        jPanel1.setBorder(BorderFactory.createEtchedBorder());//设置边框
        add(jPanel1);
        opt = new OutputText();
        JScrollPane jsc = opt.getJsc();
        jsc.setSize(this.getWidth(), this.getHeight() - 100);
        jsc.setLocation(this.getX(), this.getY() + 50);
        setLocationRelativeTo(null);
        add(jsc);
        jdialogClose(this);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                zbdb = new ZhiBoDataBase(opt);
            }
        });
        t.start();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
               while (true){
                   if(zbdb==null || !zbdb.getdataBaseOnline()){
                       submit.setEnabled(false);
                   }else{
                       submit.setEnabled(true);
                       break;
                   }
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            }
        });
        th.start();
        setVisible(true);

    }

    /**
     * 给文本域添加监听器
     */
    public void addJButtonMonitor(JButton j) {
        j.addActionListener(e -> {
            //判断是否为提交按钮
            if (j == submit) {
                runSelect();
            }

        });
    }

    /**
     * 执行查询
     */
    public void runSelect() {
        //检测输入userid是否合法
        if (!useridText.getText().matches("\\d+") && !"*".equals(useridText.getText())) {
            TooltipUtil.errTooltip("输入的userId不正确，请输入整数");
            return;
        } else {
            this.userID = useridText.getText();
        }
        Thread t = new Thread(new Refresh());
        t.start();
        Thread se = new Thread(new Runnable() {
            @Override
            public void run() {
                selectData();
                opt.addText("=======================执行完毕===========================\n");

            }
        });
        se.start();
    }

    /**
     * 查询礼物数据
     */
    public void selectData() {
        ResultSet rs;
        try {
            if (userID == null || ("*").equals(userID)) {
                rs = zbdb.getCdb().selectSql("select * from biz_backpack_gift");
                opt.addText("select * from biz_backpack_gift\n");
            } else {
                rs = zbdb.getCdb().selectSql("select * from biz_backpack_gift where user_id = " + userID);
                opt.addText("select * from biz_backpack_gift where user_id = " + userID+"\n");
            }
            ResultSetMetaData data = rs.getMetaData();


            ArrayList<String> list = new ArrayList<>();
            while (rs.next()) {
                StringBuffer stb = new StringBuffer();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    //获得所有列的数目及实际列数
                    int columnCount = data.getColumnCount();
                    //获得指定列的列名
                    String columnName = data.getColumnName(i);
                    if (!"bp_gift_name".equals(columnName) &&
                            !"bp_gift_price".equals(columnName) &&
                            !"bp_gift_num".equals(columnName) &&
                            !"user_id".equals(columnName) &&
                            !"bp_gift_id".equals(columnName)) continue;
                    switch (columnName) {
                        case "bp_gift_name":
                            columnName = "name";
                            break;
                        case "bp_gift_price":
                            columnName = "price";
                            break;
                        case "bp_gift_num":
                            columnName = "num";
                            break;
                        case "bp_gift_id":
                            columnName = "id";
                            break;
                    }
                    //获得指定列的列值
                    String columnValue = rs.getString(i);
                    String str = columnName + ":" + columnValue;
                    if (!"id".equals(columnName)) str = StringUtils.leftPad(str, 15, " ");
                    stb.append(str);
                    //获得指定列的数据类型
                    int columnType = data.getColumnType(i);
                    //获得指定列的数据类型名
                    String columnTypeName = data.getColumnTypeName(i);
                    //所在的Catalog名字
                    String catalogName = data.getCatalogName(i);
                    //对应数据类型的类
                    String columnClassName = data.getColumnClassName(i);
                    //在数据库中类型的最大字符个数
                    int columnDisplaySize = data.getColumnDisplaySize(i);
                    //默认的列的标题
                    String columnLabel = data.getColumnLabel(i);
                    //获得列的模式
                    String schemaName = data.getSchemaName(i);
                    //某列类型的精确度(类型的长度)
                    int precision = data.getPrecision(i);
                    //小数点后的位数
                    int scale = data.getScale(i);
                    //获取某列对应的表名
                    String tableName = data.getTableName(i);
                    // 是否自动递增
                    boolean isAutoInctement = data.isAutoIncrement(i);
                    //在数据库中是否为货币型
                    boolean isCurrency = data.isCurrency(i);
                    //是否为空
                    int isNullable = data.isNullable(i);
                    //是否为只读
                    boolean isReadOnly = data.isReadOnly(i);
                    //能否出现在where中
                    boolean isSearchable = data.isSearchable(i);
                    stb = addStrBuffer(stb, "");

                }
                list.add(stb.toString());
            }
            Iterator<String> iterable = list.iterator();
            int index = 1;
            while (iterable.hasNext()) {
                opt.addText(index + "：" + iterable.next() + "\n");
                index++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            opt.setText("查询发生异常");
        }finally {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            refreshEndTherad = true;
        }
    }

    private StringBuffer addStrBuffer(StringBuffer stb, String text) {
        stb.append(text);
        stb.append("   ");
        return stb;
    }
}

class Refresh implements Runnable {

    @Override
    public void run() {
        int index=1;
        while (true) {
            String[] arr = new String[0];
            arr=Utlis.FrameUtils.addFilesShiftArrays(Utlis.WindosUtils.getDirectoryFilesName("image/refresh"), arr);
            if(index>=arr.length)index=0;
            refresh.setIcon(new ImageIcon("image/refresh/"+arr[index]));
            index++;
            if(refreshEndTherad){
                refreshEndTherad=false;
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
























