/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.registry.zookeeper;

import com.solid4j.xmen.common.constant.XmenConstant;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.common.util.CollectionUtil;
import com.solid4j.xmen.registry.AbstractRegistry;

import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: solidwang
 * @since 1.0
 */
public class ZookeeperRegistry extends AbstractRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private final ZkClient zkClient;

    public ZookeeperRegistry() {
        // 创建 ZooKeeper 客户端
        zkClient = new ZkClient("127.0.0.1:2181", 5000, 10000);
        LOGGER.info("connect zookeeper");
    }

    @Override
    public void registry(String serviceName, URL url) {
        LOGGER.debug("registry service start...");
        // 创建 registry 节点（持久）
        String registryPath = XmenConstant.ZK_REGISTRY_PATH;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            LOGGER.debug("create registry node: {}", registryPath);
        }
        // 创建 service 节点（持久）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            LOGGER.debug("create service node: {}", servicePath);
        }
        // 创建 address 节点（临时）
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, url.toString());
        LOGGER.debug("create address node: {}", addressNode);
    }

    @Override
    public String discovery(String serviceName) {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000, 10000);;
        // 创建 ZooKeeper 客户端
        LOGGER.debug("connect zookeeper");
        try {
            // 获取 service 节点
            String servicePath = XmenConstant.ZK_REGISTRY_PATH + "/" + serviceName;
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath));
            }
            List<String> addressList = zkClient.getChildren(servicePath);
            if (CollectionUtil.isEmpty(addressList)) {
                throw new RuntimeException(String.format("can not find any address node on path: %s", servicePath));
            }
            // 获取 address 节点
            String address;
            int size = addressList.size();
            if (size == 1) {
                // 若只有一个地址，则获取该地址
                address = addressList.get(0);
                LOGGER.debug("get only address node: {}", address);
            } else {
                // 若存在多个地址，则随机获取一个地址
                address = addressList.get(ThreadLocalRandom.current().nextInt(size));
                LOGGER.debug("get random address node: {}", address);
            }
            // 获取 address 节点的值
            String addressPath = servicePath + "/" + address;
            return zkClient.readData(addressPath);
        } finally {
            zkClient.close();
        }
    }
}
