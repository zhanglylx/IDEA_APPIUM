package CXBCase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecordAd {
    private Map<String,Boolean> ad;
    private Map<String,ArrayList<String>> adText;
    private static RecordAd rad;
    private RecordAd(){
          this.ad = new HashMap<>();
          this.adText = new HashMap<>();
    }
    public static RecordAd getRecordAd(){
        if(rad==null){
            rad =new RecordAd();
        }
        return rad;
    }
    public void setAd(String ad){
        this.ad.put(ad,true);
    }
    public void setAd(String ad,String text){
        ArrayList<String> list = this.adText.get(ad);
        if(list==null)list = new ArrayList<>();
        list.add(text);
        this.adText.put(ad,list);
        this.ad.put(ad,true);
    }
    public void printAd(){
        for(Map.Entry<String,Boolean> en :ad.entrySet()){
            System.out.print(en.getKey()+":"+en.getValue());
            if(adText.containsKey(en.getKey())) {
                System.out.println(":" + adText.get(en.getKey()).toString());
            }else{
                System.out.println();
            }
        }
    }
}
