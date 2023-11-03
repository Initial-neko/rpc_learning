import Util.GzipCompresser;
import Util.JavaSerializer;
import Util.SerialCompress;
import Util.Serializer;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SerialCompressTest {

    @Test
    public void compress(){
        String abc = "123123sadasds__~+~+~+";
        final byte[] bytes = abc.getBytes(StandardCharsets.UTF_8);
        final GzipCompresser compresser = new GzipCompresser();
        Arrays.equals(compresser.uncompress(compresser.compress(bytes)), bytes);
    }

    @Test
    public void serial(){
        String neko = "12312312";
        Serializer serializer = new JavaSerializer();
        System.out.println(serializer.deserialize(serializer.serialize(neko)).equals(neko));
    }

    @Test
    public void serial_compress(){
        String abc = "123123n12k3jk12j3120das0-d0asd";
        final byte[] bytes = abc.getBytes(StandardCharsets.UTF_8);
        System.out.println(SerialCompress.deSerialUnCompress(SerialCompress.serialCompress(abc)).equals(abc));
    }

}
