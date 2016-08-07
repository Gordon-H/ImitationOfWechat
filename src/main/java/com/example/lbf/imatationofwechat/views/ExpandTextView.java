package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.LogUtil;

/**
 * Created by lbf on 2016/7/22.
 */
public class ExpandTextView extends LinearLayout implements View.OnClickListener{

    private static final int DEFAULT_MAX_LINES = 4;
    private Context context;
    private String content;
    private int maxLines;
    private TextView tvContent;
    private TextView tvExpand;

    public ExpandTextView(Context context) {
        super(context);
        init(context,null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
        maxLines = ta.getInt(R.styleable.ExpandTextView_maxLines,DEFAULT_MAX_LINES);
        content = ta.getString(R.styleable.ExpandTextView_content);
        ta.recycle();
        addViews();
    }

    private void addViews() {
        setOrientation(VERTICAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvContent = new TextView(context);
        tvContent.setLayoutParams(params);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvContent.setTextColor(Color.BLACK);
        addView(tvContent);
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvExpand = new TextView(context);
        int pd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                context.getResources().getDisplayMetrics());
        tvExpand.setPadding(0, pd, 0, pd);
        tvExpand.setLayoutParams(params2);
        tvExpand.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tvExpand.setTextColor(ContextCompat.getColor(context, R.color.praise_item));
        tvExpand.setText("全文");
        tvExpand.setOnClickListener(this);
        addView(tvExpand);
        setContent(content);
    }

    @Override
    public void onClick(View v) {
        String text = tvExpand.getText().toString();
        if(text.equals("全文")){
            tvExpand.setText("收起");
            tvContent.setMaxLines(100);
        }else{
            tvExpand.setText("全文");
            tvContent.setMaxLines(maxLines);
        }
    }

    public void setContent(String content) {
        if(TextUtils.isEmpty(content)){
            content = "";
        }
        this.content = content;
        tvContent.setMaxLines(100);
        tvContent.setText(content);
        tvContent.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = tvContent.getLineCount();
                LogUtil.i("lineCount = "+lineCount);
                if(lineCount <= 4){
                    tvExpand.setVisibility(GONE);
                }else{
                    tvExpand.setVisibility(VISIBLE);
                }
                tvContent.setMaxLines(maxLines);
            }
        });

    }
}
