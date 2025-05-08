package com.xixi.register;

import com.xixi.common.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
