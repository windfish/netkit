package com.netkit.event;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.NetkitContext;
import com.netkit.Session;

/**
 * session 心跳监听，若心跳超时，则关闭session
 * @author xuliang
 * @since 2019年7月5日 下午3:24:49
 *
 */
public class HeartbeatListener implements Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    private NetkitContext context;

    public HeartbeatListener(NetkitContext context) {
        this.context = context;
    }

    public void run() {
        try{
            Collection<Session> sessions = this.context.getAllSessions();
            
            for(Session s: sessions){
                long heartbeatTime = s.getLastCommunicationTime();
                long diffTime = System.currentTimeMillis() - heartbeatTime;
                if(diffTime > this.context.getSessionTimeout()){
                    logger.warn("the session [{}] was timeout, close this channel [{}]", s.getSessionId(), s.getRemoteAddress());
                    s.close();
                }
            }
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
    
}
