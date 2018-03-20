package AppTest;

import AppiumMethod.Tooltip;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;

import javax.swing.*;
import java.util.ArrayList;

/**
 *运行case
 * 通过筛选case中的事件执行
 * @author 张连宇
 */

public class RunTest {
    /**
     * 记录log
     * @param s
     */
    public static void addList(String s) {
        System.out.println(s);
        Log.logList.add(s);
        allAddList(s);
    }

    /**
     * 当时想通过重载方法给日志打出加入格式
     * 最后放弃，没有将方法删除
     * @param s
     * @param i
     */
    public static void addList(String s, int i) {
        System.out.println(s);
        Log.logList.add(s);
        allAddList(s);
    }
    /**
     * 记录全部log
     * @param s
     */
    public static void allAddList(String s) {
        Log.alllogList.add(s);
    }

    /**
     * 将case存在全部log中
     * @param s
     */
    public static void allAddList(ArrayList<String> s) {
        Log.alllogList.add("listSize：" + s.size());
        for (String str : s) {
            Log.alllogList.add(str);
        }
        Log.alllogList.add(StringUtils.repeat("-", 150));
    }
    public static void main(String[] args) {
        GetRunApp.RunApp();
        int n=0;
        while(true) {
            ArrayList<String> conutList =  new ArrayList<String>();
            GetCase testCase = new GetCase();
            /**
             * 执行用例
             */
            if (testCase.getRunATestCaseList().size() > 0) {
                addList("执行单个测试用例");
                allAddList(testCase.getRunATestCaseList());
                for(int i=n;i<testCase.getRunATestCaseList().size();i++){
                    conutList.add(testCase.getRunATestCaseList().get(i));
                }
                runTestCase(conutList, testCase.getFistNum());
                n=testCase.getRunATestCaseList().size();
            } else {
                addList("执行全部测试用例");
                allAddList(testCase.getCaseList());
                for(int i=n;i<testCase.getCaseList().size();i++){
                    conutList.add(testCase.getCaseList().get(i));
                }
                runTestCase(conutList, testCase.getFistNum());
                n=testCase.getCaseList().size();
            }
            if( GetRunApp.numDIY==0) {
                if (JOptionPane.showConfirmDialog(null, "是否继续执行自定义模式", "自定义？", JOptionPane.YES_NO_OPTION) == 1) {
                    break;
                }
            }else{
                break;
            }

        }
    }
    /**
     * 根据list执行用例
     *
     * @param list
     */
    private static void runTestCase(ArrayList<String> list, ArrayList<Integer> num) {
        Devices devices = Devices.getDevices("默认");
        //用于判断当前这个case是否存在检测不通过，如果存在检测不通过，跳过这次用例
        boolean cont = true;
        String succeed = "张连宇";
        boolean sn = true;

        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            String e = listSub(s);
            int n = 1;
            addList("");
            if (i < num.size()) {
                if (cont) addList("第:" + num.get(i) + "获取到的行数据：" + s + "; 解析出的value：" + e + ":");
            } else {
                if (cont) addList("获取到的行数据：" + s + "; 解析出的value：" + e + ":");
            }
            if ((s.trim().startsWith("snapshot")) && sn) {
                addList("执行: snapshot", 1);
                devices.snapshot(e);
            } else if (s.trim().startsWith("caseName") || s.trim().startsWith("runATestCaseName")) {
                devices = Devices.getDevices(e);
                addList("执行: caseName" + e + StringUtils.repeat("-", 120));
                if (!"张连宇".equals(succeed)) Log.succeedLogList.add("第" + n + "个case" + e + "执行:" + cont);
                succeed = e;
                sn = true;
                cont = true;
            } else if (!cont) {
                addList(devices.getCaseName() + "执行:continue");
                sn = false;
                continue;
            } else if (s.trim().startsWith("click")) {
                if (!click(devices, s)) cont = false;
            } else if (s.trim().startsWith("exists")) {
                if (!exists(devices, s)) cont = false;
            } else if (s.trim().startsWith("equals")) {
                addList("执行: equals", 1);
                if (!equalsCase(devices, s)) {
                    addList("  判断元素:" + e + "不通过", 1);
                    cont = false;
                } else {
                    addList("  判断元素:" + e + "通过", 1);
                }
            } else if (s.trim().startsWith("contains")) {
                addList("执行: contains", 1);
                if (!containsCase(devices, s)) {
                    addList("  判断元素:" + e + "不通过", 1);
                    cont = false;
                } else {
                    addList("  判断元素:" + e + "通过", 1);
                }
            } else if (s.trim().startsWith("wait")) {
                addList("执行: wait", 1);
                threadSleep(Long.parseLong(e));
            } else if (s.trim().equals("restart")) {
                addList("执行: restart", 1);
                devices.startApp();
            } else if (s.trim().startsWith("backspace")) {
                addList("执行: backspace", 1);
                devices.backspace();
            } else if (s.trim().startsWith("input")) {
                addList("执行: input ", 1);
                if (!inputCharacter(devices, s)) cont = false;
            } else if (s.trim().startsWith("slide")) {
                addList("执行: slide ", 1);
                slide(devices, s);
//                slideCoord(devices, s);
            } else if (s.trim().startsWith("coordinatesClick")) {
                addList("执行: coordinatesClick ", 1);
                coordinatesClick(devices, s);
            } else {
                addList("没有找到需要执行的操作:" + s, 1);
            }
            if( GetRunApp.numDeBug==0){
                if(JOptionPane.showConfirmDialog(null,
                        "继续？", "调试",JOptionPane.YES_NO_OPTION)==1)break;
            }
            Log.saveLog();
        }
        if (!"张连宇".equals(succeed)) Log.succeedLogList.add("最后一个case执行:" + cont);
        addList("所有测试执行完毕");
        Log.saveLog();
    }

    /**
     * 通过坐标点击
     */
    private static void coordinatesClick(Devices devices, String s) {
        String e = s.substring(s.indexOf("=") + 1, s.length());
        String[] n = e.split(",");
        n[0] = n[0].trim();
        n[1] = n[1].trim();
        int x = 0;
        int y = 0;
        try {
            x = Integer.parseInt(n[0]);
            y = Integer.parseInt(n[1]);
        } catch (Exception ss) {
            addList("参数不合法：" + e);
        }
        addList("x:" + x + " ; y:" + y);
        devices.clickScreen(x, y, 100);
    }

    /**
     * 滑动
     */
    private static void slide(Devices devices, String s) {
        int n = 0;
        try {
            n = Integer.parseInt(listSub(s));
        } catch (Exception e) {
            addList("参数不合法：" + n);
        }
        if (s.trim().startsWith("slideDown")) {
            addList("执行slideDown：" + n);
            devices.swipeToDown(n);
        } else if (s.trim().startsWith("slideUp")) {
            addList("执行slideUp：" + n);
            devices.swipeToUp(n);
        } else if (s.trim().startsWith("slideLift")) {
            addList("执行slideLift：" + n);
            devices.swipeToLeft2(n);
        } else if (s.trim().startsWith("slideRight")) {
            addList("执行slideRight：" + n);
            devices.swipeToRight(n);
        } else {
            addList("没有找到需要执行的操作：" + s);
        }


    }

    /**
     * 坐标滑动
     * @param devices
     * @param s
     */
    private static void slideCoord(Devices devices, String s) {
        int startX;
        int startY;
        int endX;
        int endY;
        int time;
        try {
           s=s.trim();
           String[] coord =s.split(":");
           if(coord.length!=5) throw new IllegalArgumentException();
           startX = Integer.parseInt(coord[0]);
           startY = Integer.parseInt(coord[1]);
           endX = Integer.parseInt(coord[2]);
           endY = Integer.parseInt(coord[3]);
           time = Integer.parseInt(coord[4]);
        } catch (Exception e) {
            addList("参数不合法：" + s);
        }
//        devices.swipe(startX,startY,endX,endY,time);


    }

    /**
     * 输入字符
     */
    private static boolean inputCharacter(Devices devices, String s) {
        boolean cont = true;
        String e = listSub(s);
        String[] str = e.split(";");
        if (s.trim().startsWith("inputId")) {
            addList("执行: inputId", 1);
            if (!devices.inputCharacter(By.id(str[0].trim()), str[1].trim())) {
                addList("  元素未找到:" + e, 1);
                cont = false;
            }
        } else if (s.trim().startsWith("inputClass")) {
            addList("执行: inputClass", 1);
            if (!devices.inputCharacter(By.className(str[0].trim()), str[1].trim())) {
                addList("  元素未找到:" + e, 1);
                cont = false;
            }
        } else if (s.trim().startsWith("inputXpath")) {
            addList("执行: inputXpath", 1);
            if (!devices.inputCharacter(By.className(str[0].trim()), str[1].trim())) {
                addList("  元素未找到:" + e, 1);
                cont = false;
            }
        } else {
            addList("一个exists事件都没有命中", 1);
            cont = false;
        }
        return cont;

    }


    /**
     * 判断是否存在
     */
    private static boolean exists(Devices devices, String s) {
        boolean cont = true;
        String e = listSub(s);
        if (s.trim().startsWith("existsId")) {
            addList("执行: existsId", 1);
            if (!devices.isElementExsitAndroid(By.id(e))) {
                addList("  判断元素:" + e + "不通过", 1);
                cont = false;
            }
        } else if (s.trim().startsWith("existsClass")) {
            addList("执行: existsClass", 1);
            if (!devices.isElementExsitAndroid(By.className(e))) {
                addList("  判断元素:" + e + "不通过", 1);
                cont = false;
            }
        } else if (s.trim().startsWith("existsXpath")) {
            addList("执行: existsXpath", 1);
            if (!devices.isElementExsitAndroid(By.xpath(e))) {
                addList("  判断元素:" + e + "不通过", 1);
                cont = false;
            }
        } else {
            addList("一个exists事件都没有命中", 1);
            cont = false;
        }
        return cont;
    }

    /**
     * 判断点击
     */
    private static boolean click(Devices de, String s) {
        String e = listSub(s);
        boolean cont = true;
        boolean b;
        if (s.trim().startsWith("clickId")) {
            addList("执行: clickId", 1);
            if (!(b = de.clickfindElement(By.id(e)))) {
                cont = false;
                addList(" clickId状态:" + b, 1);
            }
        } else if (s.trim().startsWith("clickClass")) {
            addList("执行: clickClass", 1);
            if (!(b = de.clickfindElement(By.className(e)))) {
                cont = false;
                addList(" clickClass状态:" + b, 1);
            }
        } else if (s.trim().startsWith("clickXpath")) {
            addList("执行: clickXpath", 1);
            if (!(b = de.clickfindElement(By.xpath(e)))) {
                cont = false;
                addList("clickXpath状态:" + b, 1);
            }
        } else if (s.trim().startsWith("clickExistsId")) {
            addList("执行: clickExistsId", 1);
            if (!(b = de.clickfindElement(By.id(e)))) {
                addList("clickExistsId状态:" + b, 1);
            }
        } else if (s.trim().startsWith("clickExistsClass")) {
            addList("执行: clickExistsClass", 1);
            if (!(b = de.clickfindElement(By.className(e)))) {
                addList("clickExistsClass状态:" + b, 1);
            }
        } else if (s.trim().startsWith("clickExistsXpath")) {
            addList("执行: clickExistsXpath", 1);
            if (!(b = de.clickfindElement(By.xpath(e)))) {
                addList("clickExistsXpath状态:" + b, 1);
            }
        } else {
            addList("没有找到一条click命令" + s, 1);
            cont = false;
        }
        return cont;
    }

    /**
     * 用于判断用例中的元素文本是否正确
     *
     * @param de
     * @param s
     * @return
     */
    private static boolean equalsCase(Devices de, String s) {
        String[] arr = listSubArray(s);
        String test = null;
        if (s.trim().startsWith("equalsId")) {
            test = de.getText(By.id(arr[0]));
            addList("执行: equalsId 获取到手机中的数据：" + test + "; 配置文件中的数据：" + arr[1], 1);
            if (!arr[1].equals(test)) {
                return false;
            }

        } else if (s.trim().startsWith("equalsClass")) {
            test = de.getText(By.className(arr[0]));
            addList("执行: equalsClass 获取到手机中的数据：" + test + "; 配置文件中的数据：" + arr[1], 1);
            if (!arr[1].equals(test)) {
                return false;
            }
        } else if (s.trim().startsWith("equalsXpath")) {
            test = de.getText(By.xpath(arr[0]));
            addList("执行: equalsXpath 获取到手机中的数据：" + test + "; 配置文件中的数据：" + arr[1], 1);
            if (!arr[1].equals(test)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /**
     * 用于判断用例中文本是否包含在元素中的文本
     *
     * @param de
     * @param s
     * @return
     */
    private static boolean containsCase(Devices de, String s) {
        String[] arr = listSubArray(s);
        String test = null;
        if (s.trim().startsWith("containsId")) {
            test = de.getText(By.id(arr[0]));
            addList("执行: containsId 获取到手机中的数据：" + test + "; 配置文件中的数据：" + arr[1], 1);
            if (!test.contains(arr[1])) {
                return false;
            }
        } else if (s.trim().startsWith("containsClass")) {
            test = de.getText(By.className(arr[0]));
            addList("执行: containsClass 获取到手机中的数据：" + test + "; 配置文件中的数据：" + arr[1], 1);
            if (!test.contains(arr[1])) {
                return false;
            }
        } else if (s.trim().startsWith("containsXpath")) {
            test = de.getText(By.xpath(arr[0]));
            addList("执行: containsXpath 获取到手机中的数据：" + test + "; 配置文件中的数据：" + arr[1], 1);
            if (!test.contains(arr[1])) {
                return false;
            }
        } else {

            return false;
        }
        return true;
    }

    /**
     * 分割字符串，格式xxx = wwwww
     *
     * @param list
     * @return 返回wwwww
     */
    public static String listSub(String list) {
        return list.substring(list.indexOf("=") + 1, list.length()).trim();
    }

    /**
     * 分割字符串，格式 XXX = WWWWW;LLLLL
     *
     * @param list
     * @return 返回数组，0index为WWWW,1index为LLLLL
     */
    public static String[] listSubArray(String list) {
        String[] li = new String[2];
        li[0] = list.substring(list.indexOf("=") + 1, list.indexOf(";")).trim();
        li[1] = list.substring(list.indexOf(";") + 1, list.length()).trim();
        return li;
    }

    /**
     * 系统暂停
     */
    public static void threadSleep(Long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Tooltip.errHint("系统暂时停出现问题");
        }
    }
}


/**
 * 等待主页展示
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 * <p>
 * 返回到书架
 */
//    private void waitHomePage() {
//        System.out.println("等待主页展示.....");
//        while (true) {
//            if (devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/tab_shelf_view"))) {
//                devices.snapshot("主页展现");
//                break;
//            }else if (devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/img_ad_detail"))
//                    || devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/tab_comp_view"))
//                    ){
//                devices.snapshot("当前未在主页");
//                backBookRack();
//                break;
//
//            }
//        }
//        try {
//            Thread.sleep(1500);
//            System.out.println("主页展示");
//            System.out.println();
//        } catch (InterruptedException e) {
//            System.out.println("等待主页展示时发生异常");
//            e.printStackTrace();
//        }
//
//
//    }
/**
 * 返回到书架
 */
//    private void backBookRack(){
//        while(true){
//            devices.backspace();
//            if(devices.isElementExsitAndroid(By.id("com.mianfeia.book:id/message_two_tip"))){
//                devices.backspace();
//                break;
//            }
//        }
//    }
//}
