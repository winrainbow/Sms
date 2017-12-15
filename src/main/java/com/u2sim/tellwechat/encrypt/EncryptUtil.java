package com.u2sim.tellwechat.encrypt;

/**
 * Created by hanguojing on 2017/11/30 19:33
 */

public class EncryptUtil {

    static{
        System.loadLibrary("encrypt-lib");
    }
    public static native String encrypt(String plainText);
    public static native String decrypt(String cipherText);

}
