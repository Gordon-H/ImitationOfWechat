package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.LogUtil;

/**
 * Created by lbf on 2016/6/27.
 */
public class TextInputView extends EditText {

    private static boolean isTextEmpty = true;
    private Context context;
    private int boundLeft;
    private int boundRight;
    private int boundTop;
    private int boundBottom;
    private Drawable drawableRightNormal;
    private Drawable drawableRightHighLight;
    private boolean canDrawableRightHighLight = false;
    private boolean isDrawableRightHighLight = false;
    private Paint paint;

    public interface OnImageClickListener {
        void onImageClick(boolean isImageHighlight);
    }

    private OnImageClickListener imageClickListener;

    public void setOnImageClickListener(OnImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public TextInputView(Context context) {
        this(context, null);
    }

    public TextInputView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public TextInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextInputView);
        int imageRes = a.getResourceId(R.styleable.TextInputView_drawableRightHighLight, -1);
        if (imageRes != -1) {
            canDrawableRightHighLight = true;
            drawableRightHighLight = getResources().getDrawable(imageRes);
        }
        a.recycle();
        init();
    }

    private void init() {
        drawableRightNormal = getCompoundDrawables()[2];
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1.0f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.i("onMeasure");
//        如果放在onDraw里会有闪屏现象
        drawableRightNormal.setBounds(0, getMeasuredHeight()/2 - drawableRightNormal.getIntrinsicHeight()/2 - 5
                , drawableRightNormal.getIntrinsicWidth(),
                getMeasuredHeight()/2+drawableRightNormal.getIntrinsicHeight()/2-8);
        drawableRightHighLight.setBounds(0, getMeasuredHeight()/2 - drawableRightHighLight.getIntrinsicHeight()/2 - 5
                , drawableRightHighLight.getIntrinsicWidth(),
                getMeasuredHeight()/2+drawableRightHighLight.getIntrinsicHeight()/2-8);
        boundLeft = getMeasuredWidth() - drawableRightNormal.getIntrinsicWidth();
        boundRight = getMeasuredWidth();
        boundTop = getMeasuredHeight()-8-drawableRightNormal.getIntrinsicHeight();
        boundBottom = getMeasuredHeight()-8;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int lineY = getHeight()+getScrollY()-4;
//        int lineHeight = getHeight();
        canvas.drawLine(0, lineY, getWidth(), lineY, paint);

    }
    public void setImageHighlight(boolean isHighlight){
        if(isDrawableRightHighLight == isHighlight){
            return;
        }
        isDrawableRightHighLight = isHighlight;
        if (!isDrawableRightHighLight) {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                    drawableRightNormal, getCompoundDrawables()[3]);
        } else {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                    drawableRightHighLight, getCompoundDrawables()[3]);
        }

    }
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            paint.setColor(context.getResources().getColor(R.color.editTextUnderLine));
        } else {
            paint.setColor(Color.BLACK);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (x > boundLeft && x < boundRight
                    &&y > boundTop && y < boundBottom) {
                if (canDrawableRightHighLight) {
                    setImageHighlight(!isDrawableRightHighLight);
                    if (imageClickListener != null) {
                        imageClickListener.onImageClick(isDrawableRightHighLight);
                    }
                }
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public interface OnTextStateChangeListener {
        void onTextStateChange(boolean isEmpty);
    }

    private OnTextStateChangeListener listener;

    public void setOnTextStateChangeListener(OnTextStateChangeListener listener) {
        this.listener = listener;
    }

    //    onTextChange的执行顺序先于构造方法，因此isTextEmpty需要设为static以保证正确初始化
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (isTextEmpty && getText().length() > 0) {
            isTextEmpty = false;
            if (listener != null) {
                listener.onTextStateChange(false);
            }
        }else if (!isTextEmpty && getText().length() == 0) {
                isTextEmpty = true;
                if (listener != null) {
                    listener.onTextStateChange(true);
                }
        }
    }
}
