package com.netkit;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.event.ChannelEvent;
import com.netkit.event.NetkitChannelEventListener;
import com.netkit.event.SendMessageCompletedListener;
import com.netkit.utils.Message;

/**
 * 通道处理程序
 * @author xuliang
 * @since 2019年7月5日 上午10:55:48
 *
 */
class NetkitChannelHandler extends SimpleChannelUpstreamHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private NetkitContext context;
    private NetkitChannelEventListener channelEventListener;
    private Session session;
    
    public NetkitChannelHandler(NetkitContext context) {
        this.context = context;
        this.channelEventListener = context.getChannelEventListener();
    }
    
    /**
     * 通道收到消息时
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if(!(e.getMessage() instanceof Message)){
            return;
        }
        Message message = (Message) e.getMessage();
        if(Message.isPrintLog(message.getCmd())){
            logger.debug("messageReceived: {}", message);
        }
        
        try{
            if(channelEventListener != null){
                ChannelEvent event = new ChannelEvent(context, e.getChannel());
                this.channelEventListener.onMessageReceived(event, message);
            }
        }catch (Exception ex) {
            logger.warn("handle message received event listener exception", ex);
        }
    }
    
    /**
     * 通道连接时
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception{
        super.channelConnected(ctx, e);
        if(this.channelEventListener != null){
            ChannelEvent event = new ChannelEvent(context, e.getChannel());
            this.channelEventListener.onConnected(event);
        }
        int sessionSize = this.context.getSessionSize();
        if(sessionSize > this.context.getMaxConnetions()){
            Message message = new Message(0);
            this.session.sendMessage(message).addFutureListener(new SendMessageCompletedListener() {
                @Override
                public void onComplate(Session session) {
                    session.close();
                }
            });
            logger.warn("The server connections has reached maximum.");
        }
    }
    
    /**
     * 通道断开连接时
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        if(this.channelEventListener != null){
            ChannelEvent event = new ChannelEvent(context, e.getChannel());
            this.channelEventListener.onDisconnected(event);
        }
    }
    
    /**
     * 通道联通时，创建session
     */
    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        if(this.channelEventListener != null){
            ChannelEvent event = new ChannelEvent(context, e.getChannel());
            this.channelEventListener.onOpen(event);
        }
        this.session = this.context.createSession(e.getChannel());
    }
    
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelClosed(ctx, e);
        if(this.channelEventListener != null){
            ChannelEvent event = new ChannelEvent(context, e.getChannel());
            this.channelEventListener.onClosed(event);
        }
    }
    
    /**
     * 通道发生异常时
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e){
        if(channelEventListener != null){
            ChannelEvent event = new ChannelEvent(context, e.getChannel());
            this.channelEventListener.onExceptionCaught(event, e.getCause());
        }
        e.getFuture().cancel();
        logger.warn("channel [{}] exception, {}", e.getChannel().getId(), e.getCause().getMessage());
    }
    
}
