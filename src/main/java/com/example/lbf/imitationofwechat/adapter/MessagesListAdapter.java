package com.example.lbf.imitationofwechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imitationofwechat.base.BaseAdapter;
import com.example.lbf.imitationofwechat.base.BaseViewHolder;
import com.example.lbf.imitationofwechat.module.userInfo.UserInfoActivity;
import com.example.lbf.imitationofwechat.util.CommonUtil;
import com.example.lbf.imitationofwechat.util.DisplayUtil;
import com.example.lbf.imitationofwechat.util.EmojiUtil;
import com.example.lbf.imitationofwechat.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMImageMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.newim.bean.BmobIMSendStatus;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by lbf on 2016/6/29.
 */
public class MessagesListAdapter extends BaseAdapter<BmobIMMessage> {

    private static final int TYPE_SEND_TEXT = 0;
    private static final int TYPE_RECEIVE_TEXT = 1;
    private static final int TYPE_SEND_IMAGE = 2;
    private static final int TYPE_RECEIVE_IMAGE = 3;
    private static final int TYPE_AGREE = 4;
    private static final int TYPE_RECEIVE_VOICE = 5;
    private static final int TYPE_SEND_VOICE = 6;
    //    显示时间的间隔
    private static final long TIME_INTERVAL = 1000 * 60 * 5;
    private String currentUid = "";
    private int position;
    private Context context;
    private ImageLoader imageLoader;

    public int getPosition() {
        return position;
    }

    public interface OnItemDoubleTapListener {
        boolean onItemDoubleTap(String content);
    }

    private OnItemDoubleTapListener onItemDoubleTapListener;

    public void setOnItemDoubleTapListener(OnItemDoubleTapListener onItemDoubleTapListener) {
        this.onItemDoubleTapListener = onItemDoubleTapListener;
    }

    public interface OnImageTapListener {
        boolean onImageTap(View view, String path);
    }

    private OnImageTapListener onImageTapListener;

