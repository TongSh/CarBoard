package com.wm.remusic.activity;

import java.io.File;

import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.led.LedClass;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.wm.remusic.R;

import com.hanheng.a53.led.LedClass;

import static android.os.SystemClock.sleep;


public class VideoActivity extends Activity {
    private Button btn_StartVideoCamera;
    private static final String FILE_PATH = "/sdcard/sysvideocamera.3gp";
    private static final String TAG="main";
    private MediaPlayer mp3;
    private boolean stopMP3 = false;
    private boolean[] arryState={false,false,false,false};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysvideocamera);
        alarm();
        getDipVedio();
    }

    private SoundPool sp;
    public void music() {
        sp =new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        final int soundID_1 = sp.load(getApplicationContext(), R.raw.daoche, 1);
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(soundID_1, 0.6f, 0.6f, 1, -1, 1);
            }
        });
    }


    public void alarm(){
        new Thread(){
            public void run(){
                music();
                Intent intent = new Intent();
                intent.setAction("android.media.action.VIDEO_CAPTURE");
                intent.addCategory("android.intent.category.DEFAULT");
                File file = new File(FILE_PATH);
                if(file.exists()){
                    file.delete();
                }
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 0);
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "resultCode="+requestCode);
    }

    public void getDipVedio(){
        new Thread(){
            public void run(){
                DipClass.Init();
                while (true){
                    try {
                        Message msgMessage=new Message();
                        int value= DipClass.ReadValue();
                        computed(value);
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //	拨码开关状态显示
    public void computed(int val){
        String str=addZero(val);
        char[] cr=str.toCharArray();
        int tag;
        if(cr[4]=='1'){
            sp.autoPause();
            sp.stop(1);
            sleep(100);
//            Intent it1 = new Intent();
//            it1.setClass(VideoActivity.this,Main2Activity.class);
//            startActivity(it1);
            finish();
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

}
