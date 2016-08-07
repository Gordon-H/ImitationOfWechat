package com.example.lbf.imatationofwechat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.EmojiUtil;
import com.example.lbf.imatationofwechat.util.ImageLoader;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.beans.MessageBean;
import com.example.lbf.imatationofwechat.beans.ImageSize;
import com.example.lbf.imatationofwechat.data.source.local.WeChatDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lbf on 2016/6/29.
 */
public class MessagesListAdapter extends CommonAdapter<MessageBean>{

    private static final int TYPE_SEND_TEXT = 0;
    private static final int TYPE_RECEIVE_TEXT = 1;
    private static final int TYPE_SEND_IMAGE = 2;
    private static final int TYPE_RECEIVE_IMAGE = 3;
    private int position;
    private int contactImage;
    private int userImage;
    private ImageLoader imageLoader;
    private Context context;
    public int getPosition() {
        return position;
    }
    public interface OnItemDoubleTapListener{
        boolean onItemDoubleTap(String content);
    }
    private OnItemDoubleTapListener onItemDoubleTapListener;

    public void setOnItemDoubleTapListener(OnItemDoubleTapListener onItemDoubleTapListener) {
        this.onItemDoubleTapListener = onItemDoubleTapListener;
    }
    public interface OnImageTapListener{
        boolean onImageTap(View view,String path);
    }
    private OnImageTapListener onImageTapListener;

    public void setOnImageTapListener(OnImageTapListener onImageTapListener) {
        this.onImageTapListener = onImageTapListener;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private GestureDetector detector;
    public MessagesListAdapter(Context context, List<MessageBean> beanList,
                               int contactImage, int userImage) {
        super(context,beanList);
        this.contactImage = contactImage;
        this.userImage = userImage;
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener();
        detector = new GestureDetector(context,listener);
        setTypeSupport(new MultiItemTypeSupport<MessageBean>() {
            @Override
            public int getLayoutId(int itemType) {
                switch (itemType){
                    case TYPE_SEND_TEXT:
                        return R.layout.chats_aty_item_send_text;
                    case TYPE_RECEIVE_TEXT:
                        return R.layout.chats_aty_item_receive_text;
                    case TYPE_SEND_IMAGE:
                        return R.layout.chats_aty_item_send_image;
                    case TYPE_RECEIVE_IMAGE:
                        return R.layout.chats_aty_item_receive_image;
                    default:
                        return -1;
                }
            }

            @Override
            public int getItemType(int position, MessageBean bean) {
                int type = bean.getType();
                if(bean.isSend()){
                    if(type == WeChatDBHelper.MSG_TYPE_TEXT){
                        return TYPE_SEND_TEXT;
                    }else{
                        return TYPE_SEND_IMAGE;
                    }
                } else{
                    if(type == WeChatDBHelper.MSG_TYPE_TEXT){
                        return TYPE_RECEIVE_TEXT;
                    }else{
                        return TYPE_RECEIVE_IMAGE;
                    }
                }
            }
        });
    }

    @Override
    protected void convert(final CommonViewHolder holder, final MessageBean bean) {
        boolean displayTime = bean.isDisPlaytime();
        int type = holder.getItemViewType();
        TextView time = (TextView) holder.get(R.id.chat_aty_item_time);
        if(displayTime){
            time.setVisibility(View.VISIBLE);
            time.setText(formatTime(bean.getTime()));
        }else{
            time.setVisibility(View.GONE);
        }
        if(bean.isSend()){
            holder.setImageRes(R.id.chat_aty_item_image,userImage );
        }else{
            holder.setImageRes(R.id.chat_aty_item_image,contactImage );
        }
        if(bean.getType() == WeChatDBHelper.MSG_TYPE_TEXT){
            TextView textView = (TextView) holder.get(R.id.chat_aty_item_content);
            final String content = bean.getContent();
            final SpannableString string = EmojiUtil.convertString(context,content);
            textView.setText(string);
            textView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(0,R.id.context_set_remarks,1,"设置备注及标签");
                }
            });

            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(onItemDoubleTapListener!=null){
                        GestureDetector.OnDoubleTapListener listener = new GestureDetector.OnDoubleTapListener() {
                            @Override
                            public boolean onSingleTapConfirmed(MotionEvent e) {
                                return false;
                            }

                            @Override
                            public boolean onDoubleTap(MotionEvent e) {
                                onItemDoubleTapListener.onItemDoubleTap(content);
                                return false;
                            }

                            @Override
                            public boolean onDoubleTapEvent(MotionEvent e) {
                                return false;
                            }
                        };
                        detector.setOnDoubleTapListener(listener);
                        return detector.onTouchEvent(event);
                    }
                    return false;
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setPosition(holder.getAdapterPosition());
                    return false;
                }
            });
        }else{
            ImageView imageView = (ImageView) holder.get(R.id.chat_aty_item_content);
            final String content = bean.getContent();
            ImageSize imageSize = getImageSize(content);
            Bitmap bitmap = ImageLoader.decodeBitmapFromPath(
                    content,imageSize.getWidth(),imageSize.getHeight());
            Bitmap mOut = Bitmap.createBitmap(imageSize.getWidth(),
                    imageSize.getHeight(), Bitmap.Config.ARGB_8888);
            Rect bound = new Rect(0,0,mOut.getWidth(),mOut.getHeight());
            Canvas canvas = new Canvas(mOut);
            Paint mPaint = new Paint();
            mPaint.setAntiAlias(true);
//            Bitmap bg = BitmapFactory.decodeResource(context.getResources(),R.drawable.rx);
            NinePatchDrawable bg = (NinePatchDrawable) ContextCompat.getDrawable(context,R.drawable.rx);
            bg.setBounds(bound);
            bg.draw(canvas);
//            canvas.drawBitmap(bg,null,bound,mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            float left = (imageSize.getWidth()-bitmap.getWidth())/2;
            float top = (imageSize.getHeight()-bitmap.getHeight())/2;
            canvas.drawBitmap(bitmap,left,top,mPaint);

            LogUtil.i("iamgeSize = "+imageSize.getWidth() + "   "+imageSize.getHeight());
            LogUtil.i("bitmapSize = "+bitmap.getWidth() + "   "+bitmap.getHeight());
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = imageSize.getWidth();
            params.height = imageSize.getHeight();
            imageView.setLayoutParams(params);
            imageView.setImageBitmap(mOut);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onImageTapListener != null){
                        onImageTapListener.onImageTap(v,content);
                    }
                }
            });
