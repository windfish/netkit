package com.netkit.event;

import java.util.EventListener;

import com.netkit.Session;

/**
 * 事件监听接口，负责监听Session 事件
 * @author xuliang
 * @since 2019年7月3日 下午4:55:27
 *
 */
public interface NetkitSessionListener extends EventListener {

    /**
     * 事件发生时的处理方法
     */
    void onEvent(Session session);
    
}
