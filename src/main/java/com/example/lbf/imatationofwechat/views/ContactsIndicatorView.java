package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lbf on 2016/6/23.
 */
public class ContactsIndicatorView extends View {
    private Paint mPaint;
    private String charList;
    private boolean isFirst = true;
    private int totalHeight;
    private int itemHeight;
    private float textSize;
    public interface OnItemSelectedListener{
        void onItemSelected(int position);
        void onItemUnselected();
    }
    OnItemSelectedListener listener;

    public ContactsIndicatorView(Context context) {
        this(context,null);
    }

    public ContactsIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ContactsIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(charList.length() == 0){
            return;
        }
        if(isFirst){
            isFirst = false;
            totalHeight = getMeasuredHeight();
            itemHeight = (int) (1.0*totalHeight/(charList.length()));
//            取item的80%的高度作为字体的大小（这种方法不太规范，暂时没想到其他根据控件大小调整字体大小的方法）
            textSize = (float) (itemHeight*0.8);
            mPaint.setTextSize(textSize);
        }
        drawText(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        int position = (int) (y/itemHeight);
        if(position <0){
            position = 0;
        }else if(position>charList.length() - 1){
            position = charList.length() - 1;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if(listener !=null){
                    listener.onItemSelected(position);
                }
                return true;
            case MotionEvent.ACTION_UP:
                if(listener !=null){
                    listener.onItemUnselected();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setCharList(String charList) {
        this.charList = charList;
        invalidate();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private void drawText(Canvas canvas) {
        for(int i = 0;i<charList.length();i++){
            String c = charList.substring(i,i+1);
            Rect bounds = new Rect();
            mPaint.getTextBounds(c,0,1,bounds);
            float x = (getMeasuredWidth()-bounds.width())/2;
            float y = itemHeight*(i+1)-(itemHeight - bounds.height())/2;
            canvas.drawText(c,x,y,mPaint);
        }
    }


}
