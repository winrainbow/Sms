#include <jni.h>
extern "C"
JNIEXPORT jstring

JNICALL
Java_com_u2sim_tellwechat_encrypt_EncryptUtil_encrypt(JNIEnv *env, jobject instance,
                                                      jstring plainText_) {
    const char *plainText = env->GetStringUTFChars(plainText_,0);

    // TODO
    /*jstring returnValue;

    env->ReleaseStringUTFChars(returnValue,plainText);*/

    return env->NewStringUTF(plainText);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_u2sim_tellwechat_encrypt_EncryptUtil_decrypt(JNIEnv *env, jobject instance,
                                                      jstring cipherText_) {
    const char *cipherText = env->GetStringUTFChars(cipherText_, 0);

    // TODO

    env->ReleaseStringUTFChars(cipherText_, cipherText);

    return cipherText_;
}