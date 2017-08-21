/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.compiler.support;

import com.solid4j.xmen.common.compiler.Compiler;
import com.solid4j.xmen.common.extension.Adaptive;
import com.solid4j.xmen.common.extension.ExtensionLoader;

/**
 * @author solidwang
 * @since 1.0
 */
@Adaptive
public class AdaptiveCompiler implements Compiler {
    
    @Override
    public Class<?> compile(String code) {
        ExtensionLoader<Compiler> loader = ExtensionLoader.getExtensionLoader(Compiler.class);
        Compiler compiler = loader.getDefaultExtension();
        return compiler.compile(code);
    }
}
