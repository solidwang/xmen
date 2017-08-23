/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.rpc;

import com.solid4j.xmen.common.constant.XmenConstant;
import org.apache.commons.collections4.MapUtils;

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

    public URL() {
    }

    public URL(String protocol, String host, Integer port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public URL(String protocol, String host, Integer port, Map<String, String> parameters) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(protocol)
                .append(XmenConstant.PROTOCOL_SEPARATOR)
                .append(host);
        if (port != 0) {
            stringBuilder.append(":").append(port);
        }
        if (MapUtils.isNotEmpty(parameters)) {
            stringBuilder.append("?");
            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                stringBuilder.append(parameter.getKey()).append("=").append(parameter.getValue()).append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&"));
        }
        return stringBuilder.toString();
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
     *
     * @param url
     * @return
     */
    public static URL valueOf(String url) {
        //字符串demo: xmen://0.0.0.0:20000?regProtocol=zookeeper&address=0.0.0.0:2181&connectTimeout=2000
        String protocol = null;
        String host = null;
        Integer port = 0;
        String path = null;
        if (url.indexOf(XmenConstant.PROTOCOL_SEPARATOR) == -1)
            return null;
        url = url.substring(0, url.indexOf(XmenConstant.PARAMATER_SEPARATOR));
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
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

}
