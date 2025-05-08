package com.xixi;


import com.xixi.common.URL;
import com.xixi.configuration.ManualConfiguration;
import com.xixi.impl.HelloServiceImpl;
import com.xixi.impl.HelloServiceImpl2;
import com.xixi.processor.ServiceAnnotationProcessor;
import com.xixi.protocol.HttpServer;
import com.xixi.register.LocalRegister;
import com.xixi.register.RedisRegister;

import java.util.Map;

public class Provider {

    public static void main(String[] args) {

        URL url = new URL(
                ManualConfiguration.getUrlHost(),
                ManualConfiguration.getUrlPort()
        );

        //扫描并自动注册
        ServiceAnnotationProcessor.process("com.xixi.impl", url);

        // 启动服务器
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(), url.getPort());
    }
}
