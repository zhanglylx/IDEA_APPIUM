package AppTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 用于记录log
 */
public class Log {
    public static ArrayList<String> logList = new ArrayList<String>();
    public static ArrayList<String> alllogList = new ArrayList<String>();
    public static ArrayList<String>  succeedLogList = new ArrayList<String>();
    public static void saveLog(){
        File file = new File(System.getProperty("user.dir")+"\\logList.txt");
        saveFile(file,logList);
        file = new File(System.getProperty("user.dir")+"\\alllogList.txt");
        saveFile(file,alllogList);
        file = new File(System.getProperty("user.dir")+"\\succeedLogList.txt");
        saveFile(file,succeedLogList);
    }
    public static void saveFile(File file, ArrayList<String>  list){
        try (FileOutputStream fos = new FileOutputStream(file)) {
            if(file.exists()){
                file.delete();
            }
            for(String s : list){
                s+="\n";
                fos.write(s.getBytes("UTF-8"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
