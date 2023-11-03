package client;

import java.io.Serializable;
import java.math.BigInteger;

public interface RpcProtocol extends Serializable {

    public int add(int a, int b);

    public int calculate(BigInteger big0, BigInteger big1);

    public String drawDragon(int num);
}
