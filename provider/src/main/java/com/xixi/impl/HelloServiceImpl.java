package com.xixi.impl;

import com.xixi.HelloService;
import com.xixi.annotation.RpcService;

@RpcService(interfaceClass = HelloService.class, version = "1.0")
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
