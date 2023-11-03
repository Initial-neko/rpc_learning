package Util;

import org.openjdk.jol.info.ClassLayout;

import java.io.*;
import java.util.Objects;

public class JavaSerializer implements Serializer{


    @Override
    public byte[] serialize(Object obj) {
        final ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bao)) {
            oos.writeObject(obj);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        return bao.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try(ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream)) {
            Object obj = ois.readObject();
            return obj;
        }catch (IOException | ClassNotFoundException ioe){
            ioe.printStackTrace();
        }
        throw new RuntimeException("cannot deserialize object!");
    }
    
    public static void main(String[] args) {

    }
}
