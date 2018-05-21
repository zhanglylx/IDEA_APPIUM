package AppTest;

public class FileUtil {
    /**
     * 检查创建文件的名称是否合法，不合法进行替换
     * @param path
     * @return
     */
    public static String FileNameLegal(String path){
        if (path==null)return null;
        path = path.replace(":","-");
        path = path.replace("<","《");
        path = path.replace(">","》");
        path = path.replace("*","x");
        path = path.replace("?","问号替换");
        path = path.replace("|","-");
        path = path.replace("\"","‘");
        path = path.replace("\\","l");
        path = path.replace("/","l");
        return path;
    }
}
