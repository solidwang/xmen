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
    protected List<ProtocolConfig> protocols;

    // 注册中心的配置列表
    protected List<RegistryConfig> registries;

    public List<RegistryConfig> getRegistries() {

        return registries;
    }

    public void setRegistries(List<RegistryConfig> registries) {

        this.registries = registries;
    }
    
    public void setRegistry(RegistryConfig registry) {
        this.registries = Collections.singletonList(registry);
    }

    public List<ProtocolConfig> getProtocols() {

        return protocols;
    }

    public void setProtocols(List<ProtocolConfig> protocols) {

        this.protocols = protocols;
    }
}
