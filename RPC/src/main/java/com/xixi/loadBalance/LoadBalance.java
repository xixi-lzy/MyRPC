package com.xixi.loadBalance;

import com.xixi.common.Invocation;
import com.xixi.common.URL;
import com.xixi.configuration.ManualConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalance {

    // 原子计数器保证线程安全
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static URL roundRobin(List<URL> urls){
        // 轮询算法
        if (urls == null || urls.isEmpty()) {
            return null;
        }
        int index = counter.getAndIncrement() % urls.size();
        // 处理整型溢出
        if (counter.get() == Integer.MAX_VALUE) {
            counter.set(0);
        }
        return urls.get(index);
    }

    public static URL consistentHash(List<URL> urls, Invocation invocation) {
        String hashKey = generateHashKey(invocation); // 封装生成逻辑
        return new ConsistentHashLoadBalance(urls).select(hashKey);
    }

    public static URL getURL(List<URL> urls, Invocation invocation){
        String strategy = ManualConfiguration.getLoadBalanceStrategy();
        if ("consistentHash".equals(strategy)) {
            return consistentHash(urls, invocation);
        }
        else return roundRobin(urls);
    }

    //生成hashKey的方法
    private static String generateHashKey(Invocation invocation) {
        String hashKey = invocation.getInterfaceName()
                + ":" + invocation.getMethodName()
                + ":" + Arrays.toString(invocation.getParameters());
        return hashKey;
    }
}
