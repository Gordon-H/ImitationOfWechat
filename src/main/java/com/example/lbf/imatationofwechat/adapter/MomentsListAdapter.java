package com.example.lbf.imatationofwechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.beans.CommentBean;
import com.example.lbf.imatationofwechat.beans.ContactBean;
import com.example.lbf.imatationofwechat.beans.ImageSize;
import com.example.lbf.imatationofwechat.beans.MomentBean;
import com.example.lbf.imatationofwechat.common.CommonAdapter;
import com.example.lbf.imatationofwechat.common.CommonViewHolder;
import com.example.lbf.imatationofwechat.data.source.local.ChatsLocalDataSource;
import com.example.lbf.imatationofwechat.util.ImageLoader;
import com.example.lbf.imatationofwechat.views.CommentsTextView;
import com.example.lbf.imatationofwechat.views.ExpandTextView;
import com.example.lbf.imatationofwechat.views.MultiItemTextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lbf on 2016/6/29.
 */
public class MomentsListAdapter extends CommonAdapter<MomentBean> {
    private static final int TYPE_ITEM_HEAD = 0;
    private static final int TYPE_ITEM_NORMAL = 1;
    private CommentsTextView targetCommentView;
    private MultiItemTextView targetFavorsView;
    private MomentBean targetBean;
    private ImageLoader imageLoader;
    private Context context;

    public CommentsTextView getTargetCommentView() {
        return targetCommentView;
    }

    public interface OnContactClickListener {
        boolean onContactClick(int id);
    }

    private OnContactClickListener onContactClickListener;

    public void setOnContactClickListener(OnContactClickListener onContactClickListener) {
        this.onContactClickListener = onContactClickListener;
    }

    public interface OnCommentStateListener {
        boolean onCommentStart(int targetContactId);
    }

    private OnCommentStateListener onCommentStateListener;

    public void setOnCommentStateListener(OnCommentStateListener onCommentStateListener) {
        this.onCommentStateListener = onCommentStateListener;
    }

    public MomentsListAdapter(Context context, List<MomentBean> beanList) {
        super(context, beanList);
        this.context = context;
        imageLoader = ImageLoader.getInstance();

        setTypeSupport(new MultiItemTypeSupport<MomentBean>() {
            @Override
            public int getLayoutId(int itemType) {
                switch (itemType) {
                    case TYPE_ITEM_HEAD:
                        return R.layout.circle_item_head;
                    case TYPE_ITEM_NORMAL:
                        return R.layout.circle_item_normal;
                    default:
                        return -1;
                }
            }

            @Override
            public int getItemType(int position, MomentBean bean) {
                if (position == 0) {
                    return TYPE_ITEM_HEAD;
                } else {
                    return TYPE_ITEM_NORMAL;
                }
            }
        });
    }

    @Override
    protected void convert(final CommonViewHolder holder, final MomentBean bean) {
        int type = holder.getItemViewType();
        if (type == TYPE_ITEM_HEAD) {
            return;
        }
        String contactName = bean.getContact().getName();
        int contactImage = bean.getContact().getImage();
        int contactId = bean.getContact().getId();
        String content = bean.getContent();
        String link = bean.getLink();
        String[] images = bean.getImages();
        final ContactBean[] favors = bean.getFavors();
        CommentBean[] comments = bean.getComments();
        holder.setText(R.id.tv_time, formatTime(bean.getTime()));
        holder.setImageRes(R.id.iv_profile_photo, contactImage);
        holder.setText(R.id.tv_name, contactName);
        ((ExpandTextView) holder.get(R.id.tv_content)).setContent(content);
        final MultiItemTextView favorTextView = ((MultiItemTextView) holder.get(R.id.lv_favor));
        final CommentsTextView commentsTextView = ((CommentsTextView) holder.get(R.id.lv_comment));
        holder.setOnClickLisener(R.id.iv_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetCommentView = commentsTextView;
                targetFavorsView = favorTextView;
                targetBean = bean;
                showOrHidePopupWindow(v, holder);
            }
        });
        convertLink(holder, link);
        convertImages(holder, images);
        favorTextView.setContactList(favors);
        favorTextView.setOnContactClickListener(onContactClickListener);
        commentsTextView.setCommentList(comments);
        commentsTextView.setOnContactClickListener(onContactClickListener);
        commentsTextView.setOnContentClickListener(new CommentsTextView.OnContentClickListener() {
            @Override
            public void onContentClick(int id) {
                targetCommentView = commentsTextView;
                onCommentStateListener.onCommentStart(id);
            }
        });

