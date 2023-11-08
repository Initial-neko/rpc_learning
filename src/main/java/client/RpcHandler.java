package client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

public class RpcHandler implements InvocationHandler {


    private RpcImpl client;

    public RpcHandler(RpcImpl client){
        this.client = client;
    }


    //@Override
    //    public int add(int a, int b) {
    //        try {
    //            Socket socket = getBalanceSocket("remote");
    //            final RpcBody add = rpcMap.get("add");
    //            add.setArguments(new Object[]{a, b});
    //            writeToSocket(socket, add);
    //
    //            System.out.println("wait response");
    //            Object response = getResponse(socket);
    //            if(response instanceof Integer){
    //                return (Integer)response;
    //            }else{
    //                throw new InternalError("cannot get result from add method!");
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return 0;
    //    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try{
            Socket socket = client.getBalanceSocket("remote");
            final RpcBody add = client.getRpcMap().get(method.getName());
            System.out.println(method.getName());
            add.setArguments(args);
            client.writeToSocket(socket, add);

            System.out.println("wait response");
            Object response = client.getResponse(socket);

            System.out.println(method.getReturnType());
            System.out.println(method.getReturnType().isInstance(response));
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
