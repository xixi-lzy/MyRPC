package com.xixi.protocol;

import com.xixi.common.Invocation;
import com.xixi.register.LocalRegister;

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
}
