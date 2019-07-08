package com.netkit.utils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author xuliang
 * @since 2019年7月5日 下午5:42:43
 *
 */
public class MessageUtil {

    public static byte[] getTimeByte() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String s = df.format(new Date());
        String[] sa = s.split("-");
        
        String h0 = sa[0].substring(0, 2);
        String h1 = sa[0].substring(2);
        String h2 = sa[1];
        String h3 = sa[2];
        String h4 = sa[3];
        String h5 = sa[4];
        String h6 = sa[5];
        
        byte[] time = new byte[7];
        time[0] = (byte) Integer.parseInt(h0, 16);
        time[1] = (byte) Integer.parseInt(h1, 16);
        time[2] = (byte) Integer.parseInt(h2, 16);
        time[3] = (byte) Integer.parseInt(h3, 16);
        time[4] = (byte) Integer.parseInt(h4, 16);
        time[5] = (byte) Integer.parseInt(h5, 16);
        time[6] = (byte) Integer.parseInt(h6, 16);
        
        return time;
    }
    
    public static Message getMessage(int cmd, byte[] data) {
        Message message = new Message();
        message.setTime(getTimeByte());
        message.setCmd(cmd);
        message.setBody(data);
        return message;
    }
    
    public static int getIntFrom4Byte(byte[] body, int start) {
        int value = 0;
        value =  (body[start] & 0x000000ff) 
         | ((body[start + 1] & 0x000000ff) << 8)
         | ((body[start + 2] & 0x000000ff) << 16)
         | ((body[start + 3] & 0x000000ff) << 24);
        return value;
    }
    
    public static String getStringFromByte(byte[] body, int start, int length) {
        String value = "";
        byte[] valueByte = new byte[length];
        System.arraycopy(body, start, valueByte, 0, valueByte.length);
        value = ByteUtil.byteToString(valueByte, Charset.forName("utf-8"));
        return value;
    }
    
}
