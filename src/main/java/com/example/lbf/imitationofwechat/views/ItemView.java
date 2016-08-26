package com.example.lbf.imitationofwechat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.util.DisplayUtil;

/**
 * Created by lbf on 2016/8/2.
 */
public class ItemView extends LinearLayout {

    private final int DEFAULT_IMAGE_SIZE;
    private Context mContext;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvLeft;
    private TextView tvRight;
    private String textLeft;
    private String textRight;
    int ivLeftSize;
    int ivRightSize;
    private Drawable imageLeft;
    private Drawable imageRight;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        DEFAULT_IMAGE_SIZE = DisplayUtil.dip2px(mContext, 40);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        textLeft = (String) ta.getText(R.styleable.ItemView_leftText);
        textRight = (String) ta.getText(R.styleable.ItemView_rightText);
        imageLeft = ta.getDrawable(R.styleable.ItemView_leftImage);
        imageRight = ta.getDrawable(R.styleable.ItemView_rightImage);
        ivLeftSize = (int) ta.getDimension(R.styleable.ItemView_leftImageSize, DEFAULT_IMAGE_SIZE);
        ivRightSize = (int) ta.getDimension(R.styleable.ItemView_rightImageSize, DEFAULT_IMAGE_SIZE);
        ta.recycle();
        initView();
        initData();
    }

    private void initData() {
        if (textLeft != null) {
            tvLeft.setText(textLeft);
        } else {
            tvLeft.setVisibility(GONE);
        }
        if (textRight != null) {
            tvRight.setText(textRight);
        } else {
            tvRight.setVisibility(GONE);
        }
        if (imageLeft != null) {
            ivLeft.setImageDrawable(imageLeft);
        } else {
            ivLeft.setVisibility(GONE);
        }
        if (imageRight != null) {
            ivRight.setImageDrawable(imageRight);
        } else {
            ivRight.setVisibility(GONE);
        }

    }

    private void initView() {
        setOrientation(HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int size = DisplayUtil.dip2px(mContext, 16);
        setPadding(size,size/4,size,size/4);
        setLayoutParams(params);
        setGravity(Gravity.CENTER_VERTICAL);
        addLeft();
        addRight();
    }

    private void addLeft() {
        ivLeft = new ImageView(mContext);
        LayoutParams params1 = new LayoutParams(ivLeftSize, ivLeftSize);
        params1.rightMargin =DisplayUtil.dip2px(mContext, 16);
        ivLeft.setLayoutParams(params1);
        addView(ivLeft);
        tvLeft = new TextView(mContext);
        LayoutParams params2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLeft.setLayoutParams(params2);
        int maxWidth = DisplayUtil.dip2px(mContext, 150);
        tvLeft.setMaxWidth(maxWidth);
        tvLeft.setEllipsize(TextUtils.TruncateAt.END);
        tvLeft.setLines(1);
        tvLeft.setGravity(Gravity.CENTER_VERTICAL);
        tvLeft.setTextColor(ContextCompat.getColor(mContext, R.color.item_text_left));
        tvLeft.setTextSize(14);
        addView(tvLeft);
        View view = new View(mContext);
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        view.setLayoutParams(params);
        addView(view);
    }

    private void addRight() {
        tvRight = new TextView(mContext);
        LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvRight.setLayoutParams(params1);
        int maxWidth = DisplayUtil.dip2px(mContext, 150);
        tvRight.setMaxWidth(maxWidth);
        tvRight.setEllipsize(TextUtils.TruncateAt.END);
        tvRight.setLines(1);
        tvRight.setGravity(Gravity.CENTER_VERTICAL);
        tvRight.setTextColor(ContextCompat.getColor(mContext, R.color.item_text_right));
        tvRight.setTextSize(12);
        addView(tvRight);
        ivRight = new ImageView(mContext);
        LayoutParams params = new LayoutParams(ivRightSize, ivRightSize);
        params.leftMargin = DisplayUtil.dip2px(mContext, 16);
        ivRight.setLayoutParams(params);
        addView(ivRight);
    }

    public void setTextLeft(String text) {
        if (tvLeft.getVisibility() == GONE) {
            tvLeft.setVisibility(VISIBLE);
        }
        tvLeft.setText(text);
    }

    public void setTextRight(String text) {
        if (tvRight.getVisibility() == GONE) {
            tvRight.setVisibility(VISIBLE);
        }
        if(tvRight!=null){
            textRight = text;
            tvRight.setText(text);
        }
    }

    public String getTextRight() {
        return textRight;
    }

    public void setImageLeft(int imageRes) {
        if (ivLeft.getVisibility() == GONE) {
            ivLeft.setVisibility(VISIBLE);
        }
        ivLeft.setImageResource(imageRes);
    }

    public void setImageRight(int imageRes) {
        if (ivRight.getVisibility() == GONE) {
            ivRight.setVisibility(VISIBLE);
        }
        ivRight.setImageResource(imageRes);
    }
    public void setImageRight(Bitmap bitmap) {
        if (ivRight.getVisibility() == GONE) {
            ivRight.setVisibility(VISIBLE);
        }
        if(bitmap!=null){
            ivRight.setImageBitmap(bitmap);

        }
    }

    public void setImageLeft(int imageRes, int size) {
        if (ivLeft.getVisibility() == GONE) {
            ivLeft.setVisibility(VISIBLE);
        }
        ViewGroup.LayoutParams params = ivLeft.getLayoutParams();
        params.width = params.height = size;
        ivLeft.setLayoutParams(params);
        ivLeft.setImageResource(imageRes);
    }

    public void setImageRight(int imageRes, int size) {
        if (ivRight.getVisibility() == GONE) {
            ivRight.setVisibility(VISIBLE);
        }
        ViewGroup.LayoutParams params = ivRight.getLayoutParams();
        params.width = params.height = size;
        ivRight.setLayoutParams(params);
        ivRight.setImageResource(imageRes);
    }

    public ImageView getIvRight() {
        return ivRight;
    }

    public int getIvRightSize() {
        return ivRightSize;
    }
}
