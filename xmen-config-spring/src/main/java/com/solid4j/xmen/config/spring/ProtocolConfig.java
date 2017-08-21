/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

/**
 * @author solidwang
 * @since 1.0
 */
public class ProtocolConfig extends AbstractConfig {

    // 服务协议
    private String name;
    // 服务端口
    private Integer port;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public Integer getPort() {

        return port;
    }

    public void setPort(Integer port) {

        this.port = port;
    }
}
