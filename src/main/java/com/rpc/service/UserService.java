package com.rpc.service;

import com.rpc.common.User;

/**
 * 服务端提供服务的方法的接口
 */
public interface UserService {
    //客户端通过这个接口调用服务器端的实现类
    User getUserByUserId(Integer id);
}
