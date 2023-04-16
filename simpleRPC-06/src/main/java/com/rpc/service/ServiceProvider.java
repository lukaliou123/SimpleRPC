package com.rpc.service;


import com.rpc.register.ServiceRegister;
import com.rpc.register.ZkServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zwy
 */
public class ServiceProvider {
    /**
     * 一个实现类可能实现多个接口
     */
    private Map<String, Object> interfaceProvider;

    private ServiceRegister serviceRegister;
    private String host;
    private int port;

    // 构造函数，初始化一个空的 hashmap 赋给 Map<String, Object> interfaceProvider
    public ServiceProvider(String host, int port) {
        //需要传入服务端自身的服务的网络地址
        this.host = host;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZkServiceRegister();
    }

    public void provideServiceInterface(Object service) {
        // 反射，.getClass().getInterfaces()得到class的interface，按照interfaces name(key)和object(value)存入map
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for (Class clazz : interfaces) {
            interfaceProvider.put(clazz.getName(), service);
            //在注册中心注册服务
            serviceRegister.register(clazz.getName(), new InetSocketAddress(host,port));
        }
    }

    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);  // 通过interface name得到object
    }
}

