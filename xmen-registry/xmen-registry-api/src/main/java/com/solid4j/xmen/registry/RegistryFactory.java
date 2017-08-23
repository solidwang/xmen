/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.registry;

import com.solid4j.xmen.common.extension.SPI;
import com.solid4j.xmen.common.rpc.URL;

/**
 * @author: solidwang
 * @since 1.0
 */
@SPI("zookeeper")
public interface RegistryFactory {

    void registry(String serviceName, URL url);

    String discovery(String serviceName, URL url);
}
