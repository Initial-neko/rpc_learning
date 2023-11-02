package Util;

public interface Compresser {

    byte[] compress(byte[] bytes);

    byte[] uncompress(byte[] bytes);

}
