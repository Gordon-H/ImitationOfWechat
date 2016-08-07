package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.LogUtil;

/**
 * Created by lbf on 2016/6/27.
 */
public class Search_Input_View3 extends EditText implements TextWatcher{

    private Context context;
    private boolean hasDrawableRight;
    private static int boundLeft;
    private static int boundRight;
    private Drawable deleteDrawable;
    private Drawable drawableRightNormal;
    private Drawable drawableRightHighLight;
    private static boolean isTextEmpty = true;
    private static boolean canClear = false;
    private static boolean canDrawableRightHighLight = false;
    private static boolean isDrawableRightHighLight = false;
    private boolean isFirst = true;
    private Paint paint;
    public interface OnImageStateChangeListener {
        void onImageStateChange(boolean isImageHighlight);
    }
    private OnImageStateChangeListener imageClickListener;

    public void setImageStateChangeListener(OnImageStateChangeListener imageClickListener) {
        this.imageClickListener = imageClickListener;
    }

    public Search_Input_View3(Context context) {
        this(context,null);
    }

    public Search_Input_View3(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public Search_Input_View3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.TextInputView);
        canClear = a.getBoolean(R.styleable.TextInputView_canClear,false);
        int imageRes = a.getResourceId(R.styleable.TextInputView_drawableRightHighLight,-1);
        if(imageRes != -1){
            canDrawableRightHighLight =true;
            drawableRightHighLight = getResources().getDrawable(imageRes);
        }
        a.recycle();
        init();
    }

    private void init() {
        addTextChangedListener(this);
        drawableRightNormal = getCompoundDrawables()[2];
        drawableRightNormal.setBounds(0,0,drawableRightNormal.getIntrinsicWidth()+20,
                drawableRightNormal.getIntrinsicHeight()+20);
        deleteDrawable = context.getResources().getDrawable(R.drawable.actionbar_icon_delete);
//        注意这里要执行setBounds,否则无法显示图片
        deleteDrawable.setBounds(0,0,deleteDrawable.getIntrinsicWidth(),deleteDrawable.getIntrinsicHeight());
        hasDrawableRight = drawableRightNormal != null;
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isFirst){
//            一开始设了减去paddingRight（打印结果为24），但感觉位置不太对，有点偏左，就删了。
            boundLeft = getMeasuredWidth() - deleteDrawable.getIntrinsicWidth();
            boundRight = getMeasuredWidth();
            isFirst = false;
        }
        paint.setStrokeWidth(1.0f);
        int lineHeight = (getHeight()+deleteDrawable.getIntrinsicHeight())/2+4;
//        int lineHeight = getHeight();
        canvas.drawLine(0,lineHeight,getWidth(),lineHeight,paint);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        LogUtil.i("onFocusChanged"+focused);
        if(focused){
            paint.setColor(context.getResources().getColor(R.color.editTextUnderLine));
        }else{
            paint.setColor(Color.BLACK);
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            if(x>boundLeft && x<boundRight){

                if(canDrawableRightHighLight){
                    if(isDrawableRightHighLight){
                        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],
                                drawableRightNormal,getCompoundDrawables()[3]);
                        isDrawableRightHighLight = false;
                    }else{
                        setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],
                                drawableRightHighLight,getCompoundDrawables()[3]);
                        isDrawableRightHighLight = true;
                    }
                    if(imageClickListener!=null){
                        imageClickListener.onImageStateChange(isDrawableRightHighLight);
                    }
                }
                if(isTextEmpty){
                    if(hasDrawableRight){
                        Toast.makeText(context,"Do something to support extra action!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(canClear){
//                        清除文字
                        setText("");
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }
    public interface OnTextChangeListener{
        void onTextChange(String text);
    }
    OnTextChangeListener listener;

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
//    onTextChange的执行顺序先于构造方法，因此isTextEmpty需要设为static以保证正确初始化
    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if(isTextEmpty && getText().length()>0){
            if(canClear){
                setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],
                        deleteDrawable,getCompoundDrawables()[3]);
            }
            isTextEmpty = false;
        }else if(!isTextEmpty && getText().length()==0){
            if(canClear) {
                setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                        drawableRightNormal, getCompoundDrawables()[3]);
            }
            isTextEmpty = true;
        }
        if(listener!=null){
            listener.onTextChange(getText().toString());
        }
    }
}
