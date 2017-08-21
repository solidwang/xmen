/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.rpc.protocol.xmen;

import com.solid4j.xmen.common.extension.ExtensionLoader;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.remote.Server;
import com.solid4j.xmen.remote.Transporter;
import com.solid4j.xmen.rpc.Exporter;
import com.solid4j.xmen.rpc.Invoker;
import com.solid4j.xmen.rpc.protocol.AbstractProtocol;

import java.util.Map;

/**
 * @author solidwang
 * @since 1.0
 */
public class XmenProtocol<T> extends AbstractProtocol<T> {

    @Override
    public Invoker<T> refer(Class<T> type, URL url) {
        XmenInvoker<T> invoker = new XmenInvoker<T>(type, url);
        return invoker;
    }

    @Override
    public void destroy() {

    }

    @Override
    public Exporter<T> export(final Invoker<T> invoker, final Map<String, Object> map) {
        XmenExporter xmenExporter = new XmenExporter(invoker);
        openServer(invoker, map);
        return xmenExporter;
    }

    private void openServer(Invoker invoker, Map<String, Object> map) {
        Transporter transporter = ExtensionLoader.getExtensionLoader(Transporter.class).getAdaptiveExtension();
        Server server = transporter.bind(invoker.getURL(), map);
    }

}
