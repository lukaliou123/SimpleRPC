package com.rpc.service;


import java.util.HashMap;

/**
 * ServiceProvider存放服务接口名与服务端对应的实现类（本质是hashmap)，服务启动时要暴露其相关的实现类
 * 根据request中的interface调用服务端中相关实现类
 * 这算不算一个小工厂？
 */
public class ServiceProvider {
    /**
     * 一个实现类可能实现多个接口
     */

    private HashMap<String,Object> interfaceProvider;

    //构造函数，初始化一个空的hashmap赋给Map<String,Object> InterfaceProvider
    public ServiceProvider(){
        this.interfaceProvider = new HashMap<>();
    }

    public void provideServiceInterface(Object service){
        //使用反射的 getClass().getInterface()得到class的interface，按照interfaces name(key)和object(value)存入map
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for(Class clazz: interfaces){
            interfaceProvider.put(clazz.getName(),service);
        }
    }

    public Object getService(String interfaceName){
        return interfaceProvider.get(interfaceName);
    }


}
