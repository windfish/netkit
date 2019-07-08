package com.netkit;

import com.netkit.utils.Message;

public interface MessageSendable {

    public abstract Future sendMessage(Message message);
    
}
