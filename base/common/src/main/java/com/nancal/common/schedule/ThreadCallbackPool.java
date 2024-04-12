package com.nancal.common.schedule;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadCallbackPool {

    //20个排队
    private static ExecutorService es = new ThreadPoolExecutor(5, 20,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024));

    public static  void exec(Runnable command,String keyword) {
        try {
            es.execute(command);
            log.info(keyword);
        } catch (Exception e) {
            log.error("执行动作：【{}】 异常： {}",keyword,e.getMessage());
        }
    }

}
