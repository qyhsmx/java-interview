package com.qyy.interview.distribute.zk;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * @author qyhsmx@outlook.com
 * @date
 */
public abstract class BaseTemplate implements ZKLock {
    Logger logger = LoggerFactory.getLogger(BaseTemplate.class);

    private static String SERVER = "127.0.0.1:2181";

    ZkClient client = new ZkClient(SERVER,3000);


    CountDownLatch countDownLatch;

    protected abstract void waitForLock();

    protected abstract boolean tryAcquire();


    @Override
    public void lock() {
        if(tryAcquire()){
            logger.info("获取锁");
        }else {
            //等待
            waitForLock();
            //继续获取锁
            lock();
        }
    }

    @Override
    public void unlock() {
        Optional.ofNullable(client).ifPresent(zkClient -> zkClient.delete("/text"));
        //Optional.ofNullable(client).ifPresent(ZkClient::close);
        logger.info("释放锁");
    }


}
