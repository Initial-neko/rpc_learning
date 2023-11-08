package Util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ZkClient {


    private static CuratorFramework zkClient;
    private static Logger logger = LoggerFactory.getLogger(ZkClient.class);


    /**
     * 初始化zookeeper客户端
     *
     * @param zkConnect
     * @param namespace
     */
    public synchronized static void init(String zkConnect, String namespace) {

        if (zkClient != null) {
            return;
        }
        try {
            zkClient = CuratorFrameworkFactory
                    .builder()
                    .connectString(zkConnect)
                    .namespace(namespace)
                    .retryPolicy(
                            new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
                    .connectionTimeoutMs(30000).sessionTimeoutMs(30000).build();

            System.out.println("zk client 初始化完毕");
            zkClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                close();
            }
        });
    }

    /**
     * Getter method for property <tt>zkClient</tt>.
     *
     * @return property value of zkClient
     */
    public static CuratorFramework get() {
        return zkClient;
    }

    /**
     * 关闭zookeeper连接
     */
    public static synchronized void close() {

        if (zkClient != null) {
            zkClient.close();
            zkClient = null;
        }
    }

    public static void checkExistsAndCreate(CuratorFramework zkClient, String path, CreateMode mode) throws Exception {
        final Stat stat = zkClient.checkExists().forPath(path);
        if(stat == null){
            zkClient.create().creatingParentsIfNeeded().withMode(mode).forPath(path);
        }

    }



}

