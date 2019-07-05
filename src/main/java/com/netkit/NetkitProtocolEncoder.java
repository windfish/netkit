package com.netkit;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.utils.Message;
import com.netkit.utils.MessageEncoder;

/**
 * 编码器
 * @author xuliang
 * @since 2019年7月5日 上午10:36:43
 *
 */
public class NetkitProtocolEncoder extends SimpleChannelHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(NetkitProtocolEncoder.class);
    
    protected ChannelBuffer encode(ChannelHandlerContext context, Channel channel, Object msg) throws Exception {
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
        Message message = (Message) msg;
        
        MessageEncoder.encode(buffer, message);
        return buffer;
    }
    
    public void writeRequested(ChannelHandlerContext context, MessageEvent event) throws Exception{
        Object msg = event.getMessage();
        if(msg == null || !(msg instanceof Message)){
            return;
        }
        
        Message message = (Message) msg;
        byte[] bytes = MessageEncoder.encode(message);
        
        if(Message.isPrintLog(message.getCmd())){
            LOGGER.debug("socket message encoder: [{}] - [{}] - message[{}]", context.getChannel().getRemoteAddress().toString(),
                                context.getChannel().getId(), message.toString());
        }
        
        ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(bytes);
        ChannelFuture future = event.getFuture();
        Channels.write(context, future, buffer);
    }
    
}
