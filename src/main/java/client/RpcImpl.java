package client;

import Util.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import serializer.JavaSerializer;
import Util.SerialCompress;
import serializer.Serializer;
import server.RemoteService;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class RpcImpl implements RpcProtocol{


    private Map<String, RpcBody> rpcMap = new HashMap<>();

    private Serializer serializer = new JavaSerializer();

    private CuratorFramework client;

    private Map<String, List<RemoteService>> serverMap = new HashMap<>();

    public List<RemoteService> getProvider(String name) throws Exception {
        String path = "/rpc/" + name + "/service";
        System.out.println(path);
        List<String> children = client.getChildren().forPath(path);
        final List<RemoteService> services = Optional.ofNullable(children).orElse(new ArrayList<>()).stream().map((child) -> {
            final RemoteService service = JSONObject.parseObject(child, RemoteService.class);
            return service;
        }).collect(Collectors.toList());
        serverMap.put(name, services);
        return services;
    }

    public void init() throws Exception {
        final Class<RpcImpl> rpcClass = RpcImpl.class;
        final Method[] methods = rpcClass.getMethods();
        for(Method method: methods){
            final Class<?>[] parameters = method.getParameterTypes();
            rpcMap.put(method.getName(), new RpcBody(method.getName(), parameters));
        }
        ZkClient.init("127.0.0.1:2181", "");
        client = ZkClient.get();
        String path = "/rpc/" + "remote" + "/service";
        client.getData().usingWatcher((CuratorWatcher) watchedEvent -> {
            serverMap.clear();
            final List<RemoteService> remote = getProvider("remote");
            System.out.println("remote node:" + remote);
        }).forPath(path);
        getProvider("remote");
        System.out.println("Rpc client init finish!");
    }


    public Map<String, RpcBody> getRpcMap() {
        return rpcMap;
    }

    public void writeToSocket(Socket socket, RpcBody rpcBody) throws IOException {
        final byte[] rpcBytes = SerialCompress.serialCompress(rpcBody);
        final OutputStream outputStream = socket.getOutputStream();
        outputStream.write(rpcBytes);
        outputStream.flush();
    }


    public Object getResponse(Socket socket) throws IOException, ClassNotFoundException {
        final byte[] bytes = IOUtils.fromInputStreamToBytes(socket.getInputStream());
        Object response = SerialCompress.deSerialUnCompress(bytes);
        System.out.println("get response " + response);
        return response;
    }

    public Socket getBalanceSocket(String name) throws IOException {
        final List<RemoteService> services = serverMap.get(name);
        int rand_index = (int)(Math.random() * services.size());
        final RemoteService service = services.get(rand_index);
        return new Socket(service.getIp(), service.getPort());
    }

}
