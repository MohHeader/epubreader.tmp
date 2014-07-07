package com.espace.epubviewer.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mohheader on 06/07/14.
 */
public class DBBaseAdapter {
    private static final String TAG = "DBBaseAdapter";

    protected static final String DATABASE_NAME = "db.sqlite.epub";

    private static final int VER_LAUNCH = 1;
    private static final int DATABASE_VERSION = VER_LAUNCH;

    protected Context mContext;
    protected static DatabaseHelper mDbHelper;

    protected static final String TABLE_PAGES = "pages";

    // Contacts Table Columns names
    protected static final String KEY_ID = "id";

    protected static final String KEY_BOOK_UID      = "uid";
    protected static final String KEY_CHAPTER       = "chapter";
    protected static final String KEY_CHAR_START    = "char_start";
    protected static final String KEY_CHAR_END      = "char_end";
    protected static final String KEY_ORIENTATION   = "orientation";
    protected static final String KEY_PAGE_NUMBER   = "page_number";

    private static final String TABLE_CREATE_PAGES = "CREATE TABLE "
            + TABLE_PAGES + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_BOOK_UID + " TEXT," +
            KEY_CHAPTER + " INTEGER," +
            KEY_CHAR_START + " INTEGER," +
            KEY_CHAR_END + " INTEGER," +
            KEY_ORIENTATION + " INTEGER," +
            KEY_PAGE_NUMBER + " INTEGER" +
            ")";



    public DBBaseAdapter(Context context) {
        mContext = context.getApplicationContext();
    }

    public SQLiteDatabase openDb() {
        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(mContext);
        }
        return mDbHelper.getWritableDatabase();
    }

    public void closeDb() {
        mDbHelper.close();
    }

    protected static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE_PAGES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d(TAG, "onUpgrade() from " + oldVersion + " to " + newVersion);

            // NOTE: This switch statement is designed to handle cascading database
            // updates, starting at the current version and falling through to all
            // future upgrade cases. Only use "break;" when you want to drop and
            // recreate the entire database.

            if (oldVersion != DATABASE_VERSION) {
                db.execSQL("DROP TABLE IF EXISTS "+TABLE_PAGES);
                onCreate(db);
            }
        }
    }
}