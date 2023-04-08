一个写了简单的RPC结构
参考了https://github.com/weiyu-zeng/SimpleRPC 的步骤

当前流程
01：04.7
01总结：实现了仅仅能用的RPC，但是调用只能调用server中某一个确定的方法

02: 04.8
02总结：相比01，首先抽象了request和response,不在写死
RPCRequest有了interface,paramName，methodname之类的参数
RPCResponse可以储存返回的数据，并返回一个成功或失败数值

然后，使用了动态代理
将Client更注重于功能的具体实现，将网络连接功能交给了client的代理类
同时将写入读取数据流的功能分给了IOClient，由proxy与其交流
ioc也负责进行网络连接，参数由代理类给


动态代理使用了jdk动态代理，使用方式就是使用反射来获得方法，使具体代码不会被局限
JDK动态代理要调用InvocationHandler这个方法