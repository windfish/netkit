package com.netkit.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.Session;
import com.netkit.event.NetkitSessionListener;

public class SessionCreatedEventListener implements NetkitSessionListener {

	private static final Logger logger = LoggerFactory.getLogger(SessionCreatedEventListener.class);

	@Override
	public void onEvent(Session session) {
		logger.debug("Session-Session[" + session.getSessionId() + "] created -" + session.getRemoteAddress().toString());
	}

}
