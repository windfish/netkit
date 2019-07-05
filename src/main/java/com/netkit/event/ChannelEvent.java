package com.netkit.event;

import java.util.EventObject;

import org.jboss.netty.channel.Channel;

import com.netkit.NetkitContext;

/**
 * Channel 事件状态对象，包含发生事件的对象
 * @author xuliang
 * @since 2019年7月3日 下午3:32:12
 *
 */
public class ChannelEvent extends EventObject {

    private static final long serialVersionUID = 1051629896923337201L;

    private NetkitContext netkitContext;
    private Channel channel;    // 发生事件的对象
    
    public ChannelEvent(NetkitContext netkitContext, Channel channel) {
        super(channel);
        this.netkitContext = netkitContext;
        this.channel = channel;
    }

    public NetkitContext getNetkitContext() {
        return netkitContext;
    }

    public Channel getChannel() {
        return channel;
    }
    
    public int getChannelID(){
        return getChannel().getId().intValue();
    }

}
