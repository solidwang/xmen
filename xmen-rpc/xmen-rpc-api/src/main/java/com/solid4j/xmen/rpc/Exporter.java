/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc;
/**
 * @author solidwang
 * @since 1.0
 */
public interface Exporter<T> {

    Invoker<T> getInvoker();
    
    void unExport();
}

    