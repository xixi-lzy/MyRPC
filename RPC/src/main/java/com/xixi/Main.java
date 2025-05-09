package com.xixi;

import com.xixi.common.URL;
import com.xixi.configuration.ManualConfiguration;
import com.xixi.processor.ServiceAnnotationProcessor;
import com.xixi.register.NodeCleaner;

public class Main {
    public static void main(String[] args) {
        //启动心跳检测和清除任务
        NodeCleaner.startCleanupTask();
        //NodeCleaner.startKeepAliveTask();

    }
}
