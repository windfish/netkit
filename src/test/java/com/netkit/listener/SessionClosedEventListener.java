package com.netkit.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.Session;
import com.netkit.event.NetkitSessionListener;
import com.netkit.utils.ContextUtil;

public class SessionClosedEventListener implements NetkitSessionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionClosedEventListener.class);
	
	@Override
	public void onEvent(Session session) {
		long userId = ContextUtil.getUserIdFromSession(session);
		logger.info("--session SESSION close userId[{}] sessionid[{}]", userId, session.getSessionId());
		ContextUtil.removeSessionByUserId(userId, session.getSessionId());
	}

}
