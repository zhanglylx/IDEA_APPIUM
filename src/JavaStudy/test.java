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


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean dateB = true;
                long date = 0;
                while (true) {
//                        if (dateB) {
//                            date = System.currentTimeMillis() + (5 * 1000);
//                            dateB = false;
//                        }
//                        if (System.currentTimeMillis() >= date) {
//                            time += 5;
//                            if (stop==1)break;
//                            System.out.println(time + " 秒");
//                            dateB = true;
//                        }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException   e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                        return;
                    }
                        System.out.println(  " 秒");
                    }
                }
        });
        t.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException   e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        t.interrupt();
    }

}
