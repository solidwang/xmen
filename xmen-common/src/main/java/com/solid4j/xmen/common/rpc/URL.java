/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.rpc;

import com.solid4j.xmen.common.constant.XmenConstant;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author solidwang
 * @since 1.0
 */
public class URL implements Serializable {

    // 协议
    private String protocol;
    // 主机
    private String host;
    // 端口
    private Integer port;

    private Map<String, String> parameters = new HashMap<String, String>();

    public URL(){}

    public URL(String protocol, String host, Integer port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return protocol + XmenConstant.PROTOCOL_SEPARATOR + host + ":" + port;
    }

    public String getProtocol() {

        return protocol;
    }

    public void setProtocol(String protocol) {

        this.protocol = protocol;
    }

    public String getHost() {

        return host;
    }

    public void setHost(String host) {

        this.host = host;
    }

    public Integer getPort() {

        return port;
    }

    public void setPort(Integer port) {

        this.port = port;
    }

    /**
     * 字符串转URL
     * @param url
     * @return
     */
    public static URL valueOf(String url) {
        //字符串demo: xmen://10.2.130.156:20000
        String protocol = null;
        String host = null;
        Integer port = 0;
        String path = null;
        if(url.indexOf(XmenConstant.PROTOCOL_SEPARATOR) == -1)
            return null;
        String[] protocolArr = url.split(XmenConstant.PROTOCOL_SEPARATOR);
        protocol = protocolArr[0];
        String hostAndPort = protocolArr[1];
        host = hostAndPort.split(":")[0];
        port = Integer.valueOf(hostAndPort.split(":")[1]);
        return new URL(protocol, host, port);
    }

    public String getParameter(String key) {
        String value = parameters.get(key);
        if (value == null || value.length() == 0) {
            value = parameters.get("default." + key);
        }
        return value;
    }

    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) { return defaultValue; }
        return value;
    }

}
