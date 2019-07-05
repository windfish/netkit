package com.netkit;

import java.util.Iterator;

import com.netkit.exception.ActionException;
import com.netkit.utils.Message;

/**
 * 业务处理过滤器链
 * @author xuliang
 * @since 2019年7月4日 下午2:02:34
 *
 */
public class ActionChain {

    private Iterator<ActionFilter> it;
    
    public void setInterator(Iterator<ActionFilter> it){
        this.it = it;
    }
    
    public void doChain(Action action, Message message) throws ActionException {
        if(it.hasNext()){
            it.next().doFilter(action, message, this);
            return;
        }
        action.execute(message);
    }
    
}
