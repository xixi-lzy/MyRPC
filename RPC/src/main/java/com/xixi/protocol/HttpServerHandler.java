package com.xixi.protocol;

import com.xixi.common.Invocation;
import com.xixi.configuration.ManualConfiguration;
import com.xixi.register.LocalRegister;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class HttpServerHandler {

    public void handler(HttpServletRequest req, HttpServletResponse resp){
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            if ("heartbeat".equals(invocation.getMethodName())) {
                handleHeartbeat(invocation, resp);
                return;
            }
            String interfaceName = invocation.getInterfaceName();
            //todo 实现从invocation中获取版本号（目前默认是1.0）
            String version = invocation.getVersion();
            Class implClass=LocalRegister.get(interfaceName,version);
            Method method=implClass.getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            String result= (String) method.invoke(implClass.newInstance(),invocation.getParameters());

            //IOUtils.write(result,resp.getOutputStream());
            byte[] bytes = result.getBytes(StandardCharsets.UTF_8);
            resp.getOutputStream().write(bytes);
            resp.getOutputStream().flush();
            resp.getOutputStream().close();

        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void handleHeartbeat(Invocation invocation, HttpServletResponse resp) {
        String serviceName = invocation.getInterfaceName();
        String address = invocation.getParameters()[0].toString();

        try (Jedis jedis = new Jedis(ManualConfiguration.getRedisHost(), ManualConfiguration.getRedisPort())) {
            // 更新节点最后心跳时间
            jedis.hset(serviceName + "_HEARTBEAT", address, String.valueOf(System.currentTimeMillis()));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
