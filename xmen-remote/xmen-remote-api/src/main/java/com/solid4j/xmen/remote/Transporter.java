/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.remote;

import com.solid4j.xmen.common.extension.Adaptive;
import com.solid4j.xmen.common.extension.SPI;
import com.solid4j.xmen.common.rpc.URL;

import java.util.Map;

/**
 * @author solidwangs
 * @since 1.0
 */
@SPI("netty")
public interface Transporter {

    @Adaptive("server")
    Server bind(URL url, Map<String, Object> map);
}
