package Squirrel;

import DataBase.ConnectDataBase;
import SquirrelFrame.OutputText;
import Utlis.SaveCrash;
import com.mysql.jdbc.CommunicationsException;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ZhiBoDataBase {
    private ConnectDataBase cdb ;
    private OutputText opt;
    public static final String DATABASE_USER="root_rw";
    public static final String DATABASE_PASSWORD = "loto5522";
    public ZhiBoDataBase(OutputText opt){
        this.opt = opt;
        try {
            cdb = new ConnectDataBase("mysql");
            cdb.coonnect("192.168.1.246:3306/wwlive",DATABASE_USER,DATABASE_PASSWORD);
            if(!cdb.getCon().isClosed())opt.addText("数据连接成功:192.168.1.246:3306/wwlive\n");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            opt.setText("数据库连接失败");
            cdb=null;
        } catch (CommunicationsException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            opt.setText("数据库连接失败");
            cdb=null;
        } catch (SQLException e) {
            e.printStackTrace();
            SaveCrash.save(e.toString());
            opt.setText("数据库连接失败");
            cdb=null;
        }

    }
    public ConnectDataBase getCdb(){
        return cdb;
    }
}
