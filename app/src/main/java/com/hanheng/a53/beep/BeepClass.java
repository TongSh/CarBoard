package com.hanheng.a53.beep;

/**
 * Created by 王真 on 2018/6/9.
 */

public class BeepClass {
    static {
        System.loadLibrary("beep-jni");
    }

    public static native String stringFromJNI();
    public static native int Init();
    public static native int IoctlRelay(int controlcode);
    public static native int Exit();

}
