package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.example.lbf.imatationofwechat.util.LogUtil;

/**
 * Created by lbf on 2016/7/18.
 */
public class ZoomImageView extends ImageView {

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector mGestureDetector;
    private float scaleFactor = 1;
    private Bitmap bitmap;
    private Matrix matrix;

    public ZoomImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean rel = mGestureDetector.onTouchEvent(event);
        return mScaleGestureDetector.onTouchEvent(event)
                ||rel;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (matrix == null && bitmap!=null) {
            matrix = new Matrix();
//            imageBound = new RectF((getWidth()-bitmap.getWidth())/2,(getHeight()-bitmap.getHeight())/2,
//                    (getWidth()+bitmap.getWidth())/2,(getHeight()+bitmap.getHeight())/2);
            imageBound = new RectF(0,0,bitmap.getWidth(),bitmap.getHeight());
            newBound = new RectF();
            adjustScaleFactor(1,getWidth()/2,getHeight()/2);
        }
        super.onDraw(canvas);
    }
    private RectF imageBound,newBound;
    private void adjustScaleFactor(float factor,float x, float y) {
            x = getWidth()/2;
            y = getHeight()/2;
        factor *= getWidth() * 1.0 / bitmap.getWidth();
//            平移到中点
        matrix.setTranslate((getWidth() - bitmap.getWidth())/2,(getHeight() - bitmap.getHeight())/2);
        matrix.postScale(factor, factor, x, y);
        matrix.mapRect(newBound,imageBound);
        LogUtil.i("imageBound = "+newBound.left+" "+newBound.right+" "+newBound.top+" "+newBound.bottom);
        setImageMatrix(matrix);
    }
    public interface OnSingleTapListener{
        void onSingleTap();
    }
    private OnSingleTapListener onSingleTapListener;

    public void setOnSingleTapListener(OnSingleTapListener onSingleTapListener) {
        this.onSingleTapListener = onSingleTapListener;
    }

    private void init(Context context) {
        setScaleType(ScaleType.MATRIX);
        scaleFactor = 1;
        mScaleGestureDetector = new ScaleGestureDetector(context,
                new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    float factor;
                    float span;
                    float focusX;
                    float focusY;
                    @Override
                    public boolean onScaleBegin(ScaleGestureDetector detector) {
                        span = detector.getCurrentSpan();
                        LogUtil.i("span = "+ span);
                        return super.onScaleBegin(detector);
                    }

                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        factor = detector.getScaleFactor();
//                        factor *= 1+(factor-1)/3;
//                        factor = 1+(detector.getCurrentSpan()-span)
//                                /(scaleFactor*getWidth());
                        LogUtil.i("factor = "+factor);
                        if(scaleFactor*factor > 0.8f){
                            focusX = detector.getFocusX();
                            focusY = detector.getFocusY();
                            adjustScaleFactor(scaleFactor*factor,focusX,focusY);
                        }

                        return super.onScale(detector);
                    }
                    @Override
                    public void onScaleEnd(ScaleGestureDetector detector) {
                        super.onScaleEnd(detector);
                        scaleFactor *= factor;
                        LogUtil.i("scaleFactor = "+scaleFactor);
                        if(scaleFactor>2){
                            adjustScaleFactor(2f, focusX, focusY);
                            scaleFactor = 2f;
                        } else if(scaleFactor<1){
                            adjustScaleFactor(1f, focusX, focusY);
                            scaleFactor = 1f;
                        }
                    }
                });
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                LogUtil.i("distanceX = " + distanceX);
                LogUtil.i("distanceY = " + distanceY);
                adjustTranslation(-distanceX,-distanceY);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if(onSingleTapListener!=null){
                    onSingleTapListener.onSingleTap();
                }
                return super.onSingleTapUp(e);
            }
        });
    }

    private void adjustTranslation(float translationX, float translationY) {
//        限制在边界内
        if(translationX > 0 && -translationX<newBound.left
                ||translationX < 0 && getWidth()-translationX>newBound.right){
            translationX = 0;
        }
        if(translationY > 0 && -translationY<newBound.top
                ||translationY < 0 && getHeight()-translationY>newBound.bottom){
            translationY = 0;
        }
        if(translationX == 0 && translationY == 0){
            return;
        }
        matrix.postTranslate(translationX,translationY);
        matrix.mapRect(newBound,imageBound);
        LogUtil.i("imageBound = "+newBound.left+" "+newBound.right+" "+newBound.top+" "+newBound.bottom);
        setImageMatrix(matrix);
    }

}
