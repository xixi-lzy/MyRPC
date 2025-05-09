package com.xixi.register;

import com.xixi.common.URL;

import java.util.*;

public class MapRemoteRegister {

    private static final Map<String, List<URL>> map= new HashMap<>();//Key,Value(接口名字，实现类)

    public static void regist(String interfaceName,URL url){
        List<URL> list = map.get(interfaceName);
        if(list==null){
            list = new ArrayList<>();
        }
        list.add(url);
        map.put(interfaceName, list);
    }

    public static List<URL> get(String interfaceName){
        return map.get(interfaceName);
    }

    public static void remove(String interfaceName, String address) {
        List<URL> urls = map.get(interfaceName);
        if (urls == null) return;

        // 使用迭代器安全删除
        Iterator<URL> iterator = urls.iterator();
        while (iterator.hasNext()) {
            URL url = iterator.next();
            // 假设URL的toString()格式为 "hostname:port"
            if ((url.getHostname() + ":" + url.getPort()).equals(address)) {
                iterator.remove();
            }
        }

        // 更新注册表
        if (urls.isEmpty()) {
            map.remove(interfaceName);
        } else {
            map.put(interfaceName, urls);
        }
    }
}
