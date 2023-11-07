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
        String regex = "\"(\\w+)\":\"([^\"]+)\"";
        Pattern pattern = Pattern.compile("\"(\\w+)\":\"([^\"]+)\"|\"(\\w+)\":\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(json);

        StringBuilder result = new StringBuilder();
        String indent = " ";
        while (matcher.find()) {
            if (matcher.group(2) != null) {
                // Key-Value Pair
                String key = matcher.group(1);
                String value = matcher.group(2);
                result.append(indent + "\"" + key + "\": \"" + value + "\"\n");
            } else if (matcher.group(4) != null) {
                // Nested JSON Object
                String key = matcher.group(3);
                String nestedJson = matcher.group(4);
                result.append(indent + "\"" + key + "\": {\n");
                result.append(nestedJson);
                result.append(indent + "}\n");
            }
        }

        System.out.println(result);
    }
}
