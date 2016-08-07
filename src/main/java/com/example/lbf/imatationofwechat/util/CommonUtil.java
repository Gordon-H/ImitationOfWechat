package com.example.lbf.imatationofwechat.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

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
}
