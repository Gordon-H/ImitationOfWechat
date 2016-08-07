package com.example.lbf.imatationofwechat.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.lbf.imatationofwechat.R;
import com.example.lbf.imatationofwechat.util.LogUtil;
import com.example.lbf.imatationofwechat.adapter.MomentsListAdapter;
import com.example.lbf.imatationofwechat.beans.ContactBean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lbf on 2016/7/22.
 */
public class MultiItemTextView extends TextView {
    private Context context;
    private List<ContactBean> contactList;
    private Drawable drawable;
    private int textColor;
    public List<ContactBean> getContactList() {
        return contactList;
    }

    private MomentsListAdapter.OnContactClickListener onContactClickListener;

    public void setOnContactClickListener(MomentsListAdapter.OnContactClickListener onContactClickListener) {
        this.onContactClickListener = onContactClickListener;
    }

    public void setContactList(ContactBean[] contacts) {
        contactList = Arrays.asList(contacts);
        setText("");
        if(contactList == null || contactList.size() == 0){
            return;
        }
        SpannableStringBuilder builder = new SpannableStringBuilder();
        addIcon(builder);
        for(int i = 0;i<contactList.size();i++){
            ContactBean contactBean = contactList.get(i);
            addSpan(builder,contactBean);
            if(i!=contactList.size()-1){
                builder.append(", ");
            }
        }
        append(builder);
    }

    private void addSpan(SpannableStringBuilder builder, ContactBean contactBean) {
        String name = contactBean.getName();
        final int id = contactBean.getId();
        builder.append(name);
        int start = builder.length()-name.length();
        int end = builder.length();
        ItemSpan span = new ItemSpan(id);
        builder.setSpan(span,start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    public void addFavor(ContactBean contactBean){
        SpannableStringBuilder builder = new SpannableStringBuilder();
        addSpan(builder,contactBean);
        append(", ");
        append(builder);
    }
    public void removeFavor(ContactBean contactBean){
        contactList.remove(contactBean);
        ContactBean[] contactBeans = new ContactBean[contactList.size()];
        int i =0;
        for(ContactBean bean:contactList){
            contactBeans[i++] = bean;
        }
        setContactList(contactBeans);
    }

    public MultiItemTextView(Context context) {
        super(context);
        init(context,null);
    }

    public MultiItemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MultiItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        textColor = ContextCompat.getColor(context,R.color.praise_item);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MultiItemTextView);
        drawable = ta.getDrawable(R.styleable.MultiItemTextView_viewIcon);
        ta.recycle();
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,
                context.getResources().getDisplayMetrics());
        drawable.setBounds(0,0,size,size);
//        注意  不加这句无法点击
        setMovementMethod(LinkMovementMethod.getInstance());
    }
    private void addIcon(SpannableStringBuilder builder){
        builder.append("a ");
        ImageSpan imageSpan = new ImageSpan(drawable);
        builder.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    class ItemSpan extends ClickableSpan{
        int id;
        public ItemSpan(int id) {
            this.id = id;
        }

        @Override
        public void onClick(View widget) {
            if(onContactClickListener!=null){
                onContactClickListener.onContactClick(id);
            }
            LogUtil.i("ItemSpan  Click!!");
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
}
