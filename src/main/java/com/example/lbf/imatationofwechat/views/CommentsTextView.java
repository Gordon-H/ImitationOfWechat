package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.EmojiUtil;
import com.example.lbf.imatationofwechat.adapter.MomentsListAdapter;
import com.example.lbf.imatationofwechat.beans.CommentBean;
import com.example.lbf.imatationofwechat.beans.ContactBean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lbf on 2016/7/22.
 */
public class CommentsTextView extends TextView {

    private Context context;
    private List<CommentBean> commentList;
    private int textColor;

    public List<CommentBean> getCommentList() {
        return commentList;
    }

    public interface OnContentClickListener{
        void onContentClick(int id);
    }
    private MomentsListAdapter.OnContactClickListener onContactClickListener;
    private OnContentClickListener onContentClickListener;

    public void setOnContactClickListener(MomentsListAdapter.OnContactClickListener onContactClickListener) {
        this.onContactClickListener = onContactClickListener;
    }

    public void setOnContentClickListener(OnContentClickListener onContentClickListener) {
        this.onContentClickListener = onContentClickListener;
    }

    public void setCommentList(CommentBean[] comments) {
        commentList = Arrays.asList(comments);
        setText("");
        if(commentList == null || commentList.size() == 0){
            return;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for(int i = 0; i< commentList.size(); i++){
            CommentBean commentBean = commentList.get(i);
            addCommentItem(builder,commentBean);
        }
        append(builder);
    }

    private void addCommentItem(SpannableStringBuilder builder, CommentBean commentBean) {
        String content = commentBean.getContent();
        ContactBean contactFrom = commentBean.getFrom();
        ContactBean contactTo = commentBean.getTo();
        int contactFromId = contactFrom.getId();
        int contactToId = contactTo.getId();
        addNameSpan(builder,contactFrom);
        if(contactToId != -1 && contactToId != 0){
//            回复评论,需要添加“回复XXX：”
            builder.append(" 回复 ");
            addNameSpan(builder,contactTo);
        }
        builder.append(": ");
        addContentSpan(builder,content,contactFromId);
        builder.append("\n");
    }

    private void addNameSpan(SpannableStringBuilder builder, ContactBean contactBean) {
        String name = contactBean.getName();
        final int id = contactBean.getId();
        builder.append(name);
        int start = builder.length()-name.length();
        int end = builder.length();
        NameSpan span = new NameSpan(id);
        builder.setSpan(span,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void addContentSpan(SpannableStringBuilder builder, String content, int id) {
        builder.append(EmojiUtil.convertString(context,content));
        int start = builder.length()-content.length();
        int end = builder.length();
        ContentSpan span = new ContentSpan(id);
        builder.setSpan(span,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void addComment(CommentBean commentBean){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        addCommentItem(builder,commentBean);
        append(builder);
    }

    public CommentsTextView(Context context) {
        super(context);
        init(context,null);
    }

    public CommentsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CommentsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        textColor = ContextCompat.getColor(context,R.color.praise_item);
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    class NameSpan extends ClickableSpan{
        int id;
        public NameSpan(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View widget) {
            if(onContactClickListener!=null){
                onContactClickListener.onContactClick(id);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(textColor);
            ds.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14,
                    context.getResources().getDisplayMetrics()));
            ds.setUnderlineText(false);
        }
    }
    class ContentSpan extends ClickableSpan{
        int id;
        public ContentSpan(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View widget) {
            if(onContentClickListener!=null){
                onContentClickListener.onContentClick(id);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.BLACK);
            ds.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,14,
                    context.getResources().getDisplayMetrics()));
            ds.setUnderlineText(false);
        }
    }
}
