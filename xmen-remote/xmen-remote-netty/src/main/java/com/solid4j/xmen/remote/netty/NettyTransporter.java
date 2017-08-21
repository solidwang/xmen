/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.remote.netty;

import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.remote.Server;
import com.solid4j.xmen.remote.Transporter;

import java.util.Map;
/**
 * @author solidwang
 * @since 1.0
 */
public class NettyTransporter implements Transporter {

    @Override
    public Server bind(URL url, Map<String, Object> map) {
        return new NettyServer(url, map);
    }
}
