package com.example.sky.contentproviderdemo.com.example.sky.contentproviderdemo.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;


public class PersonProvider extends ContentProvider {
    private static final String AUTHORITY = PersonContract.AUTHORITY;
    private static final String ID = PersonContract.Person._ID;
    private static final String NAME = PersonContract.Person.NAME;
    private static final String AGE = PersonContract.Person.AGE;
    private static final String DEFAULT_SORT_ORDER = PersonContract.Person.DEFAULT_SORT_ORDER;
    private static final String CONTENT_TYPE = PersonContract.Person.CONTENT_TYPE;
    private static final String CONTENT_ITEM_TYPE = PersonContract.Person.CONTENT_ITEM_TYPE;
    private static final String DATABASE_NAME = "person.db";
    private static final int DATABASE_VERSION = 1;
    private static final String PERSON_TABLE_NAME = "person";
    private static final int PERSON = 1;
    private static final int PERSON_ID = 2;
    private DatabaseHelper mDatabaseHelper;
    private static UriMatcher sUriMatcher;
    private static HashMap<String, String> sPersonProjectionMap;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "person", PERSON);
        sUriMatcher.addURI(AUTHORITY, "person/#", PERSON_ID);

        sPersonProjectionMap = new HashMap<String, String>();
        sPersonProjectionMap.put(ID, ID);
        sPersonProjectionMap.put(NAME, NAME);
        sPersonProjectionMap.put(AGE, AGE);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            this(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + PERSON_TABLE_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + NAME + " TEXT,"
                    + AGE + " INTEGER"
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        }
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return mDatabaseHelper == null ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case PERSON:
                qb.setTables(PERSON_TABLE_NAME);
                qb.setProjectionMap(sPersonProjectionMap);
                break;
            case PERSON_ID:
                qb.setTables(PERSON_TABLE_NAME);
                qb.setProjectionMap(sPersonProjectionMap);
                qb.appendWhere("_id" + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        String sortBy;
        if (TextUtils.isEmpty(sortOrder)) {
            sortBy = DEFAULT_SORT_ORDER;
        } else {
            sortBy = sortOrder;
        }
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case PERSON:
                return CONTENT_TYPE;
            case PERSON_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMatcher.match(uri) != PERSON) {
            throw new IllegalArgumentException("UNKNOWN URI " + uri);
        }
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }
        if (contentValues.containsKey(NAME) == false) {
            contentValues.put(NAME, (String) null);
        }
        if (contentValues.containsKey(AGE) == false) {
            contentValues.put(AGE, 0);
        }

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        long rowId = db.insert(PERSON_TABLE_NAME, null, contentValues);
        if (rowId > 0) {
            Uri pUri = ContentUris.withAppendedId(PersonContract.Person.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(pUri, null);
            return pUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowId = 0;
        switch (sUriMatcher.match(uri)) {
            case PERSON:
                rowId = db.delete(PERSON_TABLE_NAME, selection, selectionArgs);
                break;
            case PERSON_ID:
                String pId = uri.getPathSegments().get(1);
                rowId = db.delete(PERSON_TABLE_NAME, ID + "=" + pId
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return rowId;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        int rowId = 0;
        switch (sUriMatcher.match(uri)) {
            case PERSON:
                db.update(PERSON_TABLE_NAME, values, selection, selectionArgs);
                break;
            case PERSON_ID:
                String pId = uri.getPathSegments().get(1);
                rowId = db.update(PERSON_TABLE_NAME, values, ID + "=" + pId
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return rowId;
    }
}
