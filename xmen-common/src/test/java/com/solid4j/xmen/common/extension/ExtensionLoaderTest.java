/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.extension;

import com.solid4j.xmen.common.compiler.Compiler;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author: solidwang
 * @since 1.0
 */
public class ExtensionLoaderTest {

    @Test
    public void getExtensionTest() {
        Assert.assertTrue(ExtensionLoader.getExtensionLoader(TestService.class).getExtension("impl1") instanceof TestServiceImpl);
        Assert.assertTrue(ExtensionLoader.getExtensionLoader(TestService.class).getExtension("impl2") instanceof TestServiceImpl2);
    }

    @Test
    public void getDefaultExtensionTest() {
        Assert.assertTrue(ExtensionLoader.getExtensionLoader(TestService.class).getDefaultExtension() instanceof TestServiceImpl);
    }

    @Test
    public void getAdaptiveExtensionTest() {
        ExtensionLoader.getExtensionLoader(Compiler.class).getAdaptiveExtension();
    }
}
