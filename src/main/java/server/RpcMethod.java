package server;

import client.RpcProtocol;

import java.math.BigInteger;

public class RpcMethod implements RpcProtocol {

    @Override
    public int add(int a, int b){
        return a + b;
    }

    @Override
    public int calculate(BigInteger big0, BigInteger big1){
        return big0.add(big1).intValue();
    }

    @Override
    public String drawDragon(int num){
        String dragon = "  _____$\n" +
                " /      \\$\n" +
                "|        |$\n" +
                "|        |$\n" +
                " \\_____ /$\n" +
                "   | | |$\n" +
                "  / | | \\$\n" +
                " /  | |  \\$\n" +
                " |  | |  |$\n" +
                " |  | |  |$\n" +
                " |_| |_|_|";
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < num; i++){
            str.append(dragon + "\n");
        }
        return str.toString();
    }
}
