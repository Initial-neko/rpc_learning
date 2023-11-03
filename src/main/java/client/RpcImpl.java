package client;

import Util.JavaSerializer;
import Util.Serializer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    @Override
    public int add(int a, int b) {
        try {
            Socket socket = new Socket(getProvider(), 9090);
            final RpcBody add = rpcMap.get("add");
            add.setArguments(new Object[]{a, b});
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(add);

            final ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object response = objectInputStream.readObject();
            System.out.println("get response " + response);
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
        return 0;
    }

    @Override
    public String drawDragon(int num) {
        return null;
    }
}
