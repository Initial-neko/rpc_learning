package Util;

import org.openjdk.jol.info.ClassLayout;

import java.io.*;
import java.util.Objects;

public class JavaSerializer implements Serializer{


    @Override
    public OutputStream serialize(OutputStream stream, Object obj) throws IOException {
        final ObjectOutputStream outputStream = new ObjectOutputStream(stream);
        outputStream.writeObject(obj);
        return outputStream;
    }

    @Override
    public Object deserialize(InputStream inputStream) {
        try(ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            Object obj = ois.readObject();
            return obj;
        }catch (IOException | ClassNotFoundException ioe){
            ioe.printStackTrace();
        }
        throw new RuntimeException("cannot deserialize object!");
    }
    
    public static void main(String[] args) {
        String abc = "123123123";
        final JavaSerializer javaSerializer = new JavaSerializer();
    }
}
