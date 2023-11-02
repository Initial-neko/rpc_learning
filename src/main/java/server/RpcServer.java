package server;

import Util.JavaSerializer;
import Util.Serializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class RpcServer {

    Logger logger = Logger.getLogger("RPC SERVER");

    //这里启动一个服务器,通过服务器来接收请求，并且找到合适的RPC方法进行调用，返回结果
    public static void main(String[] args) throws IOException {
        new RpcServer().run();
    }

    public void run() throws IOException {
        Serializer serializer = new JavaSerializer();
        try(ServerSocket listener = new ServerSocket(9090)){
            while (true){
                try(Socket socket = listener.accept()) {
                    final Object obj = serializer.deserialize(socket.getInputStream());
                    logger.info("request is " + obj);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
