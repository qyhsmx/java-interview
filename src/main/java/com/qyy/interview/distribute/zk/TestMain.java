package com.qyy.interview.distribute.zk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qyhsmx@outlook.com
 * @date
 */
public class TestMain {

    private static Logger logger = LoggerFactory.getLogger(TestMain.class);
    private static TestUtil testUtil = new TestUtil();
    private static Concrete concrete = new Concrete();
    private static String code;
    public static void main(String[] args) {

        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                try {
                    concrete.lock();
                    code = testUtil.createCode();
                }catch (Exception e){
                    logger.error(e.toString());
                }finally {
                    concrete.unlock();
                }
                System.out.println(Thread.currentThread().getName()+" : "+code);
            }).start();
        }

    }
}
