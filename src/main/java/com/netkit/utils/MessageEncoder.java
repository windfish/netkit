package com.netkit.utils;

import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;

public class MessageEncoder {
    
    public static byte[] encode(Message message) {
        ByteBuffer buffer = ByteBuffer.allocate(message.getPacketSize());
        encode(buffer, message);
        byte[] bytes = buffer.array();
        buffer.clear();
        return bytes;
    }

    public static void encode(ByteBuffer buffer, Message message) {
        byte[] header = message.getHeaderByte();
        byte[] body = message.getBody();
        byte verify = 0;
        for (int i = 0; i < header.length; ++i) {
            verify = (byte) (verify ^ header[i]);
        }
        buffer.put(header);
        if (body != null) {
            buffer.put(body);

            for (int i = 0; i < body.length; ++i) {
                verify = (byte) (verify ^ body[i]);
            }
        }
        verify = (byte) (verify ^ message.getTail());
        buffer.put(message.getTail());
        buffer.put(verify);
    }

    public static void encode(ChannelBuffer buffer, Message message) {
        byte[] header = message.getHeaderByte();
        byte[] body = message.getBody();
        byte verify = 0;
        for (int i = 0; i < header.length; ++i) {
            verify = (byte) (verify ^ header[i]);
        }
        buffer.writeBytes(header);
        if (body != null) {
            buffer.writeBytes(body);

            for (int i = 0; i < body.length; ++i) {
                verify = (byte) (verify ^ body[i]);
            }
        }
        verify = (byte) (verify ^ message.getTail());
        buffer.writeByte(message.getTail());
        buffer.writeByte(verify);
    }
    
}
