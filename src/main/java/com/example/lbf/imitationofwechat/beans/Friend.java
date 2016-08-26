package com.example.lbf.imitationofwechat.beans;

import cn.bmob.v3.BmobObject;

/**好友表
 * @author smile
 * @project Friend
 * @date 2016-04-26
 */
public class Friend extends BmobObject{

    private User user;
    private User friendUser;

//    排序的键，为标记字符+备注。标记字符为1，2或3,1表示星标朋友，2表示备注首字符为中文或字母，3表示备注首字符为其他字符。
    private String sortKey;

//    备注默认为账户的昵称
    private String remarks;
//    标签，默认为空
    private String tag;

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(User friendUser) {
        this.friendUser = friendUser;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
