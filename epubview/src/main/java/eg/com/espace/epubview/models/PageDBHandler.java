package eg.com.espace.epubview.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohheader on 06/07/14.
 */
public class PageDBHandler  extends DBBaseAdapter {
    public PageDBHandler(Context context){
        super(context);
    }
    private void insertQuery(SQLiteDatabase db, Page page){
        ContentValues values = new ContentValues();

        values.put(KEY_BOOK_UID, page.getBookUID());
        values.put(KEY_CHAPTER,page.getChapter());
        values.put(KEY_CHAR_START,page.getCharStart());
        values.put(KEY_CHAR_END,page.getCharEnd());
        values.put(KEY_ORIENTATION,page.getOrientation());
        values.put(KEY_PAGE_NUMBER,page.getPageNumber());

        db.insert(TABLE_PAGES, null, values);
    }
    public void add(Page page) {
        SQLiteDatabase db = openDb();
        insertQuery(db, page);
        closeDb();
    }

    public void add(List<Page> pages) {
        SQLiteDatabase db = openDb();
        db.execSQL("BEGIN IMMEDIATE TRANSACTION");
        for(Page page:pages){
            insertQuery(db, page);
        }
        db.execSQL("COMMIT TRANSACTION");
        closeDb();
    }

    public List<Page> list(String book_uid, int orientation){
        List<Page> pages = new ArrayList<Page>();
        String selectQuery = "SELECT "
                +KEY_CHAPTER+ ","
                +KEY_CHAR_START+ ","
                +KEY_CHAR_END+ ","
                +KEY_PAGE_NUMBER
                +" FROM " + TABLE_PAGES + " WHERE "
                + KEY_BOOK_UID + " = \"" + book_uid +"\" "
                +" AND "+ KEY_ORIENTATION + " = \"" + orientation +"\" ";
        SQLiteDatabase db = openDb();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Page page = new Page();
                page.setChapter(cursor.getInt(0));
                page.setCharStart(cursor.getInt(1));
                page.setCharEnd(cursor.getInt(2));
                page.setPageNumber(cursor.getInt(3));
                page.setBookId(book_uid);
                page.setOrientation(orientation);

                pages.add(page);
            } while (cursor.moveToNext());
        }
        closeDb();
        return pages;
    }
}