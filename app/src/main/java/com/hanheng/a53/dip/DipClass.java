package com.hanheng.a53.dip;

/**
 * Created by 王真 on 2018/6/9.
 */

public class DipClass {
    static {
        System.loadLibrary("dip-jni");
    }
    public static native String stringFromJNI();
    public static native int Init();
    public static native int ReadValue();
    public static native int Exit();
}
