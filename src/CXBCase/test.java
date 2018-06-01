package CXBCase;

import AppTest.AppXmlUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class test {
    Random random = new Random();
    private List<String> list = new ArrayList<String>();

    public static void main(String[] args) {
//        test t = new test();
//        String [] s =new String[1];
//        String [] str = {"qq","ww","aaa"};
//        while(true) {
//            String styleStr = t.randValues(s.toString(),s);
//            String sStr = t.randValues(str.toString(),str);
//            if(styleStr ==null &&
//                    sStr ==null  ){
//                break;
//            }
//            if(styleStr ==null) styleStr = s[0];
//            if(str ==null) sStr = str[0];
//            System.out.println(styleStr+sStr);
//        }
//
//
//        int[] num = {1,5,6,4,3,8,0,-1,-2};
//        for(int i=0;i<num.length-1;i++){
//            for(int j=i+1;j<num.length;j++){
//                if(num[i]>num[j]){
//                    int ss = num[i];
//                    num[i] = num[j];
//                    num[j] = ss;
//                }
//
//            }
//        }
//        System.out.println(Arrays.toString(num));

//        List<String> list = new ArrayList<>();
//        int i=0;
//        while(true) {
//            int n =  str.indexOf("><") ;
//            String s="";
//            if(n!=-1) {
//               s = str.substring(0, n+1);
//                list.add(s);
//                str = str.replace(s,"");
//            }else{
//                list.add(str);
//                break;
//            }
//
//        }
//        for(String s : list){
//            System.out.println(s);
//
//       }

    }

    private static String getXMLText(String xpath, String xmll) {
        xpath = xpath.trim();
        List<String> listXpath = new ArrayList<>();
        //提取出xpath
        while (true) {
            int n = xpath.indexOf("//");
            if (n != -1) {
                listXpath.add(xpath.substring(0, n));
                xpath = xpath.substring(n + 2, xpath.length());

            } else {
                listXpath.add(xpath);

                break;
            }
        }
        String xml = xmll;
        List<String> listXml = new ArrayList<>();
        int i = 0;
        //提取出xml
        while (true) {
            int n = xml.indexOf("><");
            String s = "";
            if (n != -1) {
                s = xml.substring(0, n + 1);
                listXml.add(s);
                xml = xml.replace(s, "");
            } else {
                listXml.add(xml);
                break;
            }

        }

        int listXpathIndex = 0;
        //判断xpath是否在xml中
        for (String s : listXml) {
            if (!s.contains("class")) continue;
            if (listXpathIndex < listXpath.size()) {
                String listXpathStr = listXpath.get(listXpathIndex);

                if (listXpathStr.indexOf("(") == -1) {
                    if (s.contains("class=\"" + listXpathStr + "\"")) listXpathIndex++;
                } else {
                    String listXpathStrClass = listXpathStr.substring(0, listXpathStr.indexOf("("));
                    String[] element = new String[0];
                    while (true) {
                        element = Arrays.copyOf(element, element.length + 1);
                        String strInsertion = listXpathStr.substring(listXpathStr.indexOf("(") + 1, listXpathStr.indexOf(")"));
                        element[element.length - 1] = strInsertion.substring(0, strInsertion.indexOf("=") + 1) +
                                "\"" + strInsertion.substring(strInsertion.indexOf("=") + 1, strInsertion.length()) + "\"";
                        listXpathStr = listXpathStr.substring(listXpathStr.indexOf(")") + 1);
                        if (listXpathStr.indexOf("(") == -1) break;
                    }
                    boolean elementBoolean = true;
                    for (String elementFor : element) {
                        if (!s.contains(elementFor) || !s.contains("class=\"" + listXpathStrClass + "\"")) {
                            elementBoolean = false;
                            break;
                        }
                    }
                    if (elementBoolean) listXpathIndex++;
                }
                if (listXpathIndex == listXpath.size()) return s;
            }

        }


        return "";
    }

    private String randValues(String key, String[] values) {
        String str = null;
        int i = 0;
        //已随机过的随机数
        List<Integer> randList = new ArrayList<Integer>();
        //判断当前值是否已获取，如果当前数组的值都被获取，返回null
        wh:
        while (true) {
            int n = random.nextInt(values.length);
            if (i == values.length) return null;
            str = values[n];
            for (String s : list) {
                //判断值是否被使用过
                if (s != null && s.equals(key + n)) {
                    //判断当前随机数是否出现
                    for (int randlist : randList) {
                        if (randlist == n) continue wh;
                    }
                    randList.add(n);
                    i++;
                    continue wh;
                }
            }
            list.add(key + n);
            break;
        }
        return str;
    }
}
