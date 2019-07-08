package com.netkit.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.Action;
import com.netkit.ActionChain;
import com.netkit.ActionFilter;
import com.netkit.Session;
import com.netkit.exception.ActionException;
import com.netkit.utils.Message;

public class SessionFilter implements ActionFilter {

	private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);
	
	public void doFilter(Action action, Message message, ActionChain actionChain) throws ActionException {
		Session session = action.getSession();
		try {
//			session.setAttribute(Names.Session.KEY_HEARTBEAT_TIME, System.currentTimeMillis());

		    logger.info("do session filter, action[{}] message[{}]", action.getSession().getSessionId(), message.getCmd());
			actionChain.doChain(action, message);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("SessionFilterException [" + session.getSessionId() + "]", e);
			if (session.isClosed()) {
				return;
			}
		}
	}

}
