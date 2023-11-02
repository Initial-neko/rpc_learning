package Util;

import org.openjdk.jol.info.ClassLayout;

import java.io.*;
import java.util.Objects;

public class JavaSerializer implements Serializer{


    @Override
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try(ObjectOutputStream oos = new ObjectOutputStream(bao)){
            oos.writeObject(obj);
        }catch (IOException ioe){
            ioe.printStackTrace();
            return new byte[0];
        }
        return bao.toByteArray();
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

    static class testObject implements Serializable{
        int a;
        int b;
        String c;

        public testObject(int a, int b, String c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            testObject that = (testObject) o;
            return a == that.a && b == that.b && Objects.equals(c, that.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b, c);
        }

        @Override
        public String toString() {
            return "testObject{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c='" + c + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        final testObject testObject = new testObject(1, 2, "123123 new testObject()");
        final Serializer serializer = new JavaSerializer();
        final byte[] bytes = serializer.serialize(testObject);
        final ClassLayout classLayout = ClassLayout.parseInstance(testObject);
        System.out.println(classLayout.toPrintable());
        System.out.println(bytes.length);
        final testObject deserialize = (testObject)serializer.deserialize(new ByteArrayInputStream(bytes));
        System.out.println(deserialize.equals(testObject));
    }
}
