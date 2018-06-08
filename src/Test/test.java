package Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        File html = new File("Kibana.html");
        Map<String,String> map = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(html));
            String str = null;
            while ((str=br.readLine())!=null){
                String currid ="currId=";
                if(str.indexOf("currId=")!=-1){
                    str = str.substring(str.indexOf("currId=")+currid.length(),str.length());
                    if(!map.containsKey(str.substring(0,str.indexOf("&")))){
                        map.put(str.substring(0,str.indexOf("&")),"");
                        System.out.println(str.substring(0,str.indexOf("&")));
                    }

                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
