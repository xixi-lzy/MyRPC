package com.xixi.configuration;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ManualConfiguration {
    private static final Map<String, Object> config;

    static {
        Yaml yaml = new Yaml();
        InputStream input = ManualConfiguration.class.getClassLoader()
                .getResourceAsStream("application.yaml");
        config = yaml.load(input);
    }

    public static int getMaxRetries() {
        return (int) ((Map<String, Object>) config.get("xixi")).getOrDefault("max-tries", 3);
    }

    public static String getLoadBalanceStrategy() {
        try {
            Map<String, Object> loadBalanceConfig = (Map<String, Object>) ((Map<?, ?>) config.get("xixi")).get("loadBalance");
            return (String) loadBalanceConfig.getOrDefault("strategy", "roundrobin");
        } catch (Exception e) {
            System.err.println("负载均衡配置读取失败，使用默认轮询策略");
            return "roundrobin";
        }
    }

    public static String getRedisHost() {
        try {
            Map<String, Object> redisConfig = (Map<String, Object>) ((Map<?, ?>) config.get("xixi")).get("redis");
            return (String) redisConfig.getOrDefault("host", "localhost");
        } catch (Exception e) {
            System.err.println("Redis 主机配置读取失败，使用默认值 localhost");
            return "localhost";
        }
    }

    public static int getRedisPort() {
        try {
            Map<String, Object> redisConfig = (Map<String, Object>) ((Map<?, ?>) config.get("xixi")).get("redis");
            return (int) redisConfig.getOrDefault("port", 6379);
        } catch (Exception e) {
            System.err.println("Redis 端口配置读取失败，使用默认值 6379");
            return 6379;
        }
    }

    public static String getUrlHost() {
        try {
            Map<String, Object> urlConfig = (Map<String, Object>) ((Map<?, ?>) config.get("xixi")).get("url");
            return (String) urlConfig.getOrDefault("hostname", "localhost");
        } catch (Exception e) {
            System.err.println("URL 主机配置读取失败，使用默认值 localhost");
            return "localhost";
        }
    }

    public static int getUrlPort() {
        try {
            Map<String, Object> urlConfig = (Map<String, Object>) ((Map<?, ?>) config.get("xixi")).get("url");
            return (int) urlConfig.getOrDefault("port", 8080);
        } catch (Exception e) {
            System.err.println("URL 端口配置读取失败，使用默认值 8080");
            return 8080;
        }
    }
}
