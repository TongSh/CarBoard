package com.wm.remusic.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hanheng.a53.beep.BeepClass;
import com.hanheng.a53.dip.DipClass;
import com.hanheng.a53.led.LedClass;
import com.hanheng.a53.relay.RelayClass;
import com.hanheng.a53.seg7.Seg7Class;
import com.wm.remusic.R;

import java.io.File;
import java.util.Random;

import static java.lang.Thread.sleep;

public class Main2Activity extends Activity {
    /**探测过程**/
    public final static int DETECT_PROGRESS = 0;
    /* 拨码开关 */
    public final static int SWITCH_STATE = 1;
    public static final int BEEP_ON = 0;
    public static final int BEEP_OFF = 1;
    /* in back */
    public boolean inVedio = false;


    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DETECT_PROGRESS:
                    int dist = msg.arg1;
                    Toast.makeText(Main2Activity.this, "车距过近，请注意！！！", Toast.LENGTH_SHORT).show();
                    alarm(1);
                    getDistance();
                    break;
                case SWITCH_STATE:
                    computed(msg.arg1);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //	拨码开关状态
    public void computed(int val) {
        String str = addZero(val);
        char[] cr = str.toCharArray();
        int tag;
        for (int i = 0; i < cr.length; i++) {
            if (cr[i] == '0') {
                try {
                    changeState(i, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    changeState(i, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private SoundPool bgmusic;
    private int bgsound;
    private static final String FILE_PATH = "/sdcard/sysvideocamera.3gp";

    public void backDrive() {
        bgmusic.play(bgsound, 0.6f, 0.6f, 1, -1, 1);
        Intent intent = new Intent();
        intent.setAction("android.media.action.VIDEO_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        File file = new File(FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 666);
    }

    //	改变拨码开关的状态
    public void changeState(int i, int tag) throws InterruptedException {
        if (tag == 0) {
            switch (i) {
                case 4:
                    if (!inVedio) {
                        inVedio = true;
                        backDrive();
                    }
                    break;
                case 5:
                    for (int j = 0; j < 2; j++) {
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
                    for (int j = 0; j < 2; j++) {
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
        } else {
            switch (i) {
                case 4:
                    if (inVedio) {
                        inVedio = false;
                        bgmusic.autoPause();
                        finishActivity(666);
                    }
                    break;
                case 5:
                    LedClass.IoctlLed(0, 0);
                    LedClass.IoctlLed(1, 0);
                    LedClass.IoctlLed(2, 0);
                    LedClass.IoctlLed(3, 0);
                    break;
                case 6:
                    LedClass.IoctlLed(0, 0);
                    LedClass.IoctlLed(1, 0);
                    LedClass.IoctlLed(2, 0);
                    LedClass.IoctlLed(3, 0);
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
    public String addZero(int b) {
        String val = Integer.toBinaryString(b & 0xFF);
        String str = "";
        if (val.length() < 8) {
            for (int i = 0; i < 8 - val.length(); i++) {
                str += 0;
            }
            return str += val;
        }
        return val;
    }

    private SoundPool sp;

    public void alarm(int content) {
        sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        final int soundID_1 = sp.load(getApplicationContext(), R.raw.alarm, 1);
        Log.i("ALARM", "!!!");
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
        LedClass.Init();
        RelayClass.Init();
        Seg7Class.Init();
        BeepClass.Init();
        getDip();
        getDistance();
        getSpeed();

        bgmusic = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        bgsound = bgmusic.load(getApplicationContext(), R.raw.sky, 1);
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.music:
                Intent it = new Intent();
                it.setClass(Main2Activity.this, MainActivity.class);
                startActivity(it);
//                Toast.makeText(this, "music", Toast.LENGTH_SHORT).show();
                break;
            case R.id.vedio:
//                Toast.makeText(this, "Vedio", Toast.LENGTH_SHORT).show();
                Intent it1 = new Intent();
                it1.setClass(Main2Activity.this, SysVideoCameraActivity.class);
                startActivity(it1);
                break;
            case R.id.phone:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:10086"));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                break;
            case R.id.message:
                Intent it2 = new Intent();
                it2.setClass(Main2Activity.this,MapActivity.class);
                startActivity(it2);
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
                        sleep(900);
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
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void getSpeed(){
        new Thread(){
            public void run(){
                Message msg = new Message();
                int arg = 60;
                int temp = 1;
                while (true){
                    if(arg > 65)
                        temp = -1;
                    if(arg < 60)
                        temp = 1;
                    arg = arg + temp;
                    updateText(arg);
                    if (arg > 65) {
//                        BeepClass.IoctlRelay(BEEP_ON);    //调用JNI的IOCTLBEEP函数
                    }else {
                        BeepClass.IoctlRelay(BEEP_OFF);    //调用JNI的IOCTLBEEP函数
                    }
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    public String segZero(String content) {
        int count = 4 - content.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sb.append("0");
        }
        StringBuffer str = sb.append(content);
        return str.toString();
    }
    public void updateText(final int arg){
        String str = segZero(String.valueOf(arg));
        /**
         * 请在此补充硬件调用函数
         */
        new Thread(new Runnable() {
            public void run() {
                Seg7Class.Seg7Show(arg);
            }
        }).start();
    }
}
