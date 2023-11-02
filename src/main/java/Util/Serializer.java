package Util;

import java.io.InputStream;

public interface Serializer {

    byte[] serialize(Object obj);

    Object deserialize(InputStream inputStream);
}
