/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.protocol;

import com.solid4j.xmen.rpc.Exporter;
import com.solid4j.xmen.rpc.Invoker;

/**
 * @author solidwang
 * @since 1.0
 */
public class AbstractExporter<T> implements Exporter<T> {

    private Invoker<T> invoker;

    private volatile boolean unexported = false;

    public AbstractExporter(Invoker<T> invoker) {
        this.invoker = invoker;
    }

    @Override
    public Invoker<T> getInvoker() {
        return invoker;
    }

    @Override
    public void unExport() {
        if (unexported) { return; }
        unexported = true;
        //getInvoker();
    }

}
