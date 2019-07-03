package com.netkit.event;

import java.util.EventListener;

import com.netkit.Session;

public interface NetkitSessionListener extends EventListener {

    void onEvent(Session session);
    
}
