import org.junit.Before;
import org.junit.Test;
import org.openjdk.jol.util.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SocketTest {


    @Test
    public void socketReadTest() throws InterruptedException {

        new Thread(() -> {
            try {
                System.out.println("server start");
                ServerSocket serverSocket = new ServerSocket(8080);
                System.out.println("server start");
                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream();

                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                byte[] recvBuf = new byte[1024];
                int recvLen;
                while((recvLen = in.read(recvBuf)) != -1){
                    System.out.println(recvLen);
                    System.out.println(new String(recvBuf, 0, recvLen));
                    bao.write(recvBuf);
                }

                OutputStream out = socket.getOutputStream();
                out.write(bao.toByteArray());

                socket.close();
                serverSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(1000);
        try {
            Socket socket = new Socket("localhost", 8080);

            System.out.println("start send data");
            int count = 500;
            StringBuilder str = new StringBuilder();
            for(int i = 0; i < count; i++){
                str.append("hello ");
            }
            str.append(" 1000");
            byte[] data = str.toString().getBytes();
            OutputStream out = socket.getOutputStream();
            out.write(data);

            InputStream in = socket.getInputStream();
            byte[] recvBuf = new byte[1024];
            int recvLen = in.read(recvBuf);

            System.out.println(new String(recvBuf, 0, recvLen));

            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
