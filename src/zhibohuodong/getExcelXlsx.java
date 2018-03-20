package zhibohuodong;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class getExcelXlsx {
    public static ArrayList<String[]> list = new ArrayList<String[]>();
    public static boolean[] nullXls ;
    public static boolean getNullXls(int i){
        if(i>=nullXls.length)return true;
        return nullXls[i];
    }
    /**
     * @author ZhangLianYu 2017/07/27
     * @since 以下方法为获取到指定Excel中的第一行所有列和最后一行所有列的数据 第一行列的数据赋值给Key 第二行列的数据赋值给Values
     * 代码中获取第一行数据不能为空
     */
    public static ArrayList<String[]> getExcelXlsx() {
        // jxl.Workbook readColumnwb = null;
        try {
            InputStream is = new FileInputStream("E:\\zhibo\\user.xlsx");
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
            // 获取每一个工作薄
            // for (int numSheet = 0; numSheet <
            // xssfWorkbook.getNumberOfSheets(); numSheet++) {
            // XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            // if (xssfSheet == null) {
            // continue;
            // }
            // 获取当前工作薄的每一行
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
            int Column = xssfSheet.getRow(0).getPhysicalNumberOfCells();// 获取所有的列
            int row = xssfSheet.getLastRowNum(); // 获取所有的行
            nullXls = new boolean[row];
            // 暂时插入第一行key和最后一行values
            f:for (int rowNum = 0; rowNum < Column; rowNum++) {
                String[] values = new String[0];
                boolean b = false;
                // 指定访问第行的内容
                for (int rw = 0; rw < row; rw++) {
                    XSSFRow xssfColumn = xssfSheet.getRow(rw);
                    XSSFCell keyRow = xssfColumn.getCell(rowNum);
                    if(rw==0 &&( (keyRow).toString().equals("user_id")
                            ||(keyRow).toString().equals("user_name")
                            ||(keyRow).toString().equals("token")
                    )) b = true;
                    if(!b)continue f;
                    keyRow.setCellType(org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING);// 将小数转换成整数
                    values = Arrays.copyOf(values, values.length + 1);
                    try {
                        values[rw] = (keyRow).toString();
                        nullXls[rw] = false;
                        if("".equals(values[rw])) nullXls[rw] = true;
                    } catch (NullPointerException e) {
                        values[rw] = "";
                      nullXls[rw] = true;

                    }
                }
                list.add(values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } // finally {
        // readwb.close();
        // }
        return list;
    }
}
