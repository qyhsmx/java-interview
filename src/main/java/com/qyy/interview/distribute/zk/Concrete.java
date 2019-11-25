package com.qyy.interview.distribute.zk;

import org.I0Itec.zkclient.IZkDataListener;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * @author qyhsmx@outlook.com
 * @date
 */
public class Concrete extends BaseTemplate {

    @Override
    protected void waitForLock() {
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) throws Exception {

            }

            @Override
            public void handleDataDeleted(String s) throws Exception {
                Optional.ofNullable(countDownLatch).ifPresent(CountDownLatch::countDown);
            }
        };
        client.subscribeDataChanges("/text",listener);

        if (client.exists("/text")){
            countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        client.unsubscribeDataChanges("/text",listener);

    }

    @Override
    protected boolean tryAcquire() {
        try {
            client.createEphemeral("/text");
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
