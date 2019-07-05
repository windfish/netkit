package com.netkit;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * ChannelPipeline 构造工厂
 * @author xuliang
 * @since 2019年7月5日 下午3:05:50
 *
 */
public class NetkitChannelPipelineFactory implements ChannelPipelineFactory {

    public static final String EncodeHandlerName = "encoder";
    public static final String DecodeHandlerName = "decoder";
    public static final String MessageHandlerName = "handler";
    private NetkitContext context;
    
    public NetkitChannelPipelineFactory(NetkitContext context) {
        this.context = context;
    }
    
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast(DecodeHandlerName, new NetkitProtocolDecoder());
        pipeline.addLast(EncodeHandlerName, new NetkitProtocolEncoder());
        pipeline.addLast(MessageHandlerName, new NetkitChannelHandler(context));
        return pipeline;
    }

}
