package com.hanheng.a53.led;

/**
 * Created by 王真 on 2018/6/9.
 */

public class LedClass {
    static {
        System.loadLibrary("led-jni");
    }
    public static native String stringFromJNI();
    public static native int Init();
    public static native int IoctlLed(int led_num, int controlcode);
    public static native int Exit();
}
