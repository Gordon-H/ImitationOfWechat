package com.example.lbf.imatationofwechat.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by lbf on 2016/7/10.
 */
public class InputMethodUtil {

    private static InputMethodManager inputMethodManager;
    public static void showKeyboard(Context context, View inputView) {
        if(inputMethodManager == null){
            inputMethodManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        inputView.requestFocus();
        inputMethodManager.showSoftInput(
                inputView, InputMethodManager.SHOW_FORCED);
    }
    public static void hideKeyboard(Context context){
        if(inputMethodManager == null){
            inputMethodManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), 0);
    }

}
