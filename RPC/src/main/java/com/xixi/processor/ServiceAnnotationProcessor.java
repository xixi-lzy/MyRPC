package com.xixi.processor;

import com.xixi.annotation.RpcService;
import com.xixi.common.URL;
import com.xixi.register.LocalRegister;
import com.xixi.register.RedisRegister;
import org.reflections.Reflections;

import java.util.Set;

public class ServiceAnnotationProcessor {

    public static void process(String basePackage, URL providerUrl) {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(RpcService.class);

        for (Class<?> serviceClass : services) {
            RpcService annotation = serviceClass.getAnnotation(RpcService.class);
            // 注册本地服务
            LocalRegister.regist(
                    annotation.interfaceClass().getName(),
                    annotation.version(),
                    serviceClass
            );
            // 注册到注册中心
            RedisRegister.regist(
                    annotation.interfaceClass().getName(),
                    providerUrl
            );
        }
    }
}
