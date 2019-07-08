package com.netkit.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.Session;
import com.netkit.event.ChannelEvent;
import com.netkit.event.NetkitChannelEventListener;
import com.netkit.utils.ContextUtil;
import com.netkit.utils.Message;

public class ConnectionEventListener implements NetkitChannelEventListener {

	private static final Logger logger = LoggerFactory.getLogger(ConnectionEventListener.class);

	@Override
	public void onClosed(ChannelEvent evt) {
		Session session = ContextUtil.getServerContext().getSession(evt.getChannelID());
		long userId = ContextUtil.getUserIdFromSession(session);
		logger.debug("onClosed - [{}] channelId[{}] userId[{}]", evt.getChannel().getRemoteAddress().toString(), evt.getChannelID(), userId);
	}

	@Override
	public void onConnected(ChannelEvent evt) {
		logger.debug("onConnected [" + evt.getChannel().getRemoteAddress().toString() + "] - " + evt.getChannelID());
	}

	@Override
	public void onDisconnected(ChannelEvent evt) {
		Session session = ContextUtil.getServerContext().getSession(evt.getChannelID());
		long userId = ContextUtil.getUserIdFromSession(session);
		logger.debug("onDisconnected - [{}] channelId[{}] userId[{}]", evt.getChannel().getRemoteAddress().toString(), evt.getChannelID(), userId);
	}

	@Override
	public void onMessageReceived(ChannelEvent evt, Message message) {
		Session session = ContextUtil.getServerContext().getSession(evt.getChannelID());
//		session.setAttribute(Names.Session.KEY_HEARTBEAT_TIME, System.currentTimeMillis());
		if (!Message.isFilterMessageLogCmd(message.getCmd())) {
			long userId = ContextUtil.getUserIdFromSession(session);
			logger.debug("onMessageReceived - [{}] channelId[{}] userId[{}]", evt.getChannel().getRemoteAddress().toString(), evt.getChannelID(), userId);
		}
	}

	@Override
	public void onOpen(ChannelEvent evt) {
		logger.debug("onOpen [" + evt.getChannel().getRemoteAddress().toString() + "] - " + evt.getChannelID());
	}

	@Override
	public void onExceptionCaught(ChannelEvent evt, Throwable e) {
		Session session = ContextUtil.getServerContext().getSession(evt.getChannelID());
		long userId = ContextUtil.getUserIdFromSession(session);
		logger.warn("onExceptionCaught [" + evt.getChannel().getRemoteAddress().toString() + "] " + e.getMessage() +" - " + evt.getChannelID() + "userId[" + userId + "]", e);
	}

}
