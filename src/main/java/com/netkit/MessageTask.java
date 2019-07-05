package com.netkit;

import com.netkit.utils.Message;

/**
 * 消息处理异步任务
 * @author xuliang
 * @since 2019年7月4日 下午1:39:40
 *
 */
public class MessageTask implements Runnable {

    private MessageDispatcher dispatcher;
    private Session session;
    private Message message;
    
    public MessageTask(MessageDispatcher dispatcher, Session session, Message message) {
        super();
        this.dispatcher = dispatcher;
        this.session = session;
        this.message = message;
    }

    public void run() {
        execute();
    }
    
    public void execute(){
        synchronized(this.dispatcher){
            dispatcher.dispatch(session, message);
        }
    }
    
    public static MessageTask newInstance(MessageDispatcher dispatcher, Session session, Message message){
        return new MessageTask(dispatcher, session, message);
    }

}
