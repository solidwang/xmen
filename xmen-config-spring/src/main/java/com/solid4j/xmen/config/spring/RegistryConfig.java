/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.config.spring;

/**
 * @author solidwang
 * @since 1.0
 */
public class RegistryConfig extends AbstractConfig {

    // 注册中心名称
    private String name;
    // 注册协议
    private String regProtocol;
    // 注册中心地址
    private String address;
    // 链接注册中心超时时间
    private Integer connectTimeout;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getRegProtocol() {

        return regProtocol;
    }

    public void setRegProtocol(String regProtocol) {

        this.regProtocol = regProtocol;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public Integer getConnectTimeout() {

        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {

        this.connectTimeout = connectTimeout;
    }
}
