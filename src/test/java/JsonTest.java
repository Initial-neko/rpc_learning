import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonTest {

    //{
    // "remoteService":
    // {"port":9098,"ip":"12.12.12.12","name":"neko","serialProto":"serial","compressProto":"compress"},
    // "port":90999,"name":"testService"
    // }
    @Test
    public void jsonStr(){
        String json = "{\"remoteService\":{\"port\":9098,\"ip\":\"12.12.12.12\",\"name\":\"neko\",\"serialProto\":\"serial\",\"compressProto\":\"compress\"},\"port\":90999,\"name\":\"testService\"}";
        json = json.replaceAll("\"", "");
        System.out.println(json);

    }
}
