/*
 * Copyright (c) 2017 solidwang. All Rights Reserved
 */
package com.solid4j.xmen.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author solidwang
 * @since 1.0
 */
public class NetUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetUtil.class);

    public static final String ANYHOST = "0.0.0.0";
    public static final String LOCALHOST = "127.0.0.1";

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    public static String getLocalAddress() {
        return getInetAddress().getHostAddress();
    }

    /**
     * 查找策略：hostname对应的ip --> 轮询网卡
     * 
     * @return
     */
    public static InetAddress getInetAddress() {
        InetAddress inetAddress = getLostAddressByHostname();
        if (!isValidAddress(inetAddress)) {
            inetAddress = getLocalAddressByNetworkInterface();
        }
        return inetAddress;
    }

    public static boolean isInvalidLocalhost(String host) {
        return host == null || host.length() == 0 || host.equalsIgnoreCase("localhost") || host.equals("0.0.0.0");
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches());
    }

    private static InetAddress getLocalAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            try {
                                InetAddress address = addresses.nextElement();
                                if (isValidAddress(address)) {
                                    return address;
                                }
                            } catch (Throwable e) {
                                LOGGER.warn("Failed to retriving ip address, " + e.getMessage(), e);
                            }
                        }
                    } catch (Throwable e) {
                        LOGGER.warn("Failed to retriving ip address, " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            LOGGER.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        return null;
    }

    private static InetAddress getLostAddressByHostname() {
        try {
            InetAddress localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getLocalAddress());
    }

}
