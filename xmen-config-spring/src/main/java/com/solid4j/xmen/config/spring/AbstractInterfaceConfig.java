/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

import java.util.Collections;
import java.util.List;

/**
 * @author solidwang
 * @since 1.0
 */
public class AbstractInterfaceConfig extends AbstractConfig {

    // 服务暴漏的协议
    protected ProtocolConfig protocol;

    // 注册中心的配置列表
    protected RegistryConfig registry;

    public ProtocolConfig getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolConfig protocol) {
        this.protocol = protocol;
    }

    public RegistryConfig getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }
}
