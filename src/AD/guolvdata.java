package AD;

import java.io.*;

public class guolvdata {
    public static void main(String[] args) throws Exception {
        BufferedReader bf = new BufferedReader(new FileReader("ad.txt"));
        PrintWriter bw = new PrintWriter(new FileWriter("adData.txt"));
        String msg=null;
        while((msg=bf.readLine())!=null){
            if(msg.contains("cnid=1062")||msg.contains("cnid=1063")){

            }else{
              bw.println(msg);
            }

        }
        bw.flush();
        bw.close();
    }
}
