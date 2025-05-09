package com.xixi.register;

import com.xixi.common.Invocation;
import com.xixi.common.URL;
import com.xixi.configuration.ManualConfiguration;
import com.xixi.protocol.HttpServerHandler;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//todo 定时任务时间可配置
public class NodeCleaner {
    private static final long HEARTBEAT_TIMEOUT = 30000; // 30秒超时
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void startCleanupTask() {
        scheduler.scheduleAtFixedRate(() -> {
            try (Jedis jedis = new Jedis(ManualConfiguration.getRedisHost(), ManualConfiguration.getRedisPort())) {
                Set<String> services = jedis.keys("*_HEARTBEAT");
                for (String serviceKey : services) {
                    Map<String, String> nodes = jedis.hgetAll(serviceKey);
                    long currentTime = System.currentTimeMillis();

                    nodes.forEach((address, timestampStr) -> {
                        long timestamp = Long.parseLong(timestampStr);
                        if (currentTime - timestamp > HEARTBEAT_TIMEOUT) {
                            // 移除过期节点
                            MapRemoteRegister.remove(serviceKey.replace("_HEARTBEAT", ""), address);
                            jedis.hdel(serviceKey, address);
                        }
                    });
                }
            }
        }, 30, 30, TimeUnit.SECONDS); // 每30秒执行一次
    }

//    public static void startKeepAliveTask() {
//        ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
//        heartbeatExecutor.scheduleAtFixedRate(() -> {
//            try (Jedis jedis = new Jedis(ManualConfiguration.getRedisHost(), ManualConfiguration.getRedisPort())) {
//                URL currentUrl = new URL(ManualConfiguration.getUrlHost(), ManualConfiguration.getUrlPort());
//                String address = currentUrl.getHostname() + ":" + currentUrl.getPort();
//
//                // 获取所有已注册服务
//                Set<String> services = jedis.keys("*");
//                services.stream()
//                        .filter(service -> !service.endsWith("_HEARTBEAT"))
//                        .forEach(serviceName -> {
//                            Invocation heartbeat = new Invocation();
//                            heartbeat.setMethodName("heartbeat");
//                            heartbeat.setInterfaceName(serviceName);
//                            heartbeat.setParameters(new Object[]{address});
//
//                            try {
//                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                                new ObjectOutputStream(bos).writeObject(heartbeat);
//                                MockHttpRequest mockRequest = new MockHttpRequest(bos.toByteArray());
//                                new HttpServerHandler().handler(
//                                        mockRequest,
//                                        new MockHttpResponse()
//                                );
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        });
//            }
//        }, 0, 20, TimeUnit.SECONDS);
//    }
}
