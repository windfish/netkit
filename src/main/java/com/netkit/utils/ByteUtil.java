package com.netkit.utils;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * 
 * @author xuliang
 * @since 2019年7月3日 上午10:37:08
 *
 */
public class ByteUtil {

    /**
     * 拷贝byte数组
     * 
     * @param src 原始数组
     * @param length 需拷贝的长度
     */
    public static byte[] copyBytes(byte[] src, int length) {
        byte[] dest = new byte[length];
        if (src.length > length) {
            System.arraycopy(src, 0, dest, 0, dest.length);
        } else {
            System.arraycopy(src, 0, dest, 0, src.length);
        }
        return dest;
    }

    /**
     * 拷贝byte数组
     * 
     * @param src 源数组
     * @param dest 目标数组
     */
    public static void copyBytes(byte[] src, byte[] dest) {
        if (src.length > dest.length) {
            System.arraycopy(src, 0, dest, 0, dest.length);
        } else {
            System.arraycopy(src, 0, dest, 0, src.length);
        }
    }

    /**
     * 去除byte[]中的null值（byte值为0的）
     */
    public static byte[] trimByte(byte[] b) {
        if (b == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < b.length; i++) {
            if (b[i] == 0) {
                break;
            }
            outputStream.write(b[i]);
        }
        return outputStream.toByteArray();
    }

    /**
     * byte[]转字符串
     * 
     * @param b byte 数组
     * @param charset 编码
     * @return
     */
    public static String byteToString(byte[] b, Charset charset) {
        return new String(trimByte(b), charset);
    }

    /**
     * 1byte=8bit 1short=16bit byte[]转short， b[0] << 8 左移8位，低位补0，作为short的高八位；
     * b[1] 数组第二位作为short低八位
     */
    public static short getShort(byte[] b) {
        return (short) ((b[0] << 8) + b[1]);
    }

    /**
     * short 转byte[]
     */
    public static byte[] getByteFromShort(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) (s >> 8);
        b[1] = (byte) s;
        return b;
    }

    public static int getInt(byte[] b) {
        if (b.length < 4) {
            return 0;
        }
        return (b[0] & 0xFF | (b[1] & 0xFF) << 8 | (b[2] & 0xFF) << 16 | (b[3] & 0xFF) << 24);
    }

    public static byte[] getByteFromInt(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) (i >> 24);
        b[1] = (byte) (i >> 16);
        b[2] = (byte) (i >> 8);
        b[3] = (byte) i;
        return b;
    }

    public static byte[] getByteFromIntHightBit(int i) {
        byte[] b = new byte[4];
        b[3] = (byte) (i >> 24);
        b[2] = (byte) (i >> 16);
        b[1] = (byte) (i >> 8);
        b[0] = (byte) i;
        return b;
    }

}
