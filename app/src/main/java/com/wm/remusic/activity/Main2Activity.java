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

import com.wm.remusic.R;

import java.util.Random;

public class Main2Activity extends Activity {
    /**探测过程**/
    public final static int DETECT_PROGRESS =0;



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
            }
            super.handleMessage(msg);
        }
    };

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
        getDistance();
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
                Toast.makeText(this, "Vedio", Toast.LENGTH_SHORT).show();
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
                        Thread.sleep(1 * 100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
