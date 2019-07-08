package com.netkit;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netkit.utils.ByteUtil;
import com.netkit.utils.DebugUtil;
import com.netkit.utils.Message;

/**
 * 解码器
 * @author xuliang
 * @since 2019年7月5日 上午10:03:54
 *
 */
public class NetkitProtocolDecoder extends FrameDecoder {

    private static Logger LOGGER = LoggerFactory.getLogger(NetkitProtocolDecoder.class);
    
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        // Message 头数据长度为24
        if(buffer.readableBytes() < Message.getHeaderSize()){
            LOGGER.error("buffer.readableBytes < Message.HEADER_SIZE");
        }
        
        buffer.markReaderIndex();
        // header 组成：head（长度4） + body length（长度4） + type（长度3） + id（长度4） + time（长度7） + cmd（长度2）
        try{
            // 读取header 信息
            byte[] head = new byte[4];
            buffer.readBytes(head);
            
            int len = 0;
            byte[] length = new byte[4];
            buffer.readBytes(length);
            len = ByteUtil.getInt(length);
            
            byte[] type = new byte[3];
            buffer.readBytes(type);
            
            int id = 0;
            byte[] idByte = new byte[4];
            buffer.readBytes(idByte);
            id = ByteUtil.getInt(idByte);
            
            byte[] time = new byte[7];
            buffer.readBytes(time);
            
            int cmd = buffer.readShort();
            
            if(buffer.readableBytes() < len + 2){
                LOGGER.error("buffer.readableBytes() < contentLen");
                buffer.resetReaderIndex();
                return null;
            }
            
            byte[] body = null;
            if(len > 0){
                body = new byte[len];
                buffer.readBytes(body);
            }
            
            byte tail = buffer.readByte();
            byte verify = buffer.readByte();
            
            Message message = new Message(id, time, cmd, body, tail, verify);
            message.setType(type);
            
            if(Message.isPrintLog(message.getCmd())){
                LOGGER.debug("socket message decoder: [{}] - [{}] - cmd[{}] - len[{}] - tail[{}] - verify[{}] - header[{}] - body[{}]",
                                channel.getRemoteAddress().toString(), channel.getId(), message.getCmd(), message.getLen(),
                                DebugUtil.toHex(message.getTail()), DebugUtil.toHex(message.getVerify()), 
                                DebugUtil.toHex(message.getHeaderByte(), ' '), DebugUtil.toHex(message.getBody(), ' '));
            }
            
            return message;
            
        }catch (Exception e) {
            buffer.resetReaderIndex();
            LOGGER.error("socket message decoder: ["+channel.getRemoteAddress().toString()+"] - ["+channel.getId()+"] - ["+e.getMessage()+"]", e);
        }
        
        return null;
    }

}
