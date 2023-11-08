package client;

import java.io.Serializable;
import java.math.BigInteger;

public interface RpcProtocol extends Serializable {

    default public Integer add(int a, int b) {
        return null;
    }

    default Integer calculate(BigInteger big0, BigInteger big1){
        return null;
    }

    default public String drawDragon(int num) {
        return null;
    }
}
