package AppiumMethod;

import java.io.IOException;

public class OpenFileDirectory {
    public static void selectedFile(String puth){
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("cmd /c explorer /select,  " + puth);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // 打开转换完成目录
    }

}
