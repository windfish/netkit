package com.netkit.event;

import com.netkit.FutureListener;
import com.netkit.Session;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午1:21:10
 *
 */
public abstract class SendMessageCompletedListener extends FutureListener {

    public abstract void onComplate(Session session);
    
}
