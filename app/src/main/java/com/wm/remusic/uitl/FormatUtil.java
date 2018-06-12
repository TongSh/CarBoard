package com.wm.remusic.uitl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

/**
 * Created by 王真 on 2018/6/9.
 */

public class FormatUtil {

    public static void videoRename(File recAudioFile) {
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ "/mahc/video/"+ "0" + "/";
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + ".3gp";
        File out = new File(path);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(path, fileName);
        if (recAudioFile.exists())
            recAudioFile.renameTo(out);
    }

    /**
     * 用以计时操作的相关方法
     * @param num
     * @return
     */
    public static String format(int num){
        String s = num + "";
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

}
