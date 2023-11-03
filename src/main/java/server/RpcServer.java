package server;

import Util.JavaSerializer;
import Util.Serializer;
import client.RpcBody;
import client.RpcImpl;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RpcServer {

    Logger logger = Logger.getLogger("RPC SERVER");

    private Map<String, Method> rpcMap = new HashMap<>();

    public Map<String, Method> getRpcMap(){
        return rpcMap;
    }

    public void init(){
        final Class<RpcMethod> rpcClass = RpcMethod.class;
        final Method[] methods = rpcClass.getMethods();
        for(Method method: methods){
            rpcMap.put(method.getName(), method);
        }
    }

    //这里启动一个服务器,通过服务器来接收请求，并且找到合适的RPC方法进行调用，返回结果
    public static void main(String[] args) throws IOException {

        new RpcServer().run();
    }

    public void handleRpcCall(Socket socket, RpcBody readObject) throws IOException, IllegalAccessException, InvocationTargetException {
        RpcBody rpcBody = readObject;
        if(rpcBody.getMethodName().equals("add")){
            final Method add = rpcMap.get("add");
            final Object invoke = add.invoke(new RpcMethod(), rpcBody.getArguments());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(invoke);
            logger.info("result is " + invoke);
        }
    }

    public void run() throws IOException {
        init();
        Serializer serializer = new JavaSerializer();
        try(ServerSocket listener = new ServerSocket(9090)){
            while (true){
                try(Socket socket = listener.accept()) {

                    final ObjectInputStream stream = new ObjectInputStream(socket.getInputStream());
                    final Object readObject = stream.readObject();
                    logger.info("request is " + readObject);

                    if(readObject instanceof RpcBody){
                        handleRpcCall(socket, (RpcBody) readObject);
                    }else {
                        throw new RuntimeException("cannot identify this class:" + readObject.getClass());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
