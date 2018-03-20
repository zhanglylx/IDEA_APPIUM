package zhibohuodong;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class UserUid {
    public static String[] getUid()  {
        File file = new File("E:\\zhibo\\user.txt");
        String[] str = new String[0];
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            String s = null;
            while ((s = br.readLine()) != null) {
                str = Arrays.copyOf(str, str.length + 1);
                str[i] = s;
            }
            return str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
