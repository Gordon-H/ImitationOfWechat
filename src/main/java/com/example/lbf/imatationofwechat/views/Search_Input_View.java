package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;

/**
 * Created by lbf on 2016/6/27.
 */
public class Search_Input_View extends EditText implements TextWatcher{

    private Context context;
    private boolean hasDrawableRight;
    private static int boundLeft;
    private static int boundRight;
    private Drawable deleteDrawable;
    private Drawable drawableRightNormal;
    private static boolean isTextEmpty = true;
    private boolean isFirst = true;

    public Search_Input_View(Context context) {
        this(context,null);
    }

    public Search_Input_View(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public Search_Input_View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        addTextChangedListener(this);
        drawableRightNormal = getCompoundDrawables()[2];
        deleteDrawable = context.getResources().getDrawable(R.drawable.actionbar_icon_delete);
//        注意这里要执行setBounds,否则无法显示图片
        deleteDrawable.setBounds(0,0,deleteDrawable.getIntrinsicWidth(),deleteDrawable.getIntrinsicHeight());
        hasDrawableRight = drawableRightNormal != null;
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
        Paint paint = new Paint();
        paint.setStrokeWidth(1.0f);
        paint.setColor(context.getResources().getColor(R.color.editTextUnderLine));
        canvas.drawLine(0,getHeight()-20,getWidth()-10,getHeight()-20,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            if(x>boundLeft && x<boundRight){
                if(isTextEmpty){
                    if(hasDrawableRight){
//                        语音输入
                        Toast.makeText(context,"Do something to support voice input",Toast.LENGTH_SHORT).show();
                    }
                }else{
//                    清除文字
                    setText("");
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
            setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],
                    deleteDrawable,getCompoundDrawables()[3]);
            isTextEmpty = false;
        }else if(!isTextEmpty && getText().length()==0){
            setCompoundDrawables(getCompoundDrawables()[0],getCompoundDrawables()[1],
                    drawableRightNormal,getCompoundDrawables()[3]);
            isTextEmpty = true;
        }
        if(listener!=null){
            listener.onTextChange(getText().toString());
        }
    }
}
