package client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class RpcHandler implements InvocationHandler {


    private RpcImpl client;

    public RpcHandler(RpcImpl client){
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try{
            Socket socket = client.getBalanceSocket("remote");
            final RpcBody add = client.getRpcMap().get(method.getName());
            add.setArguments(args);
            client.writeToSocket(socket, add);

            System.out.println("wait response");
            Object response = client.getResponse(socket);

            if(method.getReturnType().isInstance(response)){
                return response;
            }else{
                throw new InternalError("cannot get result from add method!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
