/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.registry.zookeeper;

import com.solid4j.xmen.common.compiler.Compiler;
import com.solid4j.xmen.common.extension.ExtensionLoader;
import com.solid4j.xmen.registry.RegistryFactory;

import org.junit.Test;

/**
 * @author: solidwang
 * @since 1.0
 */
public class ExtensionLoaderTest {

    @Test
    public void getDefaultExtensionTest() {
        RegistryFactory registryFactory= ExtensionLoader.getExtensionLoader(RegistryFactory.class).getDefaultExtension();
    }
}
