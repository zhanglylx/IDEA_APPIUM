package zhibohuodong;

import java.util.Random;

public class Nonce {
    public static String NONCE = "AB1CD2EF3GH4IJ5KL6MN7OP8QR9ST0UV_WX_YZ";
    public static String nonce(){
//        String s = "";
//        Random random = new Random();
//        String str = "";
//        int [] i = new int[8];
//        i[0] = 100000;
//        wh:while(true) {
//            int pick = random.nextInt(s.length());
//            for(int n : i){
//                if(n==pick)continue wh;
//            }
//           str+=s.charAt(pick);
//            i[str.length()-1]=pick;
//           if(str.length()==8)break;
//        }
        return   getNonce();
    }
    private static String getNonce() {
        StringBuilder builder = new StringBuilder();
        while (builder.length() < 8) {
            Random mRandom = new Random();
            builder.append(NONCE.charAt(mRandom.nextInt(NONCE.length())));
        }
        return builder.toString();
    }
}
