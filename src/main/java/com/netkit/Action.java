package com.netkit;

import com.netkit.exception.ActionException;
import com.netkit.utils.Message;

/**
 * 基础业务事件处理类，具体的业务逻辑处理需继承该类
 * @author xuliang
 * @since 2019年7月4日 下午1:55:07
 *
 */
public abstract class Action {

    private Session session;
    
    public abstract void execute(Message message) throws ActionException;
    
    public Session getSession(){
        return this.session;
    }
    
    protected void setSession(Session session){
        this.session = session;
    }
    
}
