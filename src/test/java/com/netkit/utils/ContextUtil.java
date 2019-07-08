package com.netkit.utils;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.NetkitContext;
import com.netkit.ServerMessageID;
import com.netkit.Session;
import com.netkit.SessionGroup;

public class ContextUtil {

    private static Logger logger = LoggerFactory.getLogger(ContextUtil.class);
    
    private static final NetkitContext context = new NetkitContext();
    
    public static NetkitContext getServerContext() {
        return context;
    }
    
    public static Session getSessionBySessionID(int sessionID) {
        return context.getSession(sessionID);
    }
    
    public static long getUserIdFromSession(Session session) {
        long val = 0;
        if (session != null) {
            Object o = session.getAttribute(ServerMessageID.SessionAttributes.KEY_USER_ID);
            if (o != null) {
                val = Long.valueOf(o.toString());
            }
        }
        return val;
    }
    
    private static final SessionGroup sessionGroup = new SessionGroup();
    
    public static Session removeSessionByUserId(long userId, int sessionId) {       
        Session oldSession = getSessionByUserId(userId);
        if (oldSession != null && oldSession.getSessionId() == sessionId) {
            return sessionGroup.remove(userId);
        }
        return null;
    }
    
    public static Session getSessionByUserId(long userId) {
        return sessionGroup.get(userId);
    }

    public static void addUser2SessionGroup(long userId, Session session) {
        Session oldSession = sessionGroup.remove(userId);
        if (oldSession != null && oldSession.getSessionId() != session.getSessionId()) {
            logger.debug("xxxxxxxxxx-------session---:userId:{} is close", userId);
        }
        sessionGroup.add(userId, session);
    }

    public static Collection<Session> getSessions() {
        return sessionGroup.sessions();
    }
    
    /**
     * 广播消息
     * @param message
     */
    public static void broadcast(Message message) {
        Collection<Session> sessions = ContextUtil.getServerContext().getAllSessions();
        if (sessions != null) {
            for (Session session : sessions) {
                session.sendMessage(message);
            }
        }
    }
    
}
