package client;

import java.math.BigInteger;

public interface RpcProtocol {

    public int add(int a, int b);

    public int calculate(BigInteger big0, BigInteger big1);

    public String drawDragon(int num);
}