//            imageLoader.loadImage(content,imageView);
        }


    }
    private ImageSize getImageSize(String path) {
        ImageSize imageSize = new ImageSize();
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bitmapOptions);
        int outWidth = bitmapOptions.outWidth;
        int outHeight = bitmapOptions.outHeight;
        int maxWidth = 300;
        int maxHeight = 300;
        int minWidth = 100;
        int minHeight = 100;
        if (outWidth / maxWidth > outHeight / maxHeight) {
            if (outWidth >= maxWidth) {//
                imageSize.setWidth(maxWidth);
                imageSize.setHeight(outHeight * maxWidth / outWidth);
            } else {
                imageSize.setWidth(outWidth);
                imageSize.setHeight(outHeight);
            }
            if (outHeight < minHeight) {
                imageSize.setHeight(minHeight);
                int width = outWidth * minHeight / outHeight;
                if (width > maxWidth) {
                    imageSize.setWidth(maxWidth);
                } else {
                    imageSize.setWidth(width);
                }
            }
        } else {
            if (outHeight >= maxHeight) {
                imageSize.setHeight(maxHeight);
                imageSize.setWidth(outWidth * maxHeight / outHeight);
            } else {
                imageSize.setHeight(outHeight);
                imageSize.setWidth(outWidth);
            }
            if (outWidth < minWidth) {
                imageSize.setWidth(minWidth);
                int height = outHeight * minWidth / outWidth;
                if (height > maxHeight) {
                    imageSize.setHeight(maxHeight);
                } else {
                    imageSize.setHeight(height);
                }
            }
        }
        return imageSize;
    }

    private String formatTime(long time) {
        Calendar currentCal = Calendar.getInstance();
        Calendar msgCal = Calendar.getInstance();
        msgCal.setTimeInMillis(time);
        int interval = currentCal.get(Calendar.DAY_OF_YEAR) - msgCal.get(Calendar.DAY_OF_YEAR);
        SimpleDateFormat ft;
        if(interval > 1){
            ft = new SimpleDateFormat("MM/dd/yyyy");
            return ft.format(msgCal.getTime());
        }else if(interval ==1){
            ft = new SimpleDateFormat("HH:mm");
            return "昨天 "+ft.format(msgCal.getTime());
        }else{
            ft = new SimpleDateFormat("HH:mm");
            return ft.format(msgCal.getTime());
        }
    }
}
