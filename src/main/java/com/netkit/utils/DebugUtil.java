package com.netkit.utils;

/**
 * 
 * @author xuliang
 * @since 2019年7月3日 下午1:54:52
 *
 */
public class DebugUtil {

    private static final String HEX = "0123456789ABCDEF";
    
    public static String toHex(byte[] buf, char c){
        if(buf == null){
            return "";
        }
        
        StringBuffer result = new StringBuffer(3 * buf.length);
        for(int i=0;i<buf.length-1;++i){
            appendHex(result, buf[i], c);
        }
        result.append(HEX.charAt(buf[buf.length-1] >> 4 & 0xF)).append(HEX.charAt(buf[buf.length-1] & 0xF));
        return result.toString();
    }
    
    private static void appendHex(StringBuffer sb, byte b, char c){
        sb.append(HEX.charAt(b >> 4 & 0xF)).append(HEX.charAt(b & 0xF)).append(c);
    }
    
    public static String toHex(byte b){
        return String.valueOf(HEX.charAt(b >> 4 & 0xF) + HEX.charAt(b & 0xF));
    }
    
}
