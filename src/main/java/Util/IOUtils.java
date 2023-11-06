package Util;

import java.io.*;
import java.util.Arrays;

public class IOUtils {

    public static byte[] fromInputStreamToBytes(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream bao = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bao.write(buffer, 0, bytesRead);
                if(bytesRead != buffer.length){
                    break;
                }
            }
            byte[] bytes = bao.toByteArray();
            return bytes;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static void main(String[] args) throws IOException {
    }
}
