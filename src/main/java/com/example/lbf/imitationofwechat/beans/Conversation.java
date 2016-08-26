package com.example.lbf.imitationofwechat.beans;

import android.content.Context;
import android.view.View;

import java.io.Serializable;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMConversationType;

//对BmobIMConveration的抽象封装,
public abstract class Conversation implements Serializable,Comparable{

    /**
     * 会话id
     */
    protected String cId;
    /**
     * 会话类型
     */
    protected BmobIMConversationType cType;
    /**
     * 会话名称
     */
    protected String cName;

    protected BmobIMConversation bmobIMConversation;
    /**
     * 置顶聊天时的会话顺序
     */
    protected int order;

    /**
     * 获取头像-用于会话界面显示
     */
    abstract public String getAvatar();

    /**
     * 获取最后一条消息的时间
     */
    abstract public long getLastMessageTime();

    /**
     * 获取最后一条消息的时间
     * @return
     */
    abstract public String getLastMessageContent();

//    是否置顶
    abstract public boolean isOnTop();

    abstract public void setOnTop(boolean isOnTop);
    /**
     * 获取未读会话个数
     * @return
     */
    abstract public int getUnReadCount();

    /**
     * 将所有消息标记为已读
     */
    abstract public void readAllMessages();

    public String getcName() {
        return cName;
    }

    public String getcId(){
        return cId;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    public void increaseOrder() {
        order++;}
    public void decreaseOrder() {
        order--;
    }

    public BmobIMConversation getBmobIMConversation(){
        return bmobIMConversation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conversation that = (Conversation) o;
        if (!cId.equals(that.cId)) return false;
        return cType == that.cType;
    }

    @Override
    public int hashCode() {
        int result = cId.hashCode();
        result = 31 * result + cType.hashCode();
        return result;
    }

    public abstract void onClick(Context context, View v);

    public abstract void onLongClick(Context context,View v);

    @Override
    public int compareTo(Object another) {
        if (another instanceof Conversation){
            Conversation anotherConversation = (Conversation) another;
            if(isOnTop() && !anotherConversation.isOnTop()){
                return -1;
            } else if(!isOnTop() && anotherConversation.isOnTop()){
                return 1;
            } else if(isOnTop() && anotherConversation.isOnTop()){
                return order - anotherConversation.order;
            }
            long timeGap = anotherConversation.getLastMessageTime() - getLastMessageTime();
            if (timeGap > 0) return  1;
            else if (timeGap < 0) return -1;
            return 0;
        }else{
            throw new ClassCastException();
        }
    }

}
