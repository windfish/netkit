package com.netkit;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * 用来监听channel 未来的结果
 * @author xuliang
 * @since 2019年7月3日 下午5:14:15
 *
 */
public abstract class FutureListener implements ChannelFutureListener {

    private Session session;

    public void setSession(Session session) {
        this.session = session;
    }
    
    public void operationComplete(ChannelFuture future) throws Exception {
        onComplate(this.session);
    }
    
    public abstract void onComplate(Session session);
    
}
