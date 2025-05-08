package com.xixi.impl;

import com.xixi.HelloService;
import com.xixi.annotation.RpcService;

@RpcService(interfaceClass = HelloService.class, version = "3.0")
public class HelloServiceImpl3 implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello3.0 " + name;
    }
}
