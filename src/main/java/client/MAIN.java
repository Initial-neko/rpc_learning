package client;

import java.lang.reflect.Proxy;
import java.math.BigInteger;

/**
 * RpcClient中调用以下的方法
 * 1. add
 * 2. calculate 一个很大的数
 * 3. 编写一个递归算法，通过递归的方式来写
 * 4. 能否将编写RPC method的方法抽象出来，最好只留下一个方法体
 */

//这里启动一个客户端，通过客户端来进行RPC调用
public class MAIN {

    public static void main(String[] args) throws Exception {
        //RPC client需要对请求进行包装，让服务端能够接受请求情况
        final RpcImpl rpcClient = new RpcImpl();
        rpcClient.init();
        RpcHandler handler = new RpcHandler(rpcClient);
        RpcProtocol proxy = (RpcProtocol) Proxy.newProxyInstance(RpcImpl.class.getClassLoader(), new Class[]{RpcProtocol.class}, handler);
        System.out.println(proxy.add(2, 3));
        System.out.println(proxy.calculate(BigInteger.valueOf(2434322), BigInteger.valueOf(2324242)));
        System.out.println(proxy.drawDragon(1));
    }
}
