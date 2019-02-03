package com.dtm.locationsys.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * 字节数组转换类
 */
public class ByteConvert {

    /**
     * short转byte[]
     * @param s
     * @return
     */
    public static byte[] short2Bytes(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) (s >> 8);
        b[1] = (byte) (s >> 0);

        return b;
    }

    /**
     * byte[]转short
     * @param b
     * @return
     */
    public static short bytes2Short(byte[] b){
        short s = 0;
        // 由高位到低位
        for (int i = 0; i < 2; i++){
            int shift = (2-i-1) * 8;
            s += (b[i] & 0x00ff) << shift;
        }

        return s;
    }

    /**
     * byte[]转Ushort
     * @param b
     * @return
     */
    public static int bytes2Ushort(byte[] b) {
        return b[1] & 0xff | (b[0] & 0xff) << 8;
    }

    /**
     * ushort转为byte[]
     * @param v
     * @return
     */
    public static byte[] ushort2Bytes(int v){
        byte[] r = new byte[4];
        // 有高位到低位
        r[0] = (byte)((v >> 8) & 0xff);
        r[1] = (byte)(v & 0xff);

        return r;
    }

    /**
     * int转为byte[]
     * @param v
     * @return
     */
    public static byte[] int2Bytes(int v){
        byte[] r = new byte[4];
        // 有高位到低位
        r[0] = (byte)((v >> 24) & 0xff);
        r[1] = (byte)((v >> 16) & 0Xff);
        r[2] = (byte)((v >> 8) & 0xff);
        r[3] = (byte)(v & 0xff);

        return r;
    }

    /**
     * byte[]转为int
     * @param v
     * @return
     */
    public static int bytes2Int(byte[] v){
        int r = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++){
            int shift = (4-i-1) * 8;
            r += (v[i] & 0x000000ff) << shift;
        }
        return r;
    }

    /**
     * byte[]转为Unit
     * @param v
     * @return
     */
    public static long bytes2Uint(byte[] v) {
        return ((long) (v[3] & 0xff))
                | ((long) (v[2] & 0xff)) << 8
                | ((long) (v[1] & 0xff)) << 16
                | ((long) (v[0] & 0xff)) << 24;

    }
    /**
     * long转为Unit的byte[]
     * @param v
     * @return
     */
    public static byte[] uint2Bytes(long v){
        byte[] r = new byte[4];
        // 有高位到低位，只处理long的低4字节
        r[0] = (byte)((v >> 24) & 0xff);
        r[1] = (byte)((v >> 16) & 0Xff);
        r[2] = (byte)((v >> 8) & 0xff);
        r[3] = (byte)(v & 0xff);

        return r;
    }

    /**
     * long转为byte[]
     * @param v
     * @return
     */
    public static byte[] long2Bytes(long v){
        byte[] r = new byte[8];
        // 有高位到低位
        r[0] = (byte)((v >> 56) & 0xff);
        r[1] = (byte)((v >> 48) & 0xff);
        r[2] = (byte)((v >> 40) & 0xff);
        r[3] = (byte)((v >> 32) & 0xff);
        r[4] = (byte)((v >> 24) & 0xff);
        r[5] = (byte)((v >> 16) & 0Xff);
        r[6] = (byte)((v >> 8) & 0xff);
        r[7] = (byte)(v & 0xff);

        return r;
    }

    /**
     * byte[]转为long
     * @param v
     * @return
     */
    public static long bytes2Long(byte[] v) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (v[ix] & 0xff);
        }
        return num;
    }

    /**
     * char转为byte[]
     *
     * @param chars
     * @return
     */
    public static byte[] chars2Bytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        return bb.array();

    }

    /**
     * byte[]转为char[]
     *
     * @param bytes
     * @return
     */
    public static char[] bytes2Chars(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);


        return cb.array();
    }

    /**
     * char转为byte[]
     *
     * @param c
     * @return
     */
    public static byte[] char2Bytes(char c) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(1);
        cb.put(c);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);

        return bb.array();
    }
    /**
     * byte[]转为char
     *
     * @param bytes
     * @return
     */
    public static char bytes2Char(byte[] bytes) {
        Charset cs = Charset.forName("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = cs.decode(bb);

        return cb.array()[0];
    }

    /**
     * 将byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] byte2ByteArrBit(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte)(b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

    /**
     * 把byte转为字符串的bit
     */
    public static String byte2Bit(byte b) {
        return ""
                + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * 把byte数组转为字符串的bit
     */
    public static String bytes2Bit(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        byte b;
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 7; j >= 0; j--) {
                b = (byte) ((bytes[i] >> j) & 0x1);
                sb.append(b);
            }
        }

        return sb.toString();
    }

    /**
     *  整型转为char，只限于0-9的整型
     * @param v
     * @return
     */
    public static char int2char(int v) {
        String s = String.valueOf(v);
        char[] chars = s.toCharArray();

        return chars[0];
    }

}