//        if(bean.getType() == WeChatDBHelper.MSG_TYPE_TEXT){
//            TextView textView = (TextView) holder.get(R.id.chat_aty_item_content);
//            textView.getMaxLines()
//            final String content = bean.getContent();
//            final SpannableString string = EmojiUtil.convertString(context,content);
//            textView.setText(string);
//            textView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//                @Override
//                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                    menu.add(0,R.id.context_set_remarks,1,"设置备注及标签");
//                }
//            });

    }
    private PopupWindow popupWindow;
    private void showOrHidePopupWindow(View v, final CommonViewHolder holder) {
        if(popupWindow == null){
            View content = LayoutInflater.from(context).inflate(R.layout.popup_moment_list, null);
            popupWindow = new PopupWindow(content, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.PopupMomentListMoreAnimation);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
            popupWindow.setOutsideTouchable(true);
            View viewFavor = content.findViewById(R.id.ll_momentList_moreFavor);
            final TextView tvMoreFavor = (TextView) content.findViewById(R.id.tv_momentList_moreFavor);
            View viewComment = content.findViewById(R.id.ll_momentList_moreComment);
            ChatsLocalDataSource source = ChatsLocalDataSource.getInstance(context);
            final ContactBean contactBean = source.getContactInfo(0);
            viewFavor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (targetBean.isFavored()) {
                        tvMoreFavor.setText("赞");
                        targetFavorsView.removeFavor(contactBean);
                    } else {
                        tvMoreFavor.setText("取消");
                        targetFavorsView.addFavor(contactBean);
                    }
                    targetBean.setFavored(!targetBean.isFavored());
                    popupWindow.dismiss();
                }
            });
            viewComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCommentStateListener.onCommentStart(-1);
                    popupWindow.dismiss();
                }
            });
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
        View content = popupWindow.getContentView();
        TextView tvMoreFavor = (TextView) content.findViewById(R.id.tv_momentList_moreFavor);
        //        根据每个item的bean（是否有赞）改变popupwindow的内容
        if (targetBean.isFavored()) {
            tvMoreFavor.setText("取消");
        } else {
            tvMoreFavor.setText("赞");
        }
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }else{
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0],location[1]);
        }


    }

    private void convertImages(CommonViewHolder holder, String[] images) {
        TextView textView;
    }

    private void convertLink(final CommonViewHolder holder, String link) {
        if (TextUtils.isEmpty(link)) {
            holder.get(R.id.ll_list_link).setVisibility(View.GONE);
            holder.get(R.id.tv_url_tip).setVisibility(View.GONE);
            return;
        }
        holder.get(R.id.ll_list_link).setVisibility(View.VISIBLE);
        holder.get(R.id.tv_url_tip).setVisibility(View.VISIBLE);
        final String[] strs = link.split("-");
        holder.setImageRes(R.id.iv_link_image, Integer.parseInt(strs[0]));
        holder.setText(R.id.tv_link_text, strs[1]);
        holder.get(R.id.ll_list_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(strs[2]));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "link error!", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
        if (outWidth / maxWidth > outHeight / maxHeight) {//
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
        if (interval > 1) {
            ft = new SimpleDateFormat("MM/dd/yyyy");
            return ft.format(msgCal.getTime());
        } else if (interval == 1) {
            ft = new SimpleDateFormat("HH:MM");
            return "昨天 " + ft.format(msgCal.getTime());
        } else {
            ft = new SimpleDateFormat("HH:MM");
            return ft.format(msgCal.getTime());
        }
    }
}
