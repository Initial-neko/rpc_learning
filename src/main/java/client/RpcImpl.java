package client;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

public class RpcImpl implements RpcProtocol{

    public String getProvider(){
        return "127.0.0.1";
    }

    @Override
    public int add(int a, int b) {
        try {
            Socket socket = new Socket(getProvider(), 9090);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
