package com.xixi.common;

import lombok.Data;

@Data
public class URL {

    private String hostname;
    private Integer port;

    public URL(String hostname, Integer port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String toString() {
        return hostname + ":" + port;
    }
}
