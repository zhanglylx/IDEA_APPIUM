package CXBCase;

public class PrintErr {
    String caseName;
    public  PrintErr(String caseName){
      this.caseName = caseName;
    }
    public void print(String err){
        System.out.println(caseName+"["+err+"]：失败");
    }
}
