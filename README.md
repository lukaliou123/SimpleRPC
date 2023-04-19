一个写了简单的RPC结构
参考了https://github.com/weiyu-zeng/SimpleRPC 的步骤

当前流程
01：4.7  
01总结：实现了仅仅能用的RPC，但是调用只能调用server中某一个确定的方法

02: 4.8  
02总结：相比01，首先抽象了request和response,不在写死
RPCRequest有了interface,paramName，methodname之类的参数
RPCResponse可以储存返回的数据，并返回一个成功或失败数值

为什么要使用线程？
因为BIO只能同时处理一个客户端的连接，如果需要管理多个客户端的话，就需要为我们请求的客户端单独创建一个线程


然后，使用了动态代理
将Client更注重于功能的具体实现，将网络连接功能交给了client的代理类
同时将写入读取数据流的功能分给了IOClient，由proxy与其交流
ioc也负责进行网络连接，参数由代理类给


动态代理使用了jdk动态代理，使用方式就是使用反射来获得方法，使具体代码不会被局限
JDK动态代理要调用InvocationHandler这个方法

03:4.9
我们将新写一个服务接口：通过某个id查询blog信息  
们本次改进希望重构服务端server的代码，使得server可以容纳多个service接口的调用。我们将为此通过HashMap构建一个service的映射关系。  
这次使用了ServiceProvider来管理这个HashMap  

另外原来的server处理请求是用BIO的监听，捕获消息之后直接new一个线程来处理，我们这里将使用一个线程池来代替直接new 一个线程，实现线程的有效管理。  
这里再次学习了下线程池的用法，使用了一个实现runnable接口的WorkThread进行单线程的逻辑
接着线程池执行并管理该线程

4.10：
运行失败，是反射问题，java.lang.reflect.UndeclaredThrowableException，但我找不到具体问题
4.11：
今天很忙，没时间更新，感觉对netty理解不够到位，先看看netty的八股文吧
在这个项目里，如果要调用netty的话，除了netty本身，还要写initialize和handler
具体是netty创建NioEventLoopGroup,channel要启动这个线程池需要用initializer来解决粘包问题，initializer在管道添加了固定长度或其他的什么管理数据传输  
并在管道最后添加handler，这个handler里包含如何保存或运行RPC请求的功能细节，客户端管请求，服务端管回复
顺便，initializer继承了ChannelInitializer，handler继承了SimpleChannelInboundHandler，都是让channel进行管理

4.11：
channelFuture.channel().closeFuture().sync();
找到了问题，是因为服务端没有使用sync导致没有使主线程堵塞而快速关闭，关于此的详情请见issue的笔记

4.12
今天试试修改序列化，原本的java序列化效率太低，这次用json进行序列化修改

4.13
本次改进主要针对序列化的方式进行改进。之前一直在使用java自带的序列化方式，或者是netty的序列化方式，性能不高。

这个版本我们定义自己的序列化方式。

不过我还是觉得麻烦，下次试试kryo

4.15:
写了zk的注册中心部分，感觉就是不停地玩地址
头好痛，不想周末上班，好累
明天努努力应该能吧zk部分写完，但是其中的知识我需要消化一下

4.16:
完成了zk的部分，意外的简单，就是当个注册中心，把地址放进去。。。
头有点疼，睡会，晚上把知识整理下

4.18:
完成了负载均衡部分，一个随机，一个轮转，不过总觉得输出有点不对，为什么轮转没成功呢。。。
总之再看看负载均衡的知识点
明天把序列化改为kryo

4.19
完成了kryo序列器的更改，果然代码更简洁，研究下三个序列器

这个项目正式完成，接下来就是总结知识点和写进简历了