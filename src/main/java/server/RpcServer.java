package server;

import Util.IOUtils;
import Util.JavaSerializer;
import Util.SerialCompress;
import Util.Serializer;
import client.RpcBody;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
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
        System.out.println("init success! rpcMap:" + rpcMap);
    }

    //这里启动一个服务器,通过服务器来接收请求，并且找到合适的RPC方法进行调用，返回结果
    public static void main(String[] args) throws IOException {

        new RpcServer().run();
    }

    public void handleRpcCall(Socket socket, RpcBody readObject) throws IOException, IllegalAccessException, InvocationTargetException {
        RpcBody rpcBody = readObject;
        Method invokeMethod = null;
        switch (rpcBody.getMethodName()){
            case "add":
                invokeMethod = rpcMap.get("add");
                break;
            case "calculate":
                invokeMethod = rpcMap.get("calculate");
                break;
            case "drawDragon":
                invokeMethod = rpcMap.get("drawDragon");
                break;
            default: throw new IllegalAccessException("cannot process method:" + readObject.getMethodName());
        }
        final Object invoke = invokeMethod.invoke(new RpcMethod(), rpcBody.getArguments());
        System.out.println(invoke);
        final byte[] invokeBytes = SerialCompress.serialCompress(invoke);
        final OutputStream outputStream = socket.getOutputStream();
        outputStream.write(invokeBytes);
        outputStream.flush();
        logger.info("result is " + invoke);
    }

    public void run() throws IOException {
        init();
        try(ServerSocket listener = new ServerSocket(9090)){
            while (true){
                try(Socket socket = listener.accept()) {
                    socket.setSoTimeout(3000);
                    byte[] bytes = IOUtils.fromInputStreamToBytes(socket.getInputStream());
                    System.out.println(Arrays.toString(bytes));

//                    final byte[] bytes = IOUtils.fromInputStreamToBytes(inputStream);
                    final Object readObject = SerialCompress.deSerialUnCompress(bytes);

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
