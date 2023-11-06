package Util;

public class SerialCompress {

    private static Serializer serializer = new JavaSerializer();
    private static Compresser compresser = new GzipCompresser();

    public static byte[] serialCompress(Object obj){
        final byte[] serialize = serializer.serialize(obj);
        return compresser.compress(serialize);
    }

    public static Object deSerialUnCompress(byte[] bytes){
        final byte[] uncompress = compresser.uncompress(bytes);
        return serializer.deserialize(uncompress);
    }

}
