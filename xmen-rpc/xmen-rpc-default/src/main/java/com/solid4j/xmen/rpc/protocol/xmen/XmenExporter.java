/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.protocol.xmen;

import com.solid4j.xmen.rpc.Invoker;
import com.solid4j.xmen.rpc.protocol.AbstractExporter;

/**
 * @author solidwang
 * @since 1.0
 */
public class XmenExporter<T> extends AbstractExporter<T> {

    public XmenExporter(Invoker<T> invoker) {
        super(invoker);
    }

}

    