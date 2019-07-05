package com.netkit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.exception.ActionException;
import com.netkit.utils.Message;

/**
 * 消息调度器
 * @author xuliang
 * @since 2019年7月4日 上午10:19:43
 *
 */
public class MessageDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);
    private final Map<Integer, Action> actionCache = new HashMap<Integer, Action>();
    private ActionChain chain = new ActionChain();
    
    public void dispatch(Session session, Message message){
        dispatch(session.getContext(), session, message);
    }
    
    /**
     * 根据消息的cmd，获取具体的action 和actionFilter，然后依次执行filterChain 和具体的action
     */
    public void dispatch(NetkitContext context, Session session, Message message) {
        int cmd = message.getCmd();
        try{
            Action action = getAction(context, cmd);
            if(action == null){
                LOGGER.debug(" - cmd["+cmd+"] action is null");
            }
            action.setSession(session);
            Iterator<ActionFilter> it = null;
            // 从context 中获取ActionFilter 列表
            List<ActionFilter> filters = context.getActionFilters();
            if(filters != null){
                it = filters.iterator();
            }
            
            executeAction(it, action, message);
        }catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
    
    private synchronized void setCachedAction(int cmd, Action action){
        this.actionCache.put(cmd, action);
    }
    
    private Action getCachedAction(int cmd){
        return this.actionCache.get(cmd);
    }
    
    /**
     * 根据消息的cmd，查找具体的action
     */
    private Action getAction(NetkitContext context, int cmd) throws InstantiationException, IllegalAccessException{
        Action action = getCachedAction(cmd);
        if(action != null){
            return action;
        }
        // 反射生成action
        Class<Action> clazz = context.lookupRegisteredAction(cmd);
        if(clazz == null){
            return null;
        }
        action = clazz.newInstance();
        if(action != null){
            setCachedAction(cmd, action);
        }
        
        return action;
    }
    
    private synchronized void executeAction(Iterator<ActionFilter> it, Action action, Message message) throws ActionException{
        this.chain.setInterator(it);
        this.chain.doChain(action, message);
    }
    
}
