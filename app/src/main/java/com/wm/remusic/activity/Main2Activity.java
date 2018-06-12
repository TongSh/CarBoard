package com.wm.remusic.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.led.LedClass;
import com.hanheng.a53.relay.RelayClass;
import com.wm.remusic.R;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Main2Activity extends Activity {
    /**探测过程**/
    public final static int DETECT_PROGRESS =0;
    /* 拨码开关 */
    public final static int SWITCH_STATE =1;



    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case DETECT_PROGRESS:
                    int dist = msg.arg1;
                    Toast.makeText(Main2Activity.this,"车距过近，请注意！！！",Toast.LENGTH_SHORT).show();
                    alarm(1);
                    getDistance();
                    break;
                case SWITCH_STATE:
                    computed(msg.arg1);
//                    switch (msg.arg1){
//
//                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //	拨码开关状态显示
    public void computed(int val){
        String str=addZero(val);
        char[] cr=str.toCharArray();
        int tag;
        for(int i=0;i<cr.length;i++){
            if(cr[i]=='0'){
                try {
                    changeState(i,0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    changeState(i,1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //	改变拨码开关的状态
    public void changeState(int i,int tag) throws InterruptedException {
        if(tag==0){
            switch (i) {
//                case 0:
//                    tb8.setChecked(true);
//                    break;
//                case 1:
//                    tb7.setChecked(true);
//                    break;
//                case 2:
//                    tb6.setChecked(true);
//                    break;
//                case 3:
//                    tb5.setChecked(true);
//                    break;
//                case 4:
////                    tb4.setChecked(true);
//                    textsudu.setText("检查车速");
//                    int min=0;
//                    int max=300;
//                    Random random = new Random();
//                    int num = random.nextInt(max)%(max-min+1) + min;
//                    if(num>150){
//                        textsudu.setText("您已超速，请减速");
//                        try {
//                            if (mp3 != null) {
//                                mp3.stop();
//                            }
//                            mp3.prepare();         //进入到准备状态
//                            mp3.start();
//                            sleep(100);//开始播放
//                            //  state.setText("Playing");  //改变输出信息为“Playing”，下同
//                        } catch (Exception e) {
//                            //  state.setText(e.toString());//以字符串的形式输出异常
//                            e.printStackTrace();  //在控制台（control）上打印出异常
//                        }
//                    }
//                    else{
//                        textsudu.setText("速度正常，请继续保持");
//                        try {
//                            if (mp3 != null) {
//                                mp3.stop();
//                                //state.setText("stop");
//                            }
//                        } catch (Exception e) {
//                            //  state.setText(e.toString());
//                            e.printStackTrace();
//                        }
//                    }
//                    String str = addZero1(String.valueOf(num));
//                    Seg7Class.Seg7Show(num);
//                    break;
                case 4:

                case 5:
                    Toast.makeText(Main2Activity.this,"右转",Toast.LENGTH_LONG).show();
                    for(int j=0;j<2;j++){
                        LedClass.IoctlLed(2, 1);
                        LedClass.IoctlLed(3, 1);
                        RelayClass.IoctlRelay(1, 1);
                        sleep(400);
                        LedClass.IoctlLed(2, 0);
                        LedClass.IoctlLed(3, 0);
                        RelayClass.IoctlRelay(1, 0);
                        sleep(400);
                    }
                    break;
                case 6:
                    Toast.makeText(Main2Activity.this,"左转",Toast.LENGTH_LONG).show();
                    for(int j=0;j<2;j++){
                        LedClass.IoctlLed(0, 1);
                        LedClass.IoctlLed(1, 1);
                        RelayClass.IoctlRelay(1, 1);
                        sleep(400);
                        LedClass.IoctlLed(0, 0);
                        LedClass.IoctlLed(1, 0);
                        RelayClass.IoctlRelay(1, 0);
                        sleep(400);
                    }
                    break;
//                case 7://如果传来门没有关好的信号，就发出蜂鸣声
//                    tb1.setChecked(true);
//                    textdoor.setText("车门未关好");
//                    BeepClass.IoctlRelay(BEEP_ON);    //调用JNI的IOCTLBEEP函数
//                    break;
                default:
                    break;
            }
        }else{
            switch (i) {
//                case 0:
//                    tb8.setChecked(false);
//                    break;
//                case 1:
//                    tb7.setChecked(false);
//                    break;
//                case 2:
//                    tb6.setChecked(false);
//                    break;
//                case 3:
//                    tb5.setChecked(false);
//                    break;
//                case 4:
//                    tb4.setChecked(false);
//                    Seg7Class.Exit();
//                    try {
//                        if (mp3 != null) {
//                            mp3.stop();
//                            //state.setText("stop");
//                        }
//                    } catch (Exception e) {
//                        //  state.setText(e.toString());
//                        e.printStackTrace();
//                    }
//                    break;
                case 5:
                    LedClass.IoctlLed(0, 0);
                    LedClass.IoctlLed(1, 0);
                    LedClass.IoctlLed(2, 0);
                    LedClass.IoctlLed(3, 0);
//                    tb3.setChecked(false);
                    break;
                case 6:
                    LedClass.IoctlLed(0, 0);
                    LedClass.IoctlLed(1, 0);
                    LedClass.IoctlLed(2, 0);
                    LedClass.IoctlLed(3, 0);
//                    tb2.setChecked(false);
                    break;
//                case 7:
//                    tb1.setChecked(false);
//                    textdoor.setText("车门已关好");
//                    BeepClass.IoctlRelay(BEEP_OFF);    //调用JNI的IOCTLBEEP函数
//                    break;
                default:
                    break;
            }
        }
    }

    //	字符串补零
    public String addZero(int b){
        String val = Integer.toBinaryString(b&0xFF);
        String str="";
        if(val.length()<8){
            for(int i=0;i<8-val.length();i++){
                str+=0;
            }
            return str+=val;
        }
        return val;
    }

    private SoundPool sp;
    public void alarm(int content) {
        sp =new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        final int soundID_1 = sp.load(getApplicationContext(), R.raw.alarm, 1);
        Log.i("ALARM","!!!");
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundID_1, 0.6f, 0.6f, 1, 0, 1);
            }
        });
    }


    private AlertDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        getDistance();

        LedClass.Init();
        RelayClass.Init();
        getDip();
    }

    public void onClickListener(View v) {

        switch (v.getId()) {
            case R.id.music:
                Intent it = new Intent();
                it.setClass(Main2Activity.this,MainActivity.class);
                startActivity(it);
//                Toast.makeText(this, "music", Toast.LENGTH_SHORT).show();
                break;
            case R.id.vedio:
//                Toast.makeText(this, "Vedio", Toast.LENGTH_SHORT).show();
                Intent it1 = new Intent();
                it1.setClass(Main2Activity.this,SysVideoCameraActivity.class);
                startActivity(it1);
                break;
            case R.id.phone:
                Intent it2 = new Intent();
                it2.setClass(Main2Activity.this,MapActivity.class);
                startActivity(it2);
//                Toast.makeText(this, "电话", Toast.LENGTH_SHORT).show();
                break;
            case R.id.message:
                Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "ERROR!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void getDip(){
        new Thread(){
            public void run(){
                DipClass.Init();
                while (true){
                    try {
                        Message msgMessage=new Message();
                        int value= DipClass.ReadValue();
                        msgMessage.what=SWITCH_STATE;
                        msgMessage.arg1=value;
                        handler.sendMessage(msgMessage);
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void getDistance(){
        new Thread(){
            public void run(){
                Message msg = new Message();
                for (int i = 0; i < 20000; ++i) {
                    msg.what = DETECT_PROGRESS;
                    msg.arg1 = new Random().nextInt(1000);
                    int arg = new Random().nextInt(1000);
                    if (arg < 10) {
                        handler.sendMessage(msg);
                        return;
                    }
                    Log.i("id is ---", i + "\n");
                    try {
                        sleep(1 * 100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