    public void setOnImageTapListener(OnImageTapListener onImageTapListener) {
        this.onImageTapListener = onImageTapListener;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private GestureDetector detector;

    public MessagesListAdapter(Context context) {
        super(context);
        try {
            currentUid = BmobUser.getCurrentUser(context).getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = context;
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener();
        detector = new GestureDetector(context, listener);
        imageLoader = ImageLoader.getInstance();
        setTypeSupport(new MultiItemTypeSupport<BmobIMMessage>() {
            @Override
            public int getLayoutId(int itemType) {
                switch (itemType) {
                    case TYPE_SEND_TEXT:
                        return R.layout.item_chat_send_text;
                    case TYPE_RECEIVE_TEXT:
                        return R.layout.item_chat_receive_text;
                    case TYPE_SEND_IMAGE:
                        return R.layout.item_chat_send_image;
                    case TYPE_RECEIVE_IMAGE:
                        return R.layout.item_chat_receive_image;
                    case TYPE_SEND_VOICE:
                        return R.layout.item_chat_send_voice;
                    case TYPE_RECEIVE_VOICE:
                        return R.layout.item_chat_receive_voice;
                    case TYPE_AGREE:
                        return R.layout.item_chat_agree;

                    default:
                        return -1;
                }
            }

            @Override
            public int getItemType(int position, BmobIMMessage message) {
                if (message.getMsgType().equals(BmobIMMessageType.IMAGE.getType())) {
                    return message.getFromId().equals(currentUid) ? TYPE_SEND_IMAGE : TYPE_RECEIVE_IMAGE;
                } else if (message.getMsgType().equals(BmobIMMessageType.TEXT.getType())) {
                    return message.getFromId().equals(currentUid) ? TYPE_SEND_TEXT : TYPE_RECEIVE_TEXT;
                } else if (message.getMsgType().equals(BmobIMMessageType.VOICE.getType())) {
                    return message.getFromId().equals(currentUid) ? TYPE_SEND_VOICE : TYPE_RECEIVE_VOICE;
                } else if (message.getMsgType().equals("agree")) {
                    return TYPE_AGREE;
                } else {
                    return -1;

                }
            }
        });
    }

    @Override
    protected void convert(final BaseViewHolder holder, final BmobIMMessage imMessage) {
        int type = holder.getItemViewType();
        convertTime(holder, imMessage);

        if (type == TYPE_AGREE) {
            holder.setText(R.id.ivContent, imMessage.getContent());
            return;
        }
        convertAvatar(holder, imMessage);
        convertSendState(holder, imMessage);

        if (type == TYPE_RECEIVE_TEXT ||
                type == TYPE_SEND_TEXT) {
            convertMsgTxt(holder, imMessage);

        } else if (type == TYPE_RECEIVE_IMAGE ||
                type == TYPE_SEND_IMAGE) {
            convertMsgImage(holder, imMessage);
        } else if (type == TYPE_RECEIVE_VOICE ||
                type == TYPE_SEND_VOICE) {
            convertMsgVoice(holder, imMessage);
        }
    }

    private void convertMsgVoice(final BaseViewHolder holder, BmobIMMessage imMessage) {
        boolean b = imMessage.getFromId().equals(currentUid);
        final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(b, imMessage);
        holder.setText(R.id.tvDuration, message.getDuration() + "\''");
        ImageView ivContent = (ImageView) holder.get(R.id.ivContent);
        ViewGroup.LayoutParams params = ivContent.getLayoutParams();
        final int MIN_WIDTH = DisplayUtil.dip2px(context, 60);
        params.width = (int) (MIN_WIDTH + message.getDuration() / 60 * DisplayUtil.dip2px(context, 180));
        ivContent.setLayoutParams(params);
        ivContent.setOnClickListener(new NewRecordPlayClickListener(context, message, ivContent));
        boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
        if (!isExists) {//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
            BmobDownloadManager downloadTask = new BmobDownloadManager(context, imMessage, new FileDownloadListener() {

                @Override
                public void onStart() {
                    holder.setVisibility(R.id.tvDuration, View.GONE);
                    holder.setVisibility(R.id.ivContent, View.INVISIBLE);//只有下载完成才显示播放的按钮
                }

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        holder.setVisibility(R.id.tvDuration, View.VISIBLE);
                        holder.setVisibility(R.id.ivContent, View.VISIBLE);
                    }
                }
            });
            downloadTask.execute(message.getContent());
        } else {
            holder.setVisibility(R.id.tvDuration, View.VISIBLE);
            holder.setVisibility(R.id.ivContent, View.VISIBLE);
        }
    }

