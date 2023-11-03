package Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes);
}
