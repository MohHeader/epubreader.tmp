/*
 * Copyright (C) 2014 eSpace Technologies <http://www.espace.com.eg>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eg.com.espace.epubview.models;

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