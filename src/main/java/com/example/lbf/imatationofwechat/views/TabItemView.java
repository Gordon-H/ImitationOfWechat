package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.lbf.imatationofwechat.R;

/**
 * Created by lbf on 2016/6/23.
 */
public class TabItemView extends View {
    private final int DEFAULT_TEXT_SIZE = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,12,getResources().getDisplayMetrics());
    private final int DEFAULT_COLOR = 0xFF45C01A;

    private Bitmap bitmapNormal;
    private Bitmap bitmapSelected;
    private String text;
    private int textSize = DEFAULT_TEXT_SIZE;
    private int textColor;
    private int alpha = 0;
    private Rect textBounds;
    private Paint mPaint;

    public TabItemView(Context context) {
        this(context,null);
    }

    public TabItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public TabItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TabItemView);
        int n = typedArray.getIndexCount();
        for(int i = 0;i < n;i++){
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.TabItemView_viewColor:
                    textColor = typedArray.getColor(attr,DEFAULT_COLOR);
                    break;
                case R.styleable.TabItemView_description:
                    text = typedArray.getString(attr);
                    break;
                case R.styleable.TabItemView_iconNormal:
                    bitmapNormal =((BitmapDrawable) typedArray.getDrawable(attr)).getBitmap();
                    break;
                case R.styleable.TabItemView_iconSelected:
                    bitmapSelected =((BitmapDrawable) typedArray.getDrawable(attr)).getBitmap();
                    break;
                case R.styleable.TabItemView_desTextSize:
                    textSize = (int) typedArray.getDimension(attr,DEFAULT_TEXT_SIZE);
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setColor(textColor);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        textBounds = new Rect();
        mPaint.getTextBounds(text,0,text.length(),textBounds);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIcon(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        float x = (getMeasuredWidth()-textBounds.width())/2;
        float y = getMeasuredHeight()-getPaddingBottom();
        mPaint.setColor(0x000000);
        mPaint.setAlpha(255-alpha);
        canvas.drawText(text,x,y,mPaint);
        mPaint.setColor(0x45ca45);
        mPaint.setAlpha(alpha);
        canvas.drawText(text,x,y,mPaint);
    }

    private void drawIcon(Canvas canvas) {
        int bitmapWidth = bitmapNormal.getWidth();
        int bitmapHeight = bitmapNormal.getHeight();
//        图片的边界
        Rect srcBounds = new Rect(0,0,bitmapWidth,bitmapHeight);
//        最终绘制的边界
        Rect dstBounds = new Rect();
        float ratio = (float) (bitmapWidth*1.0/bitmapHeight);
//        除去绘制text所需的空间后剩余的空间
        int spareWidth = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
        int spareHeight = getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-textBounds.height();
//        考虑图片的宽高比后，剩余空间所对应的有效空间
        int validWidth,validHeight;
        if(spareWidth<spareHeight*ratio){
            validWidth = spareWidth;
        }else{
            validWidth = (int) (spareHeight*ratio);
        }
        validHeight = (int) (validWidth/ratio);
        if(validWidth<bitmapWidth){
//            控件剩余的空间小于图片的空间，边界应以剩余的空间为准.
            dstBounds.left = (getMeasuredWidth()-validWidth)/2;
            dstBounds.top = (spareHeight-validHeight)/2+getPaddingTop();
            dstBounds.right = dstBounds.left+validWidth;
            dstBounds.bottom = dstBounds.top+validHeight;

        }else{
//            控件剩余的空间大于图片的空间，边界应以图片为准.
            dstBounds.left = (getMeasuredWidth()-bitmapWidth)/2;
            dstBounds.top = (spareHeight-bitmapHeight)/2+getPaddingTop();
            dstBounds.right = dstBounds.left+bitmapWidth;
            dstBounds.bottom = dstBounds.top+bitmapHeight;
        }
        mPaint.setAlpha(255-alpha);
        canvas.drawBitmap(bitmapNormal,srcBounds,dstBounds,mPaint);
        mPaint.setAlpha(alpha);
        canvas.drawBitmap(bitmapSelected,srcBounds,dstBounds,mPaint);
    }

    public void setViewAlpha(int alpha){
        this.alpha = alpha;
        if(Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        }else{
            postInvalidate();
        }
    }
}
