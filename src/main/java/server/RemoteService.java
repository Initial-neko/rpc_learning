package server;

import java.io.Serializable;

public class RemoteService implements Serializable {

    private String name;
    private String ip;
    private int port;
    private String serialProto;
    private String compressProto;

    public RemoteService(String name, String ip, int port, String serialProto, String compressProto) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.serialProto = serialProto;
        this.compressProto = compressProto;
    }

    public String getSerialProto() {
        return serialProto;
    }

    public void setSerialProto(String serialProto) {
        this.serialProto = serialProto;
    }

    public String getCompressProto() {
        return compressProto;
    }

    public void setCompressProto(String compressProto) {
        this.compressProto = compressProto;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "RemoteService{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", serialProto='" + serialProto + '\'' +
                ", compressProto='" + compressProto + '\'' +
                '}';
    }
}
