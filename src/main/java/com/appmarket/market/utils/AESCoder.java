package com.appmarket.market.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Kingson.chan on 2017/2/10.
 * Email:chenjingxiong@yunnex.com.
 */
public class AESCoder {
    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public AESCoder() {
    }

    public static String createKey() {
        byte[] key = initSecretKey();
        return Hex.encodeHexString(key);
    }

    private static byte[] initSecretKey() {
        KeyGenerator kg = null;

        try {
            kg = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            return new byte[0];
        }

        kg.init(128);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    public static Key toKey(byte[] key) {
        return new SecretKeySpec(key, "AES");
    }

    public static byte[] encrypt(byte[] data, Key key) throws GeneralSecurityException {
        return encrypt(data, (Key)key, "AES/ECB/PKCS5Padding");
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws GeneralSecurityException {
        return encrypt(data, (byte[])key, "AES/ECB/PKCS5Padding");
    }

    public static byte[] encrypt(byte[] data, byte[] key, String cipherAlgorithm) throws GeneralSecurityException {
        Key k = toKey(key);
        return encrypt(data, (Key)k, cipherAlgorithm);
    }

    public static byte[] encrypt(byte[] data, Key key, String cipherAlgorithm) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(1, key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt(byte[] data, byte[] key) throws GeneralSecurityException {
        return decrypt(data, (byte[])key, "AES/ECB/PKCS5Padding");
    }

    public static byte[] decrypt(byte[] data, Key key) throws Exception {
        return decrypt(data, (Key)key, "AES/ECB/PKCS5Padding");
    }

    public static byte[] decrypt(byte[] data, byte[] key, String cipherAlgorithm) throws GeneralSecurityException {
        Key k = toKey(key);
        return decrypt(data, (Key)k, cipherAlgorithm);
    }

    public static byte[] decrypt(byte[] data, Key key, String cipherAlgorithm) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(2, key);
        return cipher.doFinal(data);
    }

    private static String showByteArray(byte[] data) {
        if(data == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder("{");
            byte[] var5 = data;
            int var4 = data.length;

            for(int var3 = 0; var3 < var4; ++var3) {
                byte b = var5[var3];
                sb.append(b).append(",");
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append("}");
            return sb.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        byte[] key = initSecretKey();
        System.out.println("key：" + showByteArray(key));
        System.out.println("key：" + Hex.encodeHexString(key));
        System.out.println("key：" + showByteArray(Hex.decodeHex(Hex.encodeHexString(key).toCharArray())));
        key = Hex.decodeHex(Hex.encodeHexString(key).toCharArray());
        Key k = toKey(key);
        String data = "AES数据";
        System.out.println("加密前数据: string:" + data);
        System.out.println("加密前数据: byte[]:" + showByteArray(data.getBytes()));
        System.out.println();
        byte[] encryptData = encrypt(data.getBytes(), (Key)k);
        System.out.println("加密后数据: byte[]:" + showByteArray(encryptData));
        System.out.println("加密后数据: hexStr:" + Hex.encodeHexString(encryptData));
        System.out.println();
        byte[] decryptData = decrypt(encryptData, (Key)k);
        System.out.println("解密后数据: byte[]:" + showByteArray(decryptData));
        System.out.println("解密后数据: string:" + new String(decryptData));
        System.out.println();
    }
}
