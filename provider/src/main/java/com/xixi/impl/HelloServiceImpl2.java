package com.xixi.impl;

import com.xixi.HelloService;
import com.xixi.annotation.RpcService;

@RpcService(interfaceClass = HelloService.class, version = "2.0")
public class HelloServiceImpl2 implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello2.0 " + name;
    }
}
