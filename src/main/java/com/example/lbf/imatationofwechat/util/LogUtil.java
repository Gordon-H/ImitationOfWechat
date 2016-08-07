package com.example.lbf.imatationofwechat.util;

import android.util.Log;

/**
 * Created by lbf on 2016/6/27.
 */
public class LogUtil {
    private static boolean showLog = true;
    public static void i(String msg){
        if(showLog){
            Log.i("info",msg);
        }
    }
}
