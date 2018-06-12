package com.hanheng.a53.dotarray;

public class DotArrayClass {
	static {
		System.loadLibrary("dotArray-jni");
	}
	
	public static native String stringFromJNI();
	public static native int Init();
	public static native void DotShow(byte[] arr);
	public static native void Test();
	public static native int Exit();
}
