package compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * object => serialize => compress => socket-outStream
 *                                                  ||
 *                                                  V
 * object <= deserialize <= decompress <= socket-inputStream
 *
 *
 */
public class GzipCompresser implements Compresser {


    @Override
    public byte[] compress(byte[] bytes) {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        try(GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteOutputStream)){
            gzipOutputStream.write(bytes);
        }catch (IOException e){
            e.printStackTrace();
            return new byte[0];
        }
        final byte[] new_bytes = byteOutputStream.toByteArray();
//        System.out.println("compress bytes from " + bytes.length + " to " + new_bytes.length);
        return new_bytes;
    }

    @Override
    public byte[] uncompress(byte[] bytes) {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try(
            ByteArrayInputStream bai = new ByteArrayInputStream(bytes);
            GZIPInputStream gzipInputStream = new GZIPInputStream(bai)){

            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = gzipInputStream.read(buffer)) != -1){
                bao.write(buffer, 0, bytesRead);
            }

        }catch (IOException ioe){
            ioe.printStackTrace();
            return new byte[0];
        }
        final byte[] raw_bytes = bao.toByteArray();

//        System.out.println("uncompress bytes from " + bytes.length + " to " + raw_bytes.length);
        return raw_bytes;
    }

    public static void main(String[] args) {
        String context = "neko is a very good cat";
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < 1; i++){
            str.append(context);
        }
        byte[] bytes = str.toString().getBytes(StandardCharsets.UTF_8);
        final GzipCompresser compresser = new GzipCompresser();
        byte[] bytes_new = compresser.uncompress(compresser.compress(bytes));
        System.out.println(Arrays.equals(bytes, bytes_new));

    }
}
