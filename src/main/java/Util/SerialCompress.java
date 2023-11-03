package Util;

public class SerialCompress {

    private static Serializer serializer = new JavaSerializer();
    private static Compresser compresser = new GzipCompresser();

    public static byte[] serialCompress(Object obj){
        final byte[] serialize = serializer.serialize(obj);
        final byte[] compress = compresser.compress(serialize);
        return compress;
    }

    public static Object deSerialUnCompress(byte[] bytes){
        final byte[] uncompress = compresser.uncompress(bytes);
        final Object rawObj = serializer.deserialize(uncompress);
        return rawObj;
    }

}
