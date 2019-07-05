package com.netkit;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.channel.Channel;

import com.netkit.event.NetkitChannelEventListener;
import com.netkit.event.NetkitSessionListener;

/**
 * 
 * @author xuliang
 * @since 2019年7月4日 上午10:12:36
 *
 */
public class NetkitContext {

    private ExecutorService bossExecutor = null;
    private ExecutorService workerExecutor = null;      // 工作线程池

    private final Map<Integer, Class<Action>> registerActions = new ConcurrentHashMap<Integer, Class<Action>>();
    private final List<ActionFilter> actionFilters = new LinkedList<ActionFilter>();
    private final Map<String, Object> attributes = new ConcurrentHashMap<String, Object>();
    private final SessionGroup sessionGroup = new SessionGroup();
    private NetkitChannelEventListener channelEventListener = null;
    private NetkitSessionListener sessionOnCreatred = null;
    private NetkitSessionListener sessionOnClosed = null;
    private long sessionTimeout = 120000L;
    private int maxConnetions = 100000;

    public ExecutorService getBossExecutor() {
        if (bossExecutor == null) {
            bossExecutor = Executors.newCachedThreadPool();
        }
        return bossExecutor;
    }

    public void setBossExecutor(ExecutorService bossExecutor) {
        this.bossExecutor = bossExecutor;
    }

    public ExecutorService getWorkerExecutor() {
        if (workerExecutor == null) {
            workerExecutor = Executors.newCachedThreadPool();
        }
        return workerExecutor;
    }

    public void setWorkerExecutor(ExecutorService workerExecutor) {
        this.workerExecutor = workerExecutor;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void addActionFilter(ActionFilter filter) {
        if (filter == null) {
            throw new NullPointerException("action filter was null");
        }
        actionFilters.add(filter);
    }

    public List<ActionFilter> getActionFilters() {
        return Collections.unmodifiableList(actionFilters);
    }

    public void registerAction(int cmd, Class<Action> claxx) {
        synchronized (registerActions) {
            Class<Action> clazz = registerActions.get(cmd);
            if (clazz != null) {
                registerActions.remove(cmd);
            }
            registerActions.put(cmd, claxx);
        }
    }

    public Class<Action> lookupRegisteredAction(int cmd) {
        return registerActions.get(cmd);
    }

    public int getSessionSize() {
        return sessionGroup.size();
    }

    public NetkitChannelEventListener getChannelEventListener() {
        return channelEventListener;
    }

    public void setChannelEventListener(NetkitChannelEventListener channelEventListener) {
        this.channelEventListener = channelEventListener;
    }

    public NetkitSessionListener getSessionOnCreatedEventListener() {
        return this.sessionOnCreatred;
    }

    public void setSessionOnCreatedEventListener(NetkitSessionListener listener) {
        this.sessionOnCreatred = listener;
    }

    public NetkitSessionListener getSessionOnClosedEventListener() {
        return this.sessionOnClosed;
    }

    public void setSessionOnClosedEventListener(NetkitSessionListener listener) {
        this.sessionOnClosed = listener;
    }
    
    public Session getSession(Channel channel){
        return getSession(channel.getId());
    }
    
    public Session getSession(int sessionId){
        return sessionGroup.get(sessionId);
    }
    
    protected Session createSession(Channel channel){
        if(channel == null){
            return null;
        }
        Session session = getSession(channel);
        if(session != null){
            session.setOnCreatedEventListener(sessionOnCreatred);
            session.setOnClosedEventListener(sessionOnClosed);
            return session;
        }
        Session s = new Session(this, channel);
        s.setOnCreatedEventListener(sessionOnCreatred);
        s.setOnClosedEventListener(sessionOnClosed);
        sessionGroup.add(s.getSessionId(), s);
        return s;
    }
    
    protected Session createSession(Channel channel, NetkitSessionListener onCreatedListener, NetkitSessionListener onClosedListener){
        this.sessionOnCreatred = onCreatedListener;
        this.sessionOnClosed = onClosedListener;
        return createSession(channel);
    }
    
    public Collection<Session> getAllSessions(){
        return sessionGroup.sessions();
    }
    
    public void release(){
        sessionGroup.close();
        bossExecutor.shutdownNow();
        workerExecutor.shutdownNow();
    }

    public int getMaxConnetions() {
        return maxConnetions;
    }

    public void setMaxConnetions(int maxConnetions) {
        this.maxConnetions = maxConnetions;
    }
    
    public void setAttribute(String key, Object value){
        attributes.put(key, value);
    }
    
    public Object getAttribute(String key){
        return attributes.get(key);
    }
    
    public Map<String, Object> getAttributes(){
        return attributes;
    }

}
