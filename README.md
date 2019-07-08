# netkit
基于netty3.7 的封装，简化通信上的处理，专注于业务上的处理

# 例子
```
final int LOGIN = 0x0001;
NetkitContext context = new NetkitContext();
context.registerAction(LOGIN, LoginAction.class);   // 注册登录的逻辑处理类
NetkitServer server = new NetkitServer(context, host, port);
server.startup();
```
