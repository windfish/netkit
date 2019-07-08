package com.netkit;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.utils.MessageUtil;

public class TestClient {

    private static Logger logger = LoggerFactory.getLogger(TestClient.class);
    
    private static NetkitContext context = new NetkitContext();
    
    public static void main(String[] args) throws Exception {
        NetkitClient client = new NetkitClient("127.0.0.1", 30000, context);
        
        long userId = 1000;
        for(int i=0;i<5;i++){
            client(client, userId++);
        }
        
    }
    
    private static void client(NetkitClient client, long userId) throws Exception{
        Session session = client.createSession();
        session.setAttribute(ServerMessageID.SessionAttributes.KEY_USER_ID, userId);
        
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while(true){
                    session.sendMessage(MessageUtil.getMessage(ServerMessageID.HEART_BEAT, null));
                    
                    try{
                        TimeUnit.SECONDS.sleep(random(10, 20));
                    }catch (Exception e) {
                        logger.error("heart beat exception", e);
                    }
                }
            }
        };
        new Thread(r).start();
        
        Future login = session.sendMessage(MessageUtil.getMessage(ServerMessageID.LOGIN, (""+userId).getBytes("utf-8")));
        login.awaitUninterruptibly();
        logger.info("client userId:{} login:{} result:{}", userId, login.isSuccess(), login.getSession().getLastCommunicationTime());
    }
    
    private static Random r = new Random();
    private static int random(int begin, int end){
        if(begin > end){
            return 1;
        }
        if(begin == end){
            return begin;
        }
        return r.nextInt(end - begin) + begin;
    }
    
}
