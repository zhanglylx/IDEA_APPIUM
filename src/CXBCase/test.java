package CXBCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class test {
    Random random = new Random();
    private List<String> list = new ArrayList<String>();

    public static void main(String[] args) {
        test t = new test();
        String [] s =new String[1];
        String [] str = {"qq","ww","aaa"};
        while(true) {
            String styleStr = t.randValues(s.toString(),s);
            String sStr = t.randValues(str.toString(),str);
            if(styleStr ==null &&
                    sStr ==null  ){
                break;
            }
            if(styleStr ==null) styleStr = s[0];
            if(str ==null) sStr = str[0];
            System.out.println(styleStr+sStr);
        }
    }


    private String randValues(String key,String[] values) {
        String str = null;
        int i = 0;
        //已随机过的随机数
        List<Integer> randList = new ArrayList<Integer>();
        //判断当前值是否已获取，如果当前数组的值都被获取，返回null
        wh:while (true) {
            int n = random.nextInt(values.length);
            if (i == values.length) return null;
            str = values[n];
            for (String s : list) {
                //判断值是否被使用过
                if (s != null && s.equals(key+n)) {
                    //判断当前随机数是否出现
                    for (int randlist : randList) {
                        if (randlist == n) continue wh;
                    }
                    randList.add(n);
                    i++;
                    continue wh;
                }
            }
            list.add(key+n);
            break;
        }
        return str;
    }
}
