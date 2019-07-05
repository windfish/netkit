package com.netkit;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午4:17:26
 *
 */
public class NetkitClient {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private String host;
    private int port;
    private NetkitContext context;
    private ClientBootstrap bootstrap;
    private ChannelFactory channelFactory;
    
    public NetkitClient(String host, int port, NetkitContext context){
        this.host = host;
        this.port = port;
        this.context = context;
        
        this.channelFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        this.bootstrap = new ClientBootstrap(channelFactory);
        this.bootstrap.setPipelineFactory(new NetkitChannelPipelineFactory(context));
    }
    
    public Session createSession() throws Exception {
        ChannelFuture future = this.bootstrap.connect(new InetSocketAddress(host, port));
        future = future.awaitUninterruptibly();
        
        if(future.isSuccess()){
            return this.context.createSession(future.getChannel(), null, null);
        }
        
        throw new Exception(future.getCause());
    }
    
    public void close(){
        this.context.release();
        this.channelFactory.releaseExternalResources();
        this.bootstrap.releaseExternalResources();
    }
    
    class CreateSessionSuccessListener implements ChannelFutureListener {
        
        private Session session;
        private Object lock = new Object();

        public void operationComplete(ChannelFuture future) throws Exception {
            try{
                session = NetkitClient.this.context.createSession(future.getChannel(), null, null);
            }catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            synchronized (lock) {
                lock.notifyAll();
            }
        }
        
        public Session getSession() throws InterruptedException{
            join();
            return this.session;
        }
        
        private void join() throws InterruptedException {
            synchronized (lock) {
                while(this.session == null){
                    this.lock.wait();
                }
            }
        }
        
    }
    
}
