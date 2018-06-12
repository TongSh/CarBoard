package com.hanheng.a53.relay;

public class RelayClass {
	static {
		System.loadLibrary("relay-jni");
	}
	
	public static native String stringFromJNI();
//	初始化
	public static native int Init();
//	Io控制
	public static native int IoctlRelay(int relay_num, int controlcode);
//	退出
	public static native int Exit();
}
