package com.xixi.Proxy;

import com.xixi.common.Invocation;
import com.xixi.common.URL;
import com.xixi.configuration.ManualConfiguration;
import com.xixi.loadBalance.LoadBalance;
import com.xixi.protocol.HttpClient;
import com.xixi.register.MapRemoteRegister;
import com.xixi.register.RedisRegister;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

//private final MyConfiguration config;

@Component
public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass){

        Object proxyInstance=Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[]{interfaceClass},new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //模拟调用本地服务
                String mock = System.getProperty("mock");
                if(mock!=null && mock.startsWith("return:")){
                    String result=mock.replace("return:","");
                    return result;
                }

                //封装调用信息
                Invocation invocation=new Invocation(interfaceClass.getName(),method.getName(),method.getParameterTypes(),args);
                HttpClient httpClient = new HttpClient();
                //服务发现  先从本地缓存找，找不到再从注册中心找
                List<URL> list;
                if(MapRemoteRegister.get(interfaceClass.getName())!=null){
                    list=MapRemoteRegister.get(interfaceClass.getName());
                }
                else{
                    list = RedisRegister.get(interfaceClass.getName());
                }

                //服务调用
                String result=null;
                //重试次数可以配置
                int maxRetries = ManualConfiguration.getMaxRetries();
                int tryCount=0;
                while(tryCount++<maxRetries){
                    try {
                        //负载均衡
                        URL url = LoadBalance.getURL(list,invocation);
                        result=httpClient.send(url.getHostname(), url.getPort(), invocation);
                        if(result!=null){
                            return result;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("重试次数过多，服务调用失败");
                return null;
            }
        });

        return (T) proxyInstance;
    }
}
