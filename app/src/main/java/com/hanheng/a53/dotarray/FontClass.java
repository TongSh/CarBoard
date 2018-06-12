package com.hanheng.a53.dotarray;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.regex.Pattern;

import android.content.res.AssetManager;


public class FontClass implements Runnable{
	private byte[][] fontCodes;
	private String content;
	private int showTime;
	
	private static FontClass instance = new FontClass();
	
	public static FontClass getInstance() {
		if(instance==null) {
			instance = new FontClass();
		}
		int err = DotArrayClass.Init();
		System.out.println("连接状态："+err);
		return instance;
	}
	
	private FontClass() {
		this.setShowTime(3);
	}
	
	public void startService() {		
		new Thread(this).start();
	}
	
	public void startTest(final AssetManager am) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				DotArrayClass.Test();
				setContent("", am);
			}
		}).start();
	}
	
	public void stopService() {
		DotArrayClass.Exit();
	}

	@Override
	public void run() {
		for(int i=0;i<this.fontCodes.length;i++) {
			final int no = i;
			new Thread(new Runnable() {				
				public void run() {
					DotArrayClass.DotShow(rearrange(printCode(fontCodes[no],true,false)));
				}
			}).start();
			try {
				Thread.sleep(this.showTime*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * 获取汉字字模编码，编码方式: 阴码，逐行，顺向
	 * @param str 待取模汉字，仅限于汉字
	 * @return 字模编码，二维字节数组，每一行对应一个汉字的编码，输入非汉字字符串返回null
	 */
	public byte[][] Str2ByteArr(String str,AssetManager am){
		if(!Pattern.matches("^[\\u4e00-\\u9fa5]+$", str)) {
			str = "非法字符";
		}
		try {
			InputStream is = am.open("hzk16s");
			byte[] buffer = new byte[is.available()];
			int b = 0,k=0;
			while((b=is.read())!=-1) {
				buffer[k] = (byte) b;
				k++;
			}
			is.close();
			byte[] str2byte = str.getBytes("GB2312");
			int len = str2byte.length/2;
			byte[][] result =  new byte[len][32];
			for(int i=0;i<len;i++) {
				int offset = (94*((str2byte[i*2]&0xFF)-161)+(str2byte[i*2+1]&0xFF)-161)*32;
				result[i] = Arrays.copyOfRange(buffer, offset,offset+32);
			}
			return result;
		}catch(IOException e) {
			return null;
		}
	}
	
	/**
	 * 阴码，逐行，顺向编码打印到控制台
	 * @param code 阴码，逐行，顺向编码
	 * @param p 是否在控制台打印
	 * @param yy false：阴码，true：阳码
	 * @return 阴码/阳码 逐行，顺向数组，元素为0/1
	 */
	public int[][] printCode(byte[] code,boolean p,boolean yy) {
		int[][] arr = new int[16][16];
		byte[] buffer = Arrays.copyOf(code, 32);
		for(int k=0; k<16; k++){
	        for(int j=0; j<2; j++){
	        	int v = buffer[k*2+j]&0xFF;
	            for(int i=0; i<8; i++){
	            	if(p)
	            		System.out.print((v>>(7-i))%2==1?"● ":"○ ");
	            	if(yy) {
	            		arr[k][j*8+i] = (v>>(7-i))%2==1?0:1;
	            	}else {
	            		arr[k][j*8+i] = (v>>(7-i))%2==1?1:0;
	            	}
	            }	
	        }
	        if(p)
	        	System.out.println();
	    }
        if(p)
        	System.out.println();
		return arr;
	}
	
	/**
	 * 重新排列字模编码：阴码，逐行，顺向-->阴码，逐列，逆向
	 * @param code 阴码，逐行，顺向编码
	 * @return 阴码，逐列，逆向编码
	 */
	public byte[] rearrange(int[][] arr) {
		byte[] code = new byte[32];
		for(int i=0;i<16;i++) {
			int a=0,b=0;
			for(int j=0;j<8;j++) {
				a+=arr[j][i]<<j;
				b+=arr[j+8][i]<<j;
			}
			code[i*2] = (byte) a;
			code[i*2+1] = (byte)b;
		} 
		return code;
	}
	
	/**
	 * 重新排列字模编码：阴码，逐行，顺向-->阴码，逐列，顺向
	 * @param code 阴码，逐行，顺向编码
	 * @return 阴码，逐列，顺向编码
	 */
	public byte[] rearrange1(int[][] arr) {
		byte[] code = new byte[32];
		for(int i=0;i<16;i++) {
			int a=0,b=0;
			for(int j=0;j<8;j++) {
				a+=arr[j][i]<<(7-j);
				b+=arr[j+8][i]<<(7-j);
			}
			code[i*2] = (byte) a;
			code[i*2+1] = (byte)b;
		} 
		return code;
	}
	
	public String getHexString(byte[] bt){
		String HEX = "01234567890ABCDEF";
		StringBuffer sb = new StringBuffer();
		for(int i=0,len=bt.length;i<len;i++){
			int num = 0;
			num = (bt[i] & 0xFF);
			sb.append(HEX.charAt(num>>4));
			sb.append(HEX.charAt(num%16));
			sb.append(" ");
		}
		return sb.toString();
	}

	public byte[][] getFontCodes() {
		return fontCodes;
	}

	public String getContent() {
		return content;
	}

	public byte[][] setContent(String content,AssetManager am) {
		this.content = content;
		this.fontCodes = Str2ByteArr(content,am);
		this.startService();
		return this.fontCodes;
	}

	public int getShowTime() {
		return showTime;
	}

	public void setShowTime(int showTime) {
		this.showTime = showTime;
	}
}
