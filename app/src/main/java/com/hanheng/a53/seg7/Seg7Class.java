package com.hanheng.a53.seg7;

public class Seg7Class {
	static {
		System.loadLibrary("seg7-jni");
	}
	
	public static native String stringFromJNI();
	public static native int Init();
	public static native void Seg7Show(float f);
	public static native int Exit();
}
