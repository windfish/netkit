package com.netkit.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xuliang
 * @since 2019年7月3日 上午10:24:23
 *
 */
public class Message {

    private int id;
    private byte[] time;
    private int cmd;
    private byte[] body;
    private byte verify;

    public static boolean IS_DEBUG_MESSAGE_LOG = true;
    private static Map<Integer, Boolean> FILTER_MESSAGE_LOG_CMD_MAP = new HashMap<Integer, Boolean>();

    public static void addFilterMessageLogCmd(int cmd) {
        FILTER_MESSAGE_LOG_CMD_MAP.put(cmd, Boolean.TRUE);
    }

    public static boolean isFilterMessageLogCmd(int cmd) {
        return FILTER_MESSAGE_LOG_CMD_MAP.containsKey(cmd);
    }

    public static boolean isPrintLog(int cmd) {
        return IS_DEBUG_MESSAGE_LOG && !isFilterMessageLogCmd(cmd);
    }

    private byte[] head = { -17, -2, -17, -2 };
    private byte[] type = new byte[3];
    private byte tail = -52;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("cmd[").append(this.cmd).append("]");
        sb.append(" cmdHex[").append(Integer.toHexString(this.cmd)).append("]");
        sb.append(" id[").append(this.id).append("]");
        sb.append(" body[").append(DebugUtil.toHex(this.body, ' ')).append("]");
        return sb.toString();
    }

    public Message() {
    }

    public Message(int cmd) {
        this.cmd = cmd;
    }

    public Message(int id, byte[] time, int cmd, byte[] body, byte tail, byte verify) {
        this.id = id;
        this.time = time;
        this.cmd = cmd;
        this.body = body;
        this.tail = tail;
        this.verify = verify;
    }

    public Message(byte[] head, byte[] type, int id, byte[] time, int cmd, byte[] body, byte tail, byte verify) {
        this.head = head;
        this.type = type;
        this.id = id;
        this.time = time;
        this.cmd = cmd;
        this.body = body;
        this.tail = tail;
        this.verify = verify;
    }

    public byte[] getHead() {
        return this.head;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    public int getPacketSize() {
        return (24 + getLen() + 2);
    }

    public int getLen() {
        if (this.body == null) {
            return 0;
        }
        return this.body.length;
    }

    public byte[] getType() {
        return this.type;
    }

    public void setType(byte[] type) {
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getTime() {
        return this.time;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }

    public int getCmd() {
        return this.cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public byte[] getBody() {
        return this.body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte getTail() {
        return this.tail;
    }

    public void setTail(byte tail) {
        this.tail = tail;
    }

    public byte getVerify() {
        return this.verify;
    }

    public void setVerify(byte verify) {
        this.verify = verify;
    }

    public byte genVerify() {
        return this.verify;
    }
    
    /**
     * header 组成：head（长度4） + body length（长度4） + type（长度3） + id（长度4） + time（长度7） + cmd（长度2）
     */
    public byte[] getHeaderByte(){
        byte[] header = new byte[24];
        System.arraycopy(this.head, 0, header, 0, this.head.length);
        
        int len = getLen();
        byte[] lenByte = ByteUtil.getByteFromIntHightBit(len);
        System.arraycopy(lenByte, 0, header, 4, lenByte.length);
        
        System.arraycopy(this.type, 0, header, 8, this.type.length);
        
        System.arraycopy(ByteUtil.getByteFromIntHightBit(getId()), 0, header, 11, 4);
        
        System.arraycopy(this.time, 0, header, 15, this.time.length);
        
        System.arraycopy(ByteUtil.getByteFromShort((short)this.cmd), 0, header, 22, 2);
        
        return header;
    }

}
