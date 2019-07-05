package com.netkit;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.Channels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.event.NetkitSessionListener;
import com.netkit.exception.SessionException;
import com.netkit.utils.Message;

/**
 * session
 * 
 * @author xuliang
 * @since 2019年7月3日 下午3:23:07
 *
 */
public class Session implements MessageSendable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

    private NetkitContext context;
    private Channel channel; // 通信通道

    private final Map<String, Object> attributeMap = new HashMap<String, Object>();
    private NetkitSessionListener onCreatedListener = null; // session 建立事件的监听器
    private NetkitSessionListener onClosedListener = null; // session 关闭事件的监听器

    private long lastCommunicationTime = System.currentTimeMillis(); // 最后通信时间
    private final MessageDispatcher dispatcher = new MessageDispatcher();

    Session(NetkitContext context, Channel channel) {
        this.context = context;
        this.channel = channel;
        ChannelFuture future = channel.getCloseFuture();
        future.addListener(new ChannelClosedListener());
    }

    public NetkitContext getContext() {
        return this.context;
    }

    public void setAttribute(String key, Object value) {
        this.attributeMap.put(key, value);
    }

    public Object getAttribute(String key) {
        return this.attributeMap.get(key);
    }

    public Set<String> getAttributeNames() {
        return this.attributeMap.keySet();
    }

    public Object removeAttribute(String key) {
        return this.attributeMap.remove(key);
    }

    public int getSessionId() {
        return this.channel.getId().intValue();
    }
    
    /**
     * 通过channel 发送消息
     * @param message
     * @return
     */
    public Future sendMessage(Message message){
        onSessionMessage(message);
        if(!this.channel.isConnected()){
            throw new SessionException("The channel was closed,cannot write message.");
        }
        ChannelFuture future = Channels.write(this.channel, message);
        return new Future(future, this);
    }
    
    public Future sendMessageAndClose(Message message){
        ChannelFuture future = Channels.write(this.channel, message);
        future.addListener(ChannelFutureListener.CLOSE);
        return new Future(future, this);
    }
    
    public SocketAddress getLocalAddress(){
        return this.channel.getLocalAddress();
    }
    
    public SocketAddress getRemoteAddress(){
        return this.channel.getRemoteAddress();
    }
    
    public Channel getChannel(){
        return this.channel;
    }
    
    public Future close(){
        ChannelFuture future = this.channel.close();
        return new Future(future, this);
    }
    
    public void setOnCreatedEventListener(NetkitSessionListener listener){
        this.onCreatedListener = listener;
    }
    
    public void setOnClosedEventListener(NetkitSessionListener listener){
        this.onClosedListener = listener;
    }
    
    public NetkitSessionListener getOnCreatedEventListener(){
        return this.onCreatedListener;
    }
    
    public NetkitSessionListener getOnClosedEventListener(){
        return this.onClosedListener;
    }
    
    public boolean isClosed(){
        return !this.channel.isOpen();
    }
    
    public boolean isConnected(){
        return this.channel.isConnected();
    }
    
    public boolean isSendable(){
        return this.channel.isWritable();
    }
    
    public long getLastCommunicationTime(){
        return this.lastCommunicationTime;
    }
    
    public void onSessionMessage(Message message){
        this.lastCommunicationTime = System.currentTimeMillis();
    }
    
    /**
     * 接收到消息时，使用MessageTask 异步处理，
     */
    public void onRecvMessage(Message message){
        this.lastCommunicationTime = System.currentTimeMillis();
        // 创建MessageTask
        MessageTask task = MessageTask.newInstance(dispatcher, this, message);
        
        // 利用context 执行MessageTask
        context.getWorkerExecutor().submit(task);
    }
    
    protected void onCreated(){
        try{
            if(getOnCreatedEventListener() != null){
                getOnCreatedEventListener().onEvent(this);
            }
        }catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
    
    protected void onClosed(){
        try{
            if(getOnClosedEventListener() != null){
                getOnClosedEventListener().onEvent(this);
            }
        }catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
        this.attributeMap.clear();
    }

    class ChannelClosedListener implements ChannelFutureListener {

        public void operationComplete(ChannelFuture future) throws Exception {
            Session.this.onClosed();
        }

    }

}
