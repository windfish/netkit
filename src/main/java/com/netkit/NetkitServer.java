package com.netkit;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.netkit.event.HeartbeatListener;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午3:37:09
 *
 */
public class NetkitServer {

    private NetkitContext context;
    private String host = "0.0.0.0";
    private int port = 8000;
    private int maxConnections = 100000;
    private ServerBootstrap bootstrap = null;
    private ChannelFactory channelFactory = null;
    private Channel channel;
    private int maxMessagePachetSize = 1 * 1024 * 1024;
    private ScheduledExecutorService heartbeatService = Executors.newScheduledThreadPool(1);
    
    public NetkitServer(NetkitContext context) {
        this.context = context;
    }
    
    public NetkitServer(NetkitContext context, int port){
        this.context = context;
        this.port = port;
    }
    
    public NetkitServer(NetkitContext context, String host, int port){
        this.context = context;
        this.host = host;
        this.port = port;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxMessagePachetSize() {
        return maxMessagePachetSize;
    }

    public void setMaxMessagePachetSize(int maxMessagePachetSize) {
        this.maxMessagePachetSize = maxMessagePachetSize;
    }
    
    public NetkitContext getContext(){
        return this.context;
    }
    
    public synchronized void startup(){
        ExecutorService bossExecutor = this.context.getBossExecutor();
        ExecutorService workerExecutor = this.context.getWorkerExecutor();
        
        this.channelFactory = new NioServerSocketChannelFactory(bossExecutor, workerExecutor);
        
        this.bootstrap = new ServerBootstrap(this.channelFactory);
        this.context.setMaxConnetions(this.maxConnections);
        
        NetkitChannelPipelineFactory channelPipelineFactory = new NetkitChannelPipelineFactory(context);
        this.bootstrap.setPipelineFactory(channelPipelineFactory);
        
        this.bootstrap.setOption("child.tcpNoDelay", Boolean.TRUE);
        this.bootstrap.setOption("child.keepAlive", Boolean.TRUE);
        
        this.channel = this.bootstrap.bind(new InetSocketAddress(host, port));
        
        // 心跳检测线程，30s一次
        this.heartbeatService.scheduleAtFixedRate(new HeartbeatListener(context), 30L, 30L, TimeUnit.SECONDS);
    }
    
    public void shutdown(){
        this.channel.close();
        this.context.release();
        this.channelFactory.releaseExternalResources();
        this.heartbeatService.shutdown();
        this.bootstrap.releaseExternalResources();
    }
    
}
