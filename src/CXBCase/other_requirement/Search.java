package CXBCase.other_requirement;

import AppTest.AppXmlUtil;
import CXBCase.CXBRunCase;
import CXBCase.CaseFrame;
import ZLYUtils.ExcelUtils;
import ZLYUtils.Uiautomator;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;
import org.openqa.selenium.By;

import java.io.*;
import java.util.*;

public class Search extends CaseFrame {
    //搜索词
    private List<String> searchTerms;
    private Map<Integer, Map<String, String>> resultMap;
    private int index;
    private File excelFile;
    private String[] bounds;

    public Search(String caseName) throws IOException {
        super(caseName);
        this.searchTerms = getLocalSearchTerms();
        this.resultMap = new LinkedHashMap<>();
        this.index = 0;
        this.excelFile = new File("mfdzs-online.xlsx");
        this.bounds = new String[]{"[0,168][1080,721]", "[0,721][1080,1090]"
                , "[0,1090][1080,1459]", "[0,1459][1080,1828]"};
    }

    @Override
    public boolean caseMap() throws FileNotFoundException {
        devices.sleep(5000);
        CXBRunCase.initialize(devices);
        //点击搜索
        devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view_2"));
        for (String keywords : this.searchTerms) {
            searchKeywords(keywords);
        }
        return true;
    }

    /**
     * 执行搜索词
     */
    private void searchKeywords(String keywords) throws FileNotFoundException {
        devices.inputCharacter(By.className("android.widget.EditText"), keywords);
        devices.clickfindElement(By.id("com.mianfeia.book:id/title_right_view"));
        devices.sleep(3000);
        devices.snapshot(keywords);
        Map<String, String> map = new LinkedHashMap<>();
        String xml=devices.getPageXml();
        for (int i = 0; i < this.bounds.length; i++) {
            map = getSearchResult(map, i, this.bounds[i], keywords,xml);
        }
        devices.clickfindElement(By.className("android.widget.ImageButton"));
        if (this.index != 0) this.resultMap = ExcelUtils.getExcelXlsx(this.excelFile);
        this.resultMap.put(this.index, map);
        this.index++;
        ExcelUtils.createExcelFile(this.excelFile, "online",
                this.resultMap);

    }

    /**
     * 获取搜索结果
     *
     * @param i
     */
    private Map<String, String> getSearchResult(
            Map<String, String> map, int i, String bounds, String keywords,
            String xml) {
        map.put("keywords", keywords);
        map.put("bookName:" + (i + 1), AppXmlUtil.getXMLElement(
                getXpath(i, bounds,
                        "com.mianfeia.book:id/search_result_title_view")
                , xml, "text"
        ));
        map.put("introduction" + (i + 1), AppXmlUtil.getXMLElement(
                getXpath(i, bounds
                        , "com.mianfeia.book:id/search_result_summary_view")
                , xml, "text"
        ));
        map.put("author" + (i + 1), AppXmlUtil.getXMLElement(
                getXpath(i, bounds,
                        "com.mianfeia.book:id/search_result_author_view")
                , xml, "text"
        ));
        map.put("type" + (i + 1), AppXmlUtil.getXMLElement(
                getXpath(i, bounds,
                        "com.mianfeia.book:id/search_result_type_view")
                , xml, "text"
        ));
        return map;
    }

    private String getXpath(int i, String bounds, String resourceId) {
        return "android.support.v7.widget.RecyclerView//" +
                "android.widget.LinearLayout(index=" + i + ";)(bounds=" + bounds + ";)//" +
                "android.widget.TextView(" +
                "resource-id=" + resourceId + ";)";
    }

    /**
     * 获取本地搜索词
     */
    private List<String> getLocalSearchTerms() throws IOException {
        List<String> list = new ArrayList<>();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new FileReader("Search.txt")
            );
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                list.add(str);
            }
        } finally {
            if (bufferedReader != null) bufferedReader.close();
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        new Search("420搜索").startCase();
    }
}
