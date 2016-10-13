package com.example.sky.contentproviderdemo.com.example.sky.contentproviderdemo.provider;

import android.net.Uri;
import android.provider.BaseColumns;
public final class PersonContract {

    public static final String AUTHORITY = "com.example.sky.contentproviderdemo.person_provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static class Person implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/person");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/com.example.sky.contentproviderdemo.person_provider.person";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.example.sky.contentproviderdemo.person_provider.person";
        public static final String _ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String AGE = "age";
        public static final String DEFAULT_SORT_ORDER = AGE + " DESC";
    }

}
