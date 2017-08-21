package com.solid4j.xmen.common.extension;/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */

/**
 * @author solidwang
 */
@SPI("impl1")
public interface TestService {
    
    String hello(String name);
    
    String adaptive();
}

    