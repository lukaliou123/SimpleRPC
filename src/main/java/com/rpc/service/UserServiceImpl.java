package com.rpc.service;


import com.rpc.common.User;

import javax.jws.soap.SOAPBinding;
import java.util.Random;
import java.util.UUID;

/**
 * 服务端提供服务的方法
 * UserServiceImpl服务：接收一个id，返回一个User对象
 * 提供属于这个ID（Intger)的User,User中包含他的ID,
 * 名字Name(String)和性别sex（Boolean)
 *  * 这里举个例子，构造格式为：目标类.builder()....build()：比如
 *  * User.builder().id(id).build();
 *  * 则实际上是给User构造了：
 *  *         public User.UserBuilder id(int id) {
 *  *             this.id = id;
 *  *             return this;
 *  *         }
 *
 */
public class UserServiceImpl implements UserService{
    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户端查询了ID： "+id+"的用户");

        //模拟数据库中取用户的行为
        Random random =new Random();
        User user = User.builder()
                .userName(UUID.randomUUID().toString())
                .id(id)
                .sex(random.nextBoolean())
                .build();

        return user;
    }
}
