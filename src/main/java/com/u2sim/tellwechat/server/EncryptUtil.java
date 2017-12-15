package com.u2sim.tellwechat.server;


import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by hanguojing on 16/8/25 14:07
 * <p>
 * 和BOSS 之间的aes 加密
 */
public class EncryptUtil {


    public static String Md5(String plainText ) {
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            buf = new StringBuffer("");
            for (byte aB : b) {
                i = aB;
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            buf = null;
        }
        return buf==null?null:buf.toString().substring(8,24).toLowerCase();
    }



    private static final String PASSWORD = "98000000009999999";   // AES 密码
    private static final String IVPARAMEETER = "98000000009999999"; // AES 偏移向量


    public static String aesDecrypt(String data) {
        try {

            byte[] text = base64Decrypt(data);
            byte[] bytes = new byte[16];
            System.arraycopy(PASSWORD.getBytes(), 0, bytes, 0, PASSWORD.getBytes().length);
            SecretKeySpec keySpec = new SecretKeySpec(bytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");// 创建密码器
            IvParameterSpec iv = new IvParameterSpec(IVPARAMEETER.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] result = cipher.doFinal(text);

            return new String(result); // 返回加密结果
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 返回 aes加密，且 base64编码后的结果
     *
     * @param content
     * @return
     */
    public static String aesEncrypt(String content) {
        return aesEncrypt(null, content);
    }

    /**
     * 返回 aes加密，且 base64编码后的结果
     *
     * @param content
     * @return
     */
    public static String aesEncrypt(String key, String content) {
        try {

            byte[] bytes = new byte[16];
            if (TextUtils.isEmpty(key)) {
                System.arraycopy(PASSWORD.getBytes(), 0, bytes, 0, PASSWORD.getBytes().length);
            } else {
                System.arraycopy(key.getBytes(), 0, bytes, 0, key.getBytes().length);
            }
            SecretKeySpec keySpec = new SecretKeySpec(bytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");// 创建密码器
            IvParameterSpec iv = new IvParameterSpec(IVPARAMEETER.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] byteContent = content.getBytes("utf-8");

            byte[] result = cipher.doFinal(byteContent);

            String encrypt = base64Encrypt(result);

            return encrypt; // 返回加密结果
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对AES加密后的字节数给进行 base64编码
     *
     * @param bytes
     * @return
     */
    public static String base64Encrypt(byte[] bytes) {
        byte[] byte_result = Base64.encode(bytes, Base64.NO_WRAP);
        return new String(byte_result);
    }

    /**
     * base64 解码
     *
     * @param text
     * @return
     */
    private static byte[] base64Decrypt(String text) {
        byte[] byte_result = Base64.decode(text, Base64.NO_WRAP);
//        String result = ByteUtil.bytes2String(byte_result);
        return byte_result;
    }

}

