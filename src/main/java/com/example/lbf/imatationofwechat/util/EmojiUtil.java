package com.example.lbf.imatationofwechat.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.example.lbf.imatationofwechat.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lbf on 2016/7/10.
 */
public class EmojiUtil {
    public static SpannableString convertString(Context context,String str){
        SpannableString spannableString = new SpannableString(str);
        String regularExpression = "\\[[^\\]]+\\]";
        Pattern pattern = Pattern.compile(regularExpression);
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            String group = matcher.group();
            Bitmap bitmap = createBitmapFromName(context,group.substring(1,group.length()-1));
            ImageSpan span = new ImageSpan(context,bitmap);
            spannableString.setSpan(span, matcher.start(),matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    private static Bitmap createBitmapFromName(Context context,String name) {
        Bitmap bitmap = null;
        try {
            Field field = R.drawable.class.getDeclaredField(name);
            int resId = Integer.parseInt(field.get(null).toString());
            bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
