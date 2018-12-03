package com.appmarket.market.utils;

import com.appmarket.market.bean.SnKey;
import com.appmarket.market.bean.SnStruct;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by kingson.chan on 2017/5/8.
 * Email:chenjingxiong@yunnex.com.
 */
@Component
public class SnKeyUtil {
    //private static final String SERVER = "192.168.6.238";
    public static  String SERVER = "202.104.117.203";
    //private static final int PORT = 6667;
    public static  int PORT = 16667 ;
    private static final String KEY = "31313131313131313131313131313131";

    public static SnKey getSnKey(String sn){
        try {
            byte[] sendData = writeDataByGetKey(sn);
            byte[] recData =  sendMessage(sendData);
            if(null != recData && recData.length >0){
                return changeDataByHex(recData);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

       return null;
    }

    public static byte[] setSnKeyByByte(String sn,String hwid){
        try {
            byte[] sendData = writeDataBySetKey(sn,hwid);
            byte[] recData =  sendMessage(sendData);
            if(null != recData && recData.length >0){
                return recData;
            }
            return null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SnKey setSnKey(String sn,String hwid){
        try {
            byte[] sendData = writeDataBySetKey(sn,hwid);
            byte[] recData =  sendMessage(sendData);
            if(null != recData && recData.length >0){
                return changeDataByHex(recData);
            }
            return null;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] sendMessage(byte[] msg){
        Socket socket = null;
        try {
            socket = new Socket(SERVER, PORT);
            socket.setSoTimeout(30*1000);
            //给服务端发送信息
            OutputStream os= socket.getOutputStream();
            os.write(msg);
            //接受服务端消息并存储

            InputStream is = socket.getInputStream();
            int messageLength = 80;
            byte[] data = new byte[messageLength];
            if (is.read(data) != messageLength) {
                System.out.println("读取报文数据出错，收到报文：" + byteArray2HexString(data));
                return null;
            }
            System.out.println("接收到报文：" + byteArray2HexString(data));
            return data;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static SnKey changeData(byte[] data){
        SnKey snKey = new SnKey();
        byte[] snByte = subarray(data, 4, 16);
        String snStr = new String(snByte);
        System.out.println("原始sn报文：" + snStr);
        System.out.println("16进制sn报文：" + byteArray2HexString(snByte));
        snKey.setSn(snStr);

        byte[] snumByte = subarray(data, 20, 42);
        String snumStr = new String(snumByte);
        System.out.println("原始snum报文：" + snumStr);
        System.out.println("16进制snum报文：" + byteArray2HexString(snumByte));
        snKey.setSnum(snumStr);

        byte[] keyByte = subarray(data, 62, 16);
        String keyStr = new String(keyByte);
        System.out.println("原始key报文：" + keyStr);
        System.out.println("16进制key报文：" + byteArray2HexString(keyByte));
        snKey.setKey(keyStr);
        return snKey;
    }

    private static SnKey changeDataByHex(byte[] data){
        SnKey snKey = new SnKey();
        byte[] snByte = subarray(data, 4, 16);
        String snStr = new String(snByte);
        System.out.println("原始sn报文：" + snStr);
        System.out.println("16进制sn报文：" + byteArray2HexString(snByte));
        snKey.setSn(byteArray2HexString(snByte));

        byte[] snumByte = subarray(data, 20, 42);
        String snumStr = new String(snumByte);
        System.out.println("原始snum报文：" + snumStr);
        System.out.println("16进制snum报文：" + byteArray2HexString(snumByte));
        snKey.setSnum(byteArray2HexString(snumByte));

        byte[] keyByte = subarray(data, 62, 16);
        String keyStr = new String(keyByte);
        System.out.println("原始key报文：" + keyStr);
        System.out.println("16进制key报文：" + byteArray2HexString(keyByte));
        snKey.setKey(byteArray2HexString(keyByte));
        return snKey;
    }


    private static byte[] writeDataByGetKey(String sn) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SnStruct snStruct =new SnStruct();
        snStruct.setSTX("02");
        snStruct.setPATH("e2");
        snStruct.setETX("03");
        snStruct.setCONT(sn);
        snStruct.setLEN(sn.getBytes().length/2);
        String len = Integer.toHexString(snStruct.getLEN());
        byte[] hexLen= hexString2ByteArray(len);


        ByteArrayOutputStream baosLrc = new ByteArrayOutputStream();
        baosLrc.write(hexString2ByteArray(snStruct.getPATH()));
        baosLrc.write(hexString2ByteArray("00"));
        baosLrc.write(hexLen);
        baosLrc.write(hexString2ByteArray(sn));
        baosLrc.write(hexString2ByteArray(snStruct.getETX()));
        snStruct.setLRC(LRC(baosLrc.toByteArray()));

        baos.write(hexString2ByteArray(snStruct.getSTX()));
        baos.write(hexString2ByteArray(snStruct.getPATH()));
        baos.write(hexString2ByteArray("00"));
        baos.write(hexLen);
        baos.write(hexString2ByteArray(sn));
        baos.write(hexString2ByteArray(snStruct.getETX()));
        byte[] lrcByte = new byte[1];
        lrcByte[0] = snStruct.getLRC();
        baos.write(lrcByte);

        System.out.println("发送的报文：" + byteArray2HexString(baos.toByteArray()));

        return baos.toByteArray();
    }

    private static byte[] writeDataBySetKey(String sn,String hwid) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SnStruct snStruct =new SnStruct();
        snStruct.setSTX("02");
        snStruct.setPATH("e1");
        snStruct.setETX("03");
        snStruct.setCONT(sn+hwid);
        snStruct.setLEN((sn.getBytes().length+hwid.getBytes().length)/2);
        
        String len = Integer.toHexString(snStruct.getLEN());
        byte[] hexLen= hexString2ByteArray(len);
        ByteArrayOutputStream baosLrc = new ByteArrayOutputStream();
        baosLrc.write(hexString2ByteArray(snStruct.getPATH()));
        baosLrc.write(hexString2ByteArray("00"));
        baosLrc.write(hexLen);
        baosLrc.write(hexString2ByteArray(sn));
        baosLrc.write(hexString2ByteArray(hwid));
        baosLrc.write(hexString2ByteArray(snStruct.getETX()));
        snStruct.setLRC(LRC(baosLrc.toByteArray()));

        baos.write(hexString2ByteArray(snStruct.getSTX()));
        baos.write(hexString2ByteArray(snStruct.getPATH()));
        baos.write(hexString2ByteArray("00"));
        baos.write(hexLen);
        baos.write(hexString2ByteArray(sn));
        baos.write(hexString2ByteArray(hwid));
        baos.write(hexString2ByteArray(snStruct.getETX()));
        byte[] lrcByte = new byte[1];
        lrcByte[0] = snStruct.getLRC();
        baos.write(lrcByte);

        System.out.println("发送的报文：" + byteArray2HexString(baos.toByteArray()));

        return baos.toByteArray();
    }


    /**
     * "33d20046" 转换为 0x33 0xD2 0x00 0x46
     *
     * @param hexString
     * @return
     */
    public static byte[] hexString2ByteArray(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        byte[] hanzi = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2)
            hanzi[i / 2] = (byte) (Integer.parseInt(hexString.substring(i, i + 2), 16) & 0xff);
        return hanzi;
    }

    /**
     * 16进制字符串与字节数组相互转换 0x33 0xD2 0x00 0x46 转换为 "33d20046" 转换和打印报文用
     *
     * @param bytes
     * @return
     */
    public static String byteArray2HexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println("设置密钥接口，处理完毕返回值：" + setSnKey("30303030303030303030303030303031",
                "313032303530363030303030303030303030303030303030303030303030303030303030303030303030").toString());
        
        System.out.println("获取密钥接口，处理完毕返回值：" + getSnKey("30303030303030303030303030303031").toString());
    }

    public static byte LRC(byte[] msg) {
        byte uchLRC = 0;
        for (byte item : msg) {
            uchLRC ^= item;
        }
        return uchLRC;
    }

    public static byte[] subarray(byte[] source, int offset, int length) {
        if(source.length < offset + length) {
            length = source.length - offset;
        }
        byte[] buf = new byte[length];
        //数组之间复制函数： arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
        //src:源数组； srcPos:源数组要复制的起始位置； dest:目的数组； destPos:目的数组放置的起始位置； length:复制的长度
        System.arraycopy(source, offset, buf, 0, length);
        return buf;
    }

}
