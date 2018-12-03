package com.appmarket.market.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public class SecurityHelper {
    public SecurityHelper() {
    }

    public static byte[] encryptDES(byte[] data, byte[] key) {
        byte[] encryptedData = null;

        try {
            DESKeySpec e = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey sk = keyFactory.generateSecret(e);
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(1, sk);
            encryptedData = cipher.doFinal(data);
        } catch (InvalidKeyException var7) {
            var7.printStackTrace();
        } catch (InvalidKeySpecException var8) {
            var8.printStackTrace();
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
        } catch (NoSuchPaddingException var10) {
            var10.printStackTrace();
        } catch (IllegalBlockSizeException var11) {
            var11.printStackTrace();
        } catch (BadPaddingException var12) {
            var12.printStackTrace();
        }

        return encryptedData;
    }

    public static byte[] decryptDES(byte[] data, byte[] key) {
        byte[] decryptedData = null;

        try {
            DESKeySpec e = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey sk = keyFactory.generateSecret(e);
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(2, sk);
            decryptedData = cipher.doFinal(data);
        } catch (InvalidKeyException var7) {
            var7.printStackTrace();
        } catch (InvalidKeySpecException var8) {
            var8.printStackTrace();
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
        } catch (NoSuchPaddingException var10) {
            var10.printStackTrace();
        } catch (IllegalBlockSizeException var11) {
            var11.printStackTrace();
        } catch (BadPaddingException var12) {
            var12.printStackTrace();
        }

        return decryptedData;
    }

    public static byte[] encrypt3DES(byte[] data, byte[] key) {
        Object result = null;
        byte[] key1 = new byte[8];
        byte[] key2 = new byte[8];
        System.arraycopy(key, 0, key1, 0, 8);
        System.arraycopy(key, 8, key2, 0, 8);
        byte[] result1 = encryptDES(data, key1);
        result1 = decryptDES(result1, key2);
        result1 = encryptDES(result1, key1);
        return result1;
    }

    public static byte[] decrypt3DES(byte[] data, byte[] key) {
        Object result = null;
        byte[] key1 = new byte[8];
        byte[] key2 = new byte[8];
        System.arraycopy(key, 0, key1, 0, 8);
        System.arraycopy(key, 8, key2, 0, 8);
        byte[] result1 = decryptDES(data, key1);
        result1 = encryptDES(result1, key2);
        result1 = decryptDES(result1, key1);
        return result1;
    }

    public static byte[] encryptAES(byte[] data, byte[] key) {
        try {
            SecretKeySpec e = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/Nopadding");
            cipher.init(1, e);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        } catch (NoSuchPaddingException var5) {
            var5.printStackTrace();
        } catch (InvalidKeyException var6) {
            var6.printStackTrace();
        } catch (IllegalBlockSizeException var7) {
            var7.printStackTrace();
        } catch (BadPaddingException var8) {
            var8.printStackTrace();
        }

        return null;
    }

    public static byte[] decryptAES(byte[] data, byte[] key) {
        try {
            SecretKeySpec e = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/Nopadding");
            cipher.init(2, e);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException var4) {
            var4.printStackTrace();
        } catch (NoSuchPaddingException var5) {
            var5.printStackTrace();
        } catch (InvalidKeyException var6) {
            var6.printStackTrace();
        } catch (IllegalBlockSizeException var7) {
            var7.printStackTrace();
        } catch (BadPaddingException var8) {
            var8.printStackTrace();
        }

        return null;
    }

    public static byte[] md5Encrypt(byte[] data) {
        try {
            MessageDigest e1 = MessageDigest.getInstance("MD5");
            e1.update(data);
            return e1.digest();
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static String getMd5Hex(byte[] data) {
        String md5;
        for(md5 = HexStr.bufferToHex(md5Encrypt(data)); md5.length() < 32; md5 = "0" + md5) {
            ;
        }

        return md5;
    }

    public static String digestHexUTF8(String clearText) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digestText = md.digest(clearText.getBytes("UTF-8"));
        byte[] bytes = HexStr.encode(digestText);
        return new String(bytes);
    }

    public static String decodeByAes(String key, String data) throws Exception {
        key = "4824a32081415369c2770a6950e10c1f";
        byte[] keyByte = Hex.decodeHex(key.toCharArray());
        Key k = AESCoder.toKey(keyByte);
        byte[] decryptData = AESCoder.decrypt(Base64.decodeBase64(data), k);
        return new String(decryptData);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("MD5加密:" + getMd5Hex("orangePayKey2016".getBytes()).toUpperCase());
    }
}