    private void convertMsgImage(BaseViewHolder holder, BmobIMMessage imMessage) {
        ImageView imageView = (ImageView) holder.get(R.id.ivContent);
        boolean isSend = holder.getItemViewType() == TYPE_SEND_IMAGE;
        final BmobIMImageMessage message = BmobIMImageMessage.buildFromDB(isSend, imMessage);
        final String content = TextUtils.isEmpty(message.getRemoteUrl()) ?
                message.getLocalPath() : message.getRemoteUrl();
        LogUtil.i("image message content = " + content);
        final ImageSize imageSize = getImageSize(content);

        Bitmap mOut = Bitmap.createBitmap(imageSize.getWidth(),
                imageSize.getHeight(), Bitmap.Config.ARGB_8888);
        Rect bound = new Rect(0, 0, mOut.getWidth(), mOut.getHeight());
        final Canvas canvas = new Canvas(mOut);
        final Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
//            Bitmap bg = BitmapFactory.decodeResource(context.getResources(),R.drawable.rx);
        NinePatchDrawable bg;
        if (isSend) {
            bg = (NinePatchDrawable) ContextCompat.getDrawable(context, R.drawable.rx);
        } else {
            bg = (NinePatchDrawable) ContextCompat.getDrawable(context, R.drawable.ru);
        }
        bg.setBounds(bound);
        bg.draw(canvas);
//            canvas.drawBitmap(bg,null,bound,mPaint);

        imageLoader.loadImage(content, imageSize, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                super.onLoadingComplete(imageUri, view, bitmap);
                float left = (imageSize.getWidth() - bitmap.getWidth()) / 2;
                float top = (imageSize.getHeight() - bitmap.getHeight()) / 2;
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap, left, top, mPaint);
            }
        });

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = imageSize.getWidth();
        params.height = imageSize.getHeight();
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(mOut);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onImageTapListener != null) {
                    onImageTapListener.onImageTap(v, content);
                }
            }
        });
    }

    private void convertMsgTxt(final BaseViewHolder holder, BmobIMMessage imMessage) {
        TextView textView = (TextView) holder.get(R.id.ivContent);
        final String content = imMessage.getContent();
        final SpannableString string = EmojiUtil.convertString(context, content);
        textView.setText(string);
        textView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, R.id.context_set_remarks, 1, "删除");
            }
        });

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (onItemDoubleTapListener != null) {
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
    }

    private void convertSendState(BaseViewHolder holder, BmobIMMessage imMessage) {
        if (imMessage.getSendStatus() == BmobIMSendStatus.SENDING.getStatus()) {
            holder.setVisibility(R.id.progressBar, View.VISIBLE)
                    .setVisibility(R.id.imgBtResend, View.GONE);
        } else if (imMessage.getSendStatus() == BmobIMSendStatus.SENDED.getStatus()) {
            holder.setVisibility(R.id.progressBar, View.GONE)
                    .setVisibility(R.id.imgBtResend, View.GONE);
        } else if (imMessage.getSendStatus() == BmobIMSendStatus.SENDFAILED.getStatus()) {
            holder.setVisibility(R.id.progressBar, View.GONE)
                    .setVisibility(R.id.imgBtResend, View.VISIBLE);
        }
    }

    private void convertAvatar(BaseViewHolder holder, final BmobIMMessage imMessage) {
//            String avatarUrl = imMessage.getBmobIMUserInfo().getAvatar().trim();
        String avatarUrl = BmobIM.getInstance().getUserInfo(imMessage.getFromId()).getAvatar();
        ImageView avatarView = (ImageView) holder.get(R.id.ivAvatar);
        imageLoader.displayImage(avatarUrl, avatarView);
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(UserInfoActivity.KEY_IS_FRIEND, true);
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    private void convertTime(BaseViewHolder holder, BmobIMMessage imMessage) {
        boolean displayTime = shouldShowTime(holder.getAdapterPosition());
        if (displayTime) {
            holder.setVisibility(R.id.tvTime, View.VISIBLE)
                    .setText(R.id.tvTime, CommonUtil.formatTime(imMessage.getCreateTime()));
        } else {
            holder.setVisibility(R.id.tvTime, View.GONE);
        }
    }

    private ImageSize getImageSize(final String imageUri) {
        ImageSize imageSize;
        Uri uri = Uri.parse(imageUri);
        final BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        if (uri.getScheme().equals("file")) {
            BitmapFactory.decodeFile(uri.getPath(), bitmapOptions);
        } else if (uri.getScheme().equals("http")) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(imageUri);
                        InputStream is = url.openStream();
                        BitmapFactory.decodeStream(is, null, bitmapOptions);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int outWidth = bitmapOptions.outWidth;
        int outHeight = bitmapOptions.outHeight;
        int maxWidth = 300;
        int maxHeight = 300;
        int minWidth = 100;
        int minHeight = 100;
        int resultWidth, resultHeight;
        if (outWidth / maxWidth > outHeight / maxHeight) {
            if (outWidth >= maxWidth) {//
                resultWidth = maxWidth;
                resultHeight = outHeight * maxWidth / outWidth;
            } else {
                resultWidth = outWidth;
                resultHeight = outHeight;
            }
            if (outHeight < minHeight) {
                resultHeight = minHeight;
                int width = outWidth * minHeight / outHeight;
                if (width > maxWidth) {
                    resultWidth = maxWidth;
                } else {
                    resultWidth = width;
                }
            }
        } else {
            if (outHeight >= maxHeight) {
                resultHeight = maxHeight;
                resultWidth = outWidth * maxHeight / outHeight;
            } else {
                resultHeight = outHeight;
                resultWidth = outWidth;
            }
            if (outWidth < minWidth) {
                resultWidth = minWidth;
                int height = outHeight * minWidth / outWidth;
                if (height > maxHeight) {
                    resultHeight = maxHeight;
                } else {
                    resultHeight = height;
                }
            }
        }
        imageSize = new ImageSize(resultWidth, resultHeight);
        return imageSize;
    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = beanList.get(position - 1).getCreateTime();
        long curTime = beanList.get(position).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}
