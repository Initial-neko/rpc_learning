package client;

import server.RpcMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * RpcClient中调用以下的方法
 * 1. add
 * 2. calculate 一个很大的数
 * 3. 编写一个递归算法，通过递归的方式来写
 * 4. 能否将编写RPC method的方法抽象出来，最好只留下一个方法体
 */

//这里启动一个客户端，通过客户端来进行RPC调用
public class RpcClient {

    public static void main(String[] args) {
        //RPC client需要对请求进行包装，让服务端能够接受请求情况
        final RpcImpl rpcClient = new RpcImpl();
        rpcClient.init();
        System.out.println(rpcClient.add(2, 3));
    }
}
