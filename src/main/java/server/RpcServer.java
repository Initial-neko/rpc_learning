package server;

import Util.*;
import client.RpcBody;
import com.alibaba.fastjson.JSONObject;
import compress.CompresserEnum;
import org.apache.curator.framework.CuratorFramework;
import Util.SerialCompress;
import org.apache.zookeeper.CreateMode;
import serializer.SerializerEnum;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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

    private static int port = 9090;

    private CuratorFramework client;

    public void init(){
        final Class<RpcMethod> rpcClass = RpcMethod.class;
        final Method[] methods = rpcClass.getMethods();
        for(Method method: methods){
            rpcMap.put(method.getName(), method);
        }
        ZkClient.init("127.0.0.1:2181", "");
        client = ZkClient.get();
        System.out.println("init success! rpcMap:" + rpcMap);
    }

    //name = service + ipAddress + port
    public void bindRemoteService(RemoteService service) throws Exception {
        String serviceJson = JSONObject.toJSONString(service);
        String zkPath = "/rpc/" + service.getName() + "/service";
        String uri = zkPath + "/" + serviceJson;
        ZkClient.checkExistsAndCreate(client, zkPath, CreateMode.PERSISTENT);
        ZkClient.checkExistsAndCreate(client, uri, CreateMode.EPHEMERAL);
    }

    //这里启动一个服务器,通过服务器来接收请求，并且找到合适的RPC方法进行调用，返回结果
    public static void main(String[] args) throws Exception {
        if(args.length != 0){
            port = Integer.parseInt(args[0]);
        }
        new RpcServer().run();
    }

    public String getLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
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

    public void run() throws Exception {
        init();
        bindRemoteService(new RemoteService(
                "remote",
                getLocalIp(), port,
                SerializerEnum.JAVA.name(), CompresserEnum.GZIP.name()
        ));
        try(ServerSocket listener = new ServerSocket(port)){
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
