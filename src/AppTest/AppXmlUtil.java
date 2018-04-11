package AppTest;

import AppiumMethod.Config;

import java.util.*;

/**
 * 用于解析xml，获取xml中的参数或单行数据
 */
public class AppXmlUtil {
    private static int checkListBooleanListA;
    private static int checkListBooleanListB;

    /**
     * 获取xml当中指定的值
     * 如:AppXmlUtil.getXMLElement
     * ("android.support.v7.widget.RecyclerView(index=1;)(resource-id=com.mianfeia.book:id/content_grid_view)//
     * android.widget.LinearLayout(index=2;)//android.widget.TextView(text=更新42章;)", str,"class")
     *
     * @param xpath
     * @param xml
     * @param attribute
     * @return
     */
    public static String getXMLElement(String xpath, String xml, String attribute) {
        if (xpath == null) return "xpath=null";
        if (xml == null) return "xml=null";
        if (attribute == null) return "attribute=null";
        String str = getXML(xpath, xml);
        if (str == null) return "getXML_xpath=null";
        try {
            str = str.substring(str.indexOf(attribute), str.length());
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            return attribute + "=null";
        }
        String s = str;
        s = s.substring(0, s.indexOf("="));
        if (!attribute.equals(s)) return attribute + "=null";
        str = str.substring(0, str.indexOf("\"", str.indexOf("\"") + 1));
        return str.substring(str.indexOf("=") + 1, str.length()).replace("\"", "");

    }

    public static String getXML(String xpath, String xml) {
        checkListBooleanListA = 0;
        checkListBooleanListB = 0;
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
        List<String> listXml = new ArrayList<>();
        //提取出xml
        while (true) {
            int n = xml.indexOf("><");
            String s = "";
            if (n != -1) {
                s = xml.substring(0, n + 1);
                listXml.add(s);
                xml = xml.substring(s.length(), xml.length());
            } else {
                listXml.add(xml);
                break;
            }

        }

        //判断xpath是否在xml中
        Map<String, Integer> listMap = new HashMap<>();
        int i = 0;
        for (String strListXml : listXml) {
            //记录xml树
            String listXpathStr = listXpath.get(i);
            boolean listBoolean = false;
            if (listXpathStr.indexOf("(") != -1) {
                String listXpathStrClass = listXpathStr.substring(0, listXpathStr.indexOf("("));
                try {
                    if (checkElement(elementArr(listXpathStr), strListXml, listXpathStrClass)) {
                        if (i == listXpath.size() - 1) return strListXml;
                        listMap.put(classStr(listXpath.get(i) + i), i);
                        listBoolean = true;
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    return null;
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                    return null;
                }
            } else if (listXpathStr.indexOf("(") == -1) {
                if (strListXml.contains("class=\"" + listXpathStr + "\"")) {
                    if (i == listXpath.size() - 1) return strListXml;
                    listMap.put(classStr(listXpath.get(i) + i), i);
                    listBoolean = true;
                }
            }
            if (listBoolean) i = checkListBoolean(listMap, strListXml, i);
            if (i >= listXpath.size()) i = listXpath.size() - 1;
            checkListBooleanListA++;
        }


        return null;
    }

    private static boolean checkElement(String[] element, String strListXml, String listXpathStrClass) {
        boolean elementBoolean = true;
        for (String elementFor : element) {
            if (!strListXml.contains(elementFor) || !strListXml.contains("class=\"" + listXpathStrClass + "\"")) {
                elementBoolean = false;
                break;
            }
        }
        return elementBoolean;
    }

    private static int checkListBoolean(Map<String, Integer> listBoolean, String strListXml, int i) {
        if (strListXml.endsWith("/>")) return i;
        if (strListXml.startsWith("</")) {
            String str = strListXml.substring(strListXml.indexOf("</") + 2, strListXml.length() - 1);
            if (listBoolean.containsKey(str + i)) {
                if (checkListBooleanListA == checkListBooleanListB + 1) {
                    checkListBooleanListB++;
                    if (listBoolean.containsKey(str + (i - 1))) return listBoolean.get(str + (i - 1));

                }
                return listBoolean.get(str + i);
            }
        }
        return i + 1;
    }

    private static String classStr(String str) {
        if (str.indexOf("(") == -1) {
            return str;
        } else {
            return str.substring(0, str.indexOf("("));
        }
    }

    /**
     * 解析xpath中的参数属性
     * @param listXpathStr
     * @return
     * @throws StringIndexOutOfBoundsException
     * @throws IllegalArgumentException
     */
    private static String[] elementArr(String listXpathStr) throws StringIndexOutOfBoundsException,IllegalArgumentException {
        String[] element = new String[0];
        listXpathStr = listXpathStr.substring(listXpathStr.indexOf("("),listXpathStr.length());
        while (true) {
            if(!listXpathStr.matches("^\\(.+=.*;\\).*$")) {
               throw new IllegalArgumentException("参数不合法："+listXpathStr+"正确格式:^(.+=.*;).*$");
            }
            element = Arrays.copyOf(element, element.length + 1);
            String strInsertion = "";
            strInsertion = listXpathStr.substring(1,listXpathStr.indexOf(";)"));
            element[element.length - 1] = strInsertion.substring(0, strInsertion.indexOf("=") + 1) +
                    "\"" + strInsertion.substring(strInsertion.indexOf("=") + 1, strInsertion.length()) + "\"";
            listXpathStr = listXpathStr.substring(strInsertion.length()+3,listXpathStr.length());
            if (listXpathStr.indexOf("(") == -1) break;

        }
        return element;
    }

}
