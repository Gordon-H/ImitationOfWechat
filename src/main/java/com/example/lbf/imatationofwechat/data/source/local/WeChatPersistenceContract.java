/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lbf.imatationofwechat.data.source.local;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class WeChatPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public WeChatPersistenceContract() {}

    public static abstract class ChatsEntry implements BaseColumns {
        public static final String TABLE_CHATS = "Chats";
        public static final String COL_CHATS_FOREIGN_KEY = "key";
        public static final String COL_CHATS_TYPE = "type";
        public static final String COL_CHATS_MSG = "msg";
        public static final String COL_CHATS_TIME = "time";
    }
    public static abstract class ContactsEntry implements BaseColumns {
        public static final int CONTACT_TYPE_NORMAL = 1;
        public static final int CONTACT_TYPE_STARRED = 2;
        public static final String TABLE_CONTACTS = "Contacts";
        public static final String COL_CONTACTS_ID = "_id";
        public static final String COL_CONTACTS_ACCOUNT = "account";
        public static final String COL_CONTACTS_NAME = "name";
        public static final String COL_CONTACTS_REMARKS = "remarks";
        public static final String COL_CONTACTS_IMAGE = "image";
        public static final String COL_CONTACTS_TYPE = "type";
        public static final String COL_CONTACTS_TAG = "tag";
        public static final String COL_CONTACTS_KEY = "key";
    }
    public static abstract class MsgsEntry implements BaseColumns {
        //    消息类型：文本和图片
        public static final int MSG_TYPE_TEXT = 0;
        public static final int MSG_TYPE_IMAGE = 1;
        public static final String TABLE_MSG = "Msg";
        public static final String COL_MSG_ID = "_id";
        public static final String COL_MSG_SENDER = "sender";
        public static final String COL_MSG_RECEIVER = "receiver";
        public static final String COL_MSG_CONTENT = "content";
        public static final String COL_MSG_TYPE = "type";
        public static final String COL_MSG_TIME = "time";
        public static final String COL_MSG_DISPLAY_TIME = "display_time";
    }
    public static abstract class FollowEntry implements BaseColumns {
        public static final String TABLE_FOLLOW = "Follow";
        public static final String COL_FOLLOW_ID = "_id";
        public static final String COL_FOLLOW_ACCOUNT = "account";
        public static final String COL_FOLLOW_NAME = "name";
        public static final String COL_FOLLOW_IMAGE = "image";
        public static final String COL_FOLLOW_KEY = "key";
    }
    public static abstract class UserEntry implements BaseColumns {
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final String TABLE_USER = "User";
        public static final String COL_USER_ID = "_id";
        public static final String COL_USER_ACCOUNT = "account";
        public static final String COL_USER_NAME = "name";
        public static final String COL_USER_IMAGE = "image";
        public static final String COL_USER_QR_CODE = "qr_code";
        public static final String COL_USER_GENDER = "gender";
        public static final String COL_USER_REGION = "region";
        public static final String COL_USER_SIGNATURE = "signature";
    }
    public static abstract class MomentsEntry implements BaseColumns {
        public static final String TABLE_MOMENTS = "Moments";
        public static final String COL_MOMENTS_ID = "_id";
        public static final String COL_MOMENTS_CONTACT_ID = "contact_id";
        public static final String COL_MOMENTS_CONTENT = "content";
        public static final String COL_MOMENTS_IMAGES = "images";
        public static final String COL_MOMENTS_LINK = "link";
        public static final String COL_MOMENTS_TIME = "time";

    }
    public static abstract class CommentsEntry implements BaseColumns {
        public static final String TABLE_COMMENTS = "Comments";
        public static final String COL_COMMENTS_ID = "_id";
        public static final String COL_COMMENTS_CONTACT_ID = "contact_id";
        public static final String COL_COMMENTS_MOMENT_ID = "moment_id";
        public static final String COL_COMMENTS_CONTENT = "content";
        public static final String COL_COMMENTS_TO = "to_id";

    }
    public static abstract class FavorsEntry implements BaseColumns {
        public static final String TABLE_FAVORS = "Favors";
        public static final String COL_FAVORS_ID = "_id";
        public static final String COL_FAVORS_CONTACT_ID = "contact_id";
        public static final String COL_FAVORS_MOMENT_ID = "moment_id";
    }

}
