package com.example.lbf.imatationofwechat.shake;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.shakeSetting.ShakeSettingActivity;
import com.example.lbf.imatationofwechat.util.CommonUtil;
import com.example.lbf.imatationofwechat.util.DataUtil;

/**
 * Created by lbf on 2016/7/29.
 */
public class ShakePresenter implements ShakeContract.Presenter,SensorEventListener {

    private static final int minValue = 15;
    private static final long[] vibratePattern = {100,200,100,200};
    //    -1表示只震动一次，非-1表示从pattern的指定下标开始重复振动
    private static final int vibrateRepeat = -1;

    private Context mContext;
    private ShakeContract.View mView;

    private SensorManager sensorManager;
    private Sensor vibrateSensor;
    //    实现震动的类
    private Vibrator vibrator;
    private Animation bounceUpAnimation;
    private Animation bounceDownAnimation;
    //    是否允许摇动，用来防止短时间内连续触发事件
    private boolean allowShake = true;
    //    产生音效的类
    private SoundPool mSoundPool;
    //    音效的ID
    private int soundId;
    //    是否开启音效
    private boolean useSound;
    //    背景图
    private String currentImage;

    public ShakePresenter(Context context, ShakeContract.View view) {
        mContext = context;
        mView = CommonUtil.checkNotNull(view,"view cannot be null!");
        mView.setPresenter(this);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            mView.setHiddenBackground(bitmap);
        }
    };

    @Override
    public void start() {
    }

    @Override
    public void setupAnimation() {
        bounceUpAnimation = AnimationUtils.loadAnimation(mContext, R.anim.translate_up_down);
        bounceDownAnimation = AnimationUtils.loadAnimation(mContext,R.anim.translate_down_up);
        bounceUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                allowShake = false;
                vibrator.vibrate(vibratePattern, vibrateRepeat);
                if(useSound){
                    mSoundPool.play(soundId,1,1,0,0,1);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                allowShake = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void create() {
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        vibrateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        setupAnimation();
//        默认的背景图
        currentImage = ShakeSettingActivity.DEFAULT_IMAGE;
    }

    @Override
    public void resume() {
        sensorManager.registerListener(this, vibrateSensor,SensorManager.SENSOR_DELAY_NORMAL);
        soundId = mSoundPool.load(mContext,R.raw.shake_sound_male,0);
        SharedPreferences preferences = mContext.getSharedPreferences(DataUtil.PREF_NAME,Context.MODE_PRIVATE);
        useSound = preferences.getBoolean(DataUtil.PREF_KEY_SHAKE_USE_SOUND,true);
        final String imageUrl = preferences.getString(DataUtil.PREF_KEY_SHAKE_BACKGROUND,ShakeSettingActivity.DEFAULT_IMAGE);
        if(imageUrl != currentImage){
//            如果图片不同则进行更新
            currentImage = imageUrl;
            if(imageUrl.equals(ShakeSettingActivity.DEFAULT_IMAGE)){
//                重置为默认图片
                mView.setHiddenBackground(R.drawable.a5z);
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    开启一个线程加载图片
                    Bitmap bitmap = BitmapFactory.decodeFile(imageUrl);
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    mHandler.sendMessage(message);
                }
            }).start();
        }
    }

    @Override
    public void pause() {
        sensorManager.unregisterListener(this);
        mSoundPool.release();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!allowShake){
            return;
        }
        float[] values = event.values;
//        z轴减去重力加速度9.8
        if(Math.abs(values[0])>minValue||Math.abs(values[1])>minValue||Math.abs(values[2]-9.8)>minValue){
            mView.startAnimation(bounceUpAnimation,bounceDownAnimation);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
