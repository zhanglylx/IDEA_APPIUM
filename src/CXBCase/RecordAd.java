package CXBCase;

import java.util.HashMap;
import java.util.Map;

public class RecordAd {
    private Map<String,Boolean> ad;
    private Map<String,String> adText;
    private static RecordAd rad;
    private RecordAd(){
          this.ad = new HashMap<>();
          this.adText = new HashMap<>();
    }
    public static RecordAd getRecordAd(){
        if(rad==null){
            rad = RecordAd.getRecordAd();
        }
        return rad;
    }
    public void setAd(String ad){
        this.ad.put(ad,true);
    }
    public void setAd(String ad,String text){
        this.adText.put(ad,text);
        this.ad.put(ad,true);
    }
}
