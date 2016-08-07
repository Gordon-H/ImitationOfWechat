package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.DisplayUtil;

/**
 * Created by lbf on 2016/8/2.
 */
public class ItemView extends LinearLayout {
    private Context mContext;
    private ImageView mImageView;
    private TextView mTextView;
    private String text;
    private Drawable image;

    public ItemView(Context context) {
        this(context,null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        text = (String) ta.getText(R.styleable.ItemView_itemText);
        image = ta.getDrawable(R.styleable.ItemView_itemImage);
        ta.recycle();
        mContext = context;
        initView();
        addImage();
        addText();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,mContext.getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        setLayoutParams(params);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    private void addImage() {
        mImageView = new ImageView(mContext);
        int size = DisplayUtil.dip2px(mContext,40);
        LayoutParams params = new LayoutParams(size,size);
        params.leftMargin = params.rightMargin = DisplayUtil.dip2px(mContext,16);
        mImageView.setLayoutParams(params);
        mImageView.setImageDrawable(image);
        addView(mImageView);
    }

    private void addText() {
        mTextView = new TextView(mContext);
        int size = DisplayUtil.dip2px(mContext,150);
        LayoutParams params = new LayoutParams(size, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextView.setLayoutParams(params);
        mTextView.setText(text);
        mTextView.setEllipsize(TextUtils.TruncateAt.END);
        mTextView.setLines(1);
        mTextView.setGravity(Gravity.CENTER_VERTICAL);
        mTextView.setTextColor(ContextCompat.getColor(mContext,R.color.item_text));
        mTextView.setTextSize(14);
        addView(mTextView);
    }

    public void setText(String text){
        mTextView.setText(text);
    }
    public void setImage(int imageRes){
        mImageView.setImageResource(imageRes);
    }

}
