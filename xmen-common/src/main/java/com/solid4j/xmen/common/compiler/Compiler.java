/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.compiler;

import com.solid4j.xmen.common.extension.SPI;

/**
 * @author solidwang
 * @since 1.0
 */
@SPI("javassist")
public interface Compiler {

    Class<?> compile(String code);
}
