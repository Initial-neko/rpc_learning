package client;

import Util.JavaSerializer;
import Util.SerialCompress;
import Util.Serializer;
import org.openjdk.jol.util.IOUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class RpcImpl implements RpcProtocol{


    private Map<String, RpcBody> rpcMap = new HashMap<>();

    private Serializer serializer = new JavaSerializer();

    public String getProvider(){
        return "127.0.0.1";
    }

    public void init(){
        final Class<RpcImpl> rpcClass = RpcImpl.class;
        final Method[] methods = rpcClass.getMethods();
        for(Method method: methods){
            final Class<?>[] parameters = method.getParameterTypes();
            rpcMap.put(method.getName(), new RpcBody(method.getName(), parameters));
        }
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
        final byte[] bytes = IOUtils.readAllBytes(socket.getInputStream());
        System.out.println("input read!");
        Object response = SerialCompress.deSerialUnCompress(bytes);
        System.out.println("get response " + response);
        return response;
    }

    @Override
    public int add(int a, int b) {
        try {
            Socket socket = new Socket(getProvider(), 9090);
            final RpcBody add = rpcMap.get("add");
            add.setArguments(new Object[]{a, b});
            writeToSocket(socket, add);

            System.out.println("wait response");
            Object response = getResponse(socket);
            if(response instanceof Integer){
                return (Integer)response;
            }else{
                throw new InternalError("cannot get result from add method!");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int calculate(BigInteger big0, BigInteger big1) {
        try {
            Socket socket = new Socket(getProvider(), 9090);
            final RpcBody rpcBody = rpcMap.get("calculate");
            rpcBody.setArguments(new Object[]{big0, big1});
            writeToSocket(socket, rpcBody);

            Object response = getResponse(socket);
            if(response instanceof Integer){
                return (Integer)response;
            }else{
                throw new InternalError("cannot get result from add method!");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String drawDragon(int num) {
        try {
            Socket socket = new Socket(getProvider(), 9090);
            final RpcBody rpcBody = rpcMap.get("drawDragon");
            rpcBody.setArguments(new Object[]{num});
            writeToSocket(socket, rpcBody);

            Object response = getResponse(socket);
            if(response instanceof String){
                return (String) response;
            }else{
                throw new InternalError("cannot get result from rpcBody method!");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
