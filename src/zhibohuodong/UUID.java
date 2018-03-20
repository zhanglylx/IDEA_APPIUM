package zhibohuodong;
import java.util.UUID;
class uuid {
    public static String createUUID(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}
