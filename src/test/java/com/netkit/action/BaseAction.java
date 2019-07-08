package com.netkit.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.Action;
import com.netkit.ServerMessageID;
import com.netkit.exception.ActionException;
import com.netkit.utils.ContextUtil;
import com.netkit.utils.Message;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午5:04:00
 *
 */
public abstract class BaseAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);

    protected Message message;
    
    @Override
    public void execute(Message message) throws ActionException {
        if(message == null){
            return;
        }
        logger.info("baseAction start. message:{} id:{}", message.getCmd(), message.getId());
        
        this.message = message;
        String cmdName = ServerMessageID.serverMessageID_NAME.get(this.message.getCmd());
        if(!Message.isFilterMessageLogCmd(this.message.getCmd())){
            logger.debug("-- start  exexute action userId[{}] cmd[{}]", ContextUtil.getUserIdFromSession(getSession()), cmdName);
        }
        
        long startTime = System.currentTimeMillis();
        
        execute();
        
        long endTime = System.currentTimeMillis();
        long cost = endTime - startTime;
        
        if (!Message.isFilterMessageLogCmd(this.message.getCmd())) {
            logger.debug("--- end  execute action userId[{}] cmd[{}] cost[{}ms]", 
                    ContextUtil.getUserIdFromSession(getSession()), cmdName, cost);
        }
        if (cost > 1000) {
            logger.error("--- end  execute action userId[{}] cmd[{}] cost[{}ms]", 
                    ContextUtil.getUserIdFromSession(getSession()), cmdName, cost);
        }
    }
    
    public abstract void execute() throws ActionException;
    
    protected void sendMessage(Message message){
        if(message != null){
            getSession().sendMessage(message);
        }
    }
    
    protected String getClientIp(){
        return getSession().getRemoteAddress().toString();
    }

}
