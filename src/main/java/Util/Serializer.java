package Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {

    OutputStream serialize(OutputStream stream, Object obj) throws IOException;

    Object deserialize(InputStream inputStream);
}
