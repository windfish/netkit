package com.netkit.event;

import java.util.EventListener;

import com.netkit.utils.Message;

/**
 * 事件监听接口，负责监听Channel 事件
 * @author xuliang
 * @since 2019年7月4日 下午4:07:40
 *
 */
public interface NetkitChannelEventListener extends EventListener {

    void onOpen(ChannelEvent event);
    void onConnected(ChannelEvent event);
    void onDisconnected(ChannelEvent event);
    void onExceptionCaught(ChannelEvent event, Throwable cause);
    void onClosed(ChannelEvent event);
    void onMessageReceived(ChannelEvent event, Message message);
    
}
