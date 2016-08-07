package com.example.lbf.imatationofwechat.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.util.EmojiUtil;

/**
 * Created by lbf on 2016/7/11.
 */
public class TextViewFragment extends Fragment implements View.OnTouchListener{
    public static final String INTENT_KEY_INFO = "info";
    private TextView textView;
    public TextViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        textView = new TextView(getActivity());
        textView.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.WHITE);
        textView.setClickable(true);
        textView.setOnTouchListener(this);
        return textView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String content = bundle.getString("info");
        SpannableString spannableString = EmojiUtil.convertString(getActivity(),content);
        textView.setText(spannableString);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }
}
