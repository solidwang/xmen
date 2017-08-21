/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.remote.netty;

import com.solid4j.xmen.common.extension.ExtensionLoader;
import com.solid4j.xmen.common.obj.RpcRequest;
import com.solid4j.xmen.common.obj.RpcResponse;
import com.solid4j.xmen.common.rpc.URL;
import com.solid4j.xmen.registry.RegistryFactory;
import com.solid4j.xmen.remote.netty.codec.RpcDecoder;
import com.solid4j.xmen.remote.netty.codec.RpcEncoder;
import com.solid4j.xmen.remote.server.AbstractServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author solidwang
 * @since 1.0
 */
public class NettyServer extends AbstractServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);
    // 注册中心
    private static RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getDefaultExtension();
    // 服务名称和实现
    private Map<String, Object> handlerMap = new ConcurrentHashMap<String, Object>();
    // 服务URL
    private URL url;

    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyServer(URL url, Map<String, Object> map) {
        this.url = url;
        this.handlerMap = map;
        doOpen();
    }

    @Override
    public void doOpen() {
        try {
            // 创建并初始化 Netty 服务端 Bootstrap 对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast(new RpcDecoder(RpcRequest.class));
                    pipeline.addLast(new RpcEncoder(RpcResponse.class));
                    pipeline.addLast(new NettyServerHandler(handlerMap));
                }
            });
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            // 获取 RPC 服务器的 IP 地址与端口号
            ChannelFuture future = bootstrap.bind(url.getHost(), url.getPort()).sync();

            //将服务写入注册中心
            for (String interfaceName : handlerMap.keySet()) {
                registryFactory.registry(interfaceName, url);
                LOGGER.info("register service: {} => {}", interfaceName, url);
            }
            LOGGER.info("server started on port {}:{}", url.getHost(), url.getPort());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doClose() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
