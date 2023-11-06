package compress;

import java.io.OutputStream;

public interface Compresser {

    byte[] compress(byte[] bytes);

    byte[] uncompress(byte[] bytes);

}
