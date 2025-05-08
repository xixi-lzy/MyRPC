package com.xixi;

import com.xixi.Proxy.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Consumer {


    public static void main(String[] args) {
        HelloService helloService= ProxyFactory.getProxy(HelloService.class);
        String result=helloService.sayHello("xixi123");
        System.out.println(result);
    }
}
