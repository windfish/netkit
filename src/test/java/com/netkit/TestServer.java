package com.netkit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.action.HeartBeatAction;
import com.netkit.action.LoginAction;
import com.netkit.filter.SessionFilter;
import com.netkit.listener.ConnectionEventListener;
import com.netkit.listener.SessionClosedEventListener;
import com.netkit.listener.SessionCreatedEventListener;
import com.netkit.utils.ContextUtil;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午5:51:01
 *
 */
public class TestServer {

    private static Logger logger = LoggerFactory.getLogger(TestServer.class);
    
    private static final NetkitContext context = ContextUtil.getServerContext();
    
    public static void main(String[] args) {
        context.addActionFilter(new SessionFilter());   // action 过滤器
        registerAction(context);    // 注册业务 action
        context.setChannelEventListener(new ConnectionEventListener());
        context.setSessionOnCreatedEventListener(new SessionCreatedEventListener());
        context.setSessionOnClosedEventListener(new SessionClosedEventListener());
        
        String host = "127.0.0.1";
        int port = 30000;
        NetkitServer server = new NetkitServer(context, host, port);
        server.setMaxConnections(100000); // 最大的连接数
        server.startup();
        
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    System.out.println("session size: " + ContextUtil.getSessions().size());
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
    
    private static void registerAction(NetkitContext context){
        logger.info("--- register action.");
        context.registerAction(ServerMessageID.HEART_BEAT, HeartBeatAction.class);
        context.registerAction(ServerMessageID.LOGIN, LoginAction.class);
    }
    
}
