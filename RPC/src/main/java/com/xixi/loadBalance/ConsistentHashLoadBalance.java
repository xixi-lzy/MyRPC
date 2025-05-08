package com.xixi.loadBalance;

import com.xixi.common.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashLoadBalance {

    private final TreeMap<Long, URL> virtualNodes = new TreeMap<>();
    private static final int VIRTUAL_NODES = 160; // 每个物理节点对应虚拟节点数

    public ConsistentHashLoadBalance(List<URL> urls) {
        for (URL url : urls) {
            addNode(url);
        }
    }

    private void addNode(URL node) {
        for (int i = 0; i < VIRTUAL_NODES; i++) {
            String virtualNodeName = node.toString() + "#" + i;
            long hash = hash(virtualNodeName);
            virtualNodes.put(hash, node);
        }
    }

    public URL select(String hashKey) {
        if (virtualNodes.isEmpty()) return null;

        long hash = hash(hashKey);
        SortedMap<Long, URL> tailMap = virtualNodes.tailMap(hash);
        if (tailMap.isEmpty()) {
            return virtualNodes.firstEntry().getValue();
        }
        return tailMap.get(tailMap.firstKey());
    }

    private long hash(String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(key.getBytes());
            return bytesToLong(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    private long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xFF);
        }
        return result;
    }
}