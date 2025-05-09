package com.xixi.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Invocation implements Serializable {

    private String interfaceName;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] parameters;
    private String version="1.0";

    public Invocation(String interfaceName, String methodName, Class[] parameterTypes, Object[] parameters) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public Invocation(String interfaceName, String methodName, Class[] parameterTypes, Object[] parameters,String version) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
        this.version = version;
    }

    public Invocation() {
    }
}
