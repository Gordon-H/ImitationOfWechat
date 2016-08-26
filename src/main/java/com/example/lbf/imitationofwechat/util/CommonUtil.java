package com.example.lbf.imitationofwechat.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by lbf on 2016/7/29.
 */
public class CommonUtil {
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }
    public static <T> T checkNotNull(T reference, @Nullable Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }
    public static void showNoImplementText(Context context){
        Toast.makeText(context,"Add more codes to support!",Toast.LENGTH_SHORT).show();
    }
    public static String formatTime(long time) {
        Calendar currentCal = Calendar.getInstance();
        Calendar msgCal = Calendar.getInstance();
        msgCal.setTimeInMillis(time);
        int interval = currentCal.get(Calendar.DAY_OF_YEAR) - msgCal.get(Calendar.DAY_OF_YEAR);
        SimpleDateFormat ft;
        if(interval > 1){
            ft = new SimpleDateFormat("MM/dd/yyyy");
            return ft.format(msgCal.getTime());
        }else if(interval ==1){
            ft = new SimpleDateFormat("HH:mm");
            return "昨天"+ft.format(msgCal.getTime());
        }else{
            ft = new SimpleDateFormat("HH:mm");
            return ft.format(msgCal.getTime());
        }
    }

    public static boolean checkSdCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

}
