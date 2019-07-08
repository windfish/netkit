package com.netkit;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午4:59:32
 *
 */
public class ServerMessageID {

    public static final int HEART_BEAT = 0x00001000;     // 心跳
    public static final int LOGIN = 0x00001001;         // 登录
    
    public static Map<Integer, String> serverMessageID_NAME = new HashMap<Integer, String>();
    static{
        serverMessageID_NAME.put(HEART_BEAT, "HEART_BEAT");
        serverMessageID_NAME.put(LOGIN, "LOGIN");
    }
    
    public static class SessionAttributes {
        
        public static final String KEY_USER_ID = "USER_ID";
        
    }
    
}
