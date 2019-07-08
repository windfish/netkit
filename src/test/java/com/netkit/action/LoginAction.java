package com.netkit.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.ServerMessageID;
import com.netkit.exception.ActionException;
import com.netkit.utils.ContextUtil;
import com.netkit.utils.MessageUtil;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午5:43:28
 *
 */
public class LoginAction extends BaseAction {

    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Override
    public void execute() throws ActionException {
        byte[] body = message.getBody();
        int userId = MessageUtil.getIntFrom4Byte(body, 0);
        logger.info("login userId[{}]", userId);
        
        getSession().setAttribute(ServerMessageID.SessionAttributes.KEY_USER_ID, userId);
        ContextUtil.addUser2SessionGroup(userId, getSession());
        
        sendMessage(MessageUtil.getMessage(ServerMessageID.LOGIN, "1".getBytes()));
    }

}
