package JavaStudy;

public class test {
    public static void main(String[] args) {
        int type =4;
        switch (type){
            default:
                System.out.println(5);
            case 1:
                System.out.println(1);
            case 2:
                System.out.println(2);
            case 3:
                System.out.println(3);

        }

        int i=0;
        int j=-1;
        switch(i){
            case 0 :j=1;
            case 1 :j=1;
            case 2 :j=3;

        }
        System.out.println("j="+j);

    }

}
