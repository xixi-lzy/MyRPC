package com.xixi.register;

import java.util.HashMap;
import java.util.Map;

public class LocalRegister {

    private static final Map<String,Class> map= new HashMap<>();//Key,Value(接口名字，实现类)

    public static void regist(String interfaceName,String version,Class implClass){
        map.put(interfaceName+version,implClass);
    }

    public static Class get(String interfaceName,String version){
        return map.get(interfaceName+version);
    }

    public static Class get(String interfaceName) {
        return get(interfaceName, "1.0"); // 默认版本号 1.0
    }
}
