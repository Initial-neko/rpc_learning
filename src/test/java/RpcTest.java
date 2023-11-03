import client.RpcBody;
import client.RpcImpl;
import org.junit.Test;
import server.RpcMethod;
import server.RpcServer;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

public class RpcTest {


    @Test
    public void testReflect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final RpcServer rpcServer = new RpcServer();
        rpcServer.init();
        final RpcImpl rpc = new RpcImpl();
        rpc.init();
        final Map<String, Method> rpcMap = rpcServer.getRpcMap();
        final Map<String, RpcBody> map = rpc.getRpcMap();
        System.out.println("Method Map:" + rpcMap);
        System.out.println("rpcBody map:" + map);
        RpcBody rpcBody = map.get("add");
        rpcBody.setArguments(new Object[]{2,3});
        final Method add = rpcMap.get("add");
//        add.setAccessible(true);
        System.out.println(Arrays.toString(rpcBody.getArguments()));
        final Object invoke = add.invoke(new RpcMethod(), rpcBody.getArguments());
        System.out.println(invoke);

    }

}
