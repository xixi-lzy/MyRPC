package com.xixi.register;

import com.xixi.common.URL;
import com.xixi.configuration.ManualConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public class RedisRegister {

    private static final Jedis jedis = new Jedis(
            ManualConfiguration.getRedisHost(),
            ManualConfiguration.getRedisPort()
    );

    public static void regist(String interfaceName, URL url) {
        if (url.getPort() <= 0) {
            //todo 全局异常处理
            System.err.println("无效的端口号" + url.getPort());
            //throw new IllegalArgumentException("Invalid port number");
        }
        jedis.hset(interfaceName, url.getHostname()+":"+url.getPort(), url.toString());
    }

    public static List<URL> get(String interfaceName) {
        List<URL> list = new ArrayList<>();
        jedis.hgetAll(interfaceName).forEach((k, v) -> {
            // 增加格式校验（格式为hostname:port）
            if (v.split(":").length < 2) {
                System.err.println("Invalid URL format: " + v);
                return;
            }
            String[] parts = v.split(":");
            list.add(new URL(parts[0], Integer.parseInt(parts[1])));
        });
        return list;
    }
}
