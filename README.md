# netty_redis_zookeeper_source_code
netty,redis,zookeeper高并发实战源码


## java BIO 模型
    当个一个客户端连接进来 都要创建一个线程 与之进行通信，每个线程都独立处理自己
    负责的socket连接的输入和输出，
## BIO模型的问题
    1、 严重依赖 线程， 线程的创建和销毁都需要依赖内核的系统调用，
    2、 线程本身的占用的内存就很大， 一个java线程 占用的512k ~ 1m之间，如果
        如果线程数量过多，jvm内存就会耗尽，影响系统性能
    3、 线程之间的切换代价是非常高的，操作系统发生线程切换的时候，需要保留线程当前的上下文
        然后执行系统调用，过多的线程频繁切换带来的后果就是，线程切换的时间大于线程执行的时间
        导致cpu的负载过高，直接影响到系统的性能
       
       
## java NIO 有三个核心组件
* channel
* buffer
* selector

## NIO 中的channel
* FileChannel文件通道  用于文件的数据读写
* DatagramChannel 用于udp操作
* SocketChannel套接字通道 用于Socket套接字TCP连接的数据读写
* ServerSocketChannel 服务器套接字通道（或服务器监听通道），允许我们监听TCP连接请求，为每个监听到的请求，创建一个SocketChannel套接字通道

    什么是Channel的本质，该如何理解这个抽象的概念
            
            就是对传输链路所对应的文件描述符的一种封装
    
##  NIO中的 selector
    selector 表示一个io事件的监听和查询器，通过selector，一个线程可以监听多个通道的io事件的就绪状态
    可供选择器监控的通道IO事件类型，包括以下四种：
     （1）可读：SelectionKey.OP_READ
     （2）可写：SelectionKey.OP_WRITE 
     （3）连接：SelectionKey.OP_CONNECT 
     （4）接收：SelectionKey.OP_ACCEPT   
     
selector 使用流程
*  获取选择器实例
        
        调用静态工厂方法open()来获取Selector实例 
        Selector selector = Selector.open();
*  将通道注册到选择器实例中

        // 2.获取通道 
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); 
        // 3.设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 4.绑定连接
         serverSocketChannel.bind(new InetSocketAddress(18899)); 
         // 5.将通道注册到选择器上,并制定监听事件为：“接收连接”事件 
         serverSocketChannel.register(selector，SelectionKey.OP_ACCEPT);
*  轮询感兴趣的io就绪事件   
## 使用Buffer类的基本步骤
    （1）使用创建子类实例对象的allocate()方法，创建一个Buffer类的实例对象。
    （2）调用put()方法，将数据写入到缓冲区中。
    （3）写入完成后，在开始读取数据前，调用Buffer.flip()方法，将缓冲区转换为读模式。
    （4）调用get()方法，可以从缓冲区中读取数据。
    （5）读取完成后，调用Buffer.clear( )方法或Buffer.compact()方法，将缓冲区转换为写入模式，可以继续写入。
    
## 详解NIO Channel类
    对应到不同的网络传输协议类型，在Java中都有不同的NIO Channel（通道）相对应
    


