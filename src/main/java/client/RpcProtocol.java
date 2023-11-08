package client;

import java.io.Serializable;
import java.math.BigInteger;

public interface RpcProtocol extends Serializable {

    public Integer add(int a, int b);

    public Integer calculate(BigInteger big0, BigInteger big1);

    public String drawDragon(int num);
}
