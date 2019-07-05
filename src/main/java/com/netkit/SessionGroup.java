package com.netkit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * 
 * @author xuliang
 * @since 2019年7月4日 下午3:10:06
 *
 */
public class SessionGroup {

    private final ConcurrentHashMap<Serializable, Session> sessionMap = new ConcurrentHashMap<Serializable, Session>();
    
    private final ChannelFutureListener remover = new ChannelFutureListener() {
        
        public void operationComplete(ChannelFuture future) throws Exception {
            int sessionID = future.getChannel().getId().intValue();
            List<Serializable> keys = SessionGroup.this.findKeysBySessionId(sessionID);
            SessionGroup.this.remove(keys);
        }
    };
    
    public synchronized boolean add(Serializable key, Session s){
        Session session = s;
        Channel channel = session.getChannel();
        
        boolean added = sessionMap.putIfAbsent(key, session) == null;
        if(added){
            channel.getCloseFuture().addListener(remover);
        }
        return added;
    }
    
    public synchronized void close(){
        for(Map.Entry<Serializable, Session> entry: sessionMap.entrySet()){
            Session s = entry.getValue();
            s.close();
        }
        sessionMap.clear();
    }
    
    public boolean isEmpty(){
        return sessionMap.isEmpty();
    }
    
    public synchronized int remove(Session session){
        if(session == null){
            return 0;
        }
        List<Serializable> keys = findKeysBySessionId(session.getSessionId());
        int count = remove(keys);
        return count;
    }
    
    public synchronized Session remove(Serializable key){
        Session s = sessionMap.get(key);
        if(s == null){
            return null;
        }
        s.getChannel().getCloseFuture().removeListener(remover);
        return s;
    }
    
    public synchronized int remove(Collection<Serializable> keys){
        int count = 0;
        for(Serializable key: keys){
            Session s = remove(key);
            if(s != null){
                count++;
            }
        }
        return count;
    }
    
    public Session get(Serializable key){
        return sessionMap.get(key);
    }
    
    public List<Serializable> findKeysBySessionId(int sessionId){
        List<Serializable> keys = new ArrayList<Serializable>();
        
        for(Map.Entry<Serializable, Session> entry: sessionMap.entrySet()){
            Session s = entry.getValue();
            if(s.getSessionId() == sessionId){
                keys.add(entry.getKey());
            }
        }
        return keys;
    }
    
    public int size(){
        return sessionMap.size();
    }

    public Collection<Serializable> keys(){
        return sessionMap.keySet();
    }
    
    public Collection<Session> sessions(){
        return sessionMap.values();
    }
    
}
