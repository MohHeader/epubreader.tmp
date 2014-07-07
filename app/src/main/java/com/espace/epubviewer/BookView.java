package com.espace.epubviewer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.espace.epubviewer.tasks.SetupBook;

import java.io.IOException;
import java.io.InputStream;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by mohheader on 06/07/14.
 */
public class BookView extends RelativeLayout implements BookListener {
    EpubView epubView;
    TextView pageNumber,pageTotal;
    BookListener publicListener;

    public BookView(Context context) {
        this(context, null);
    }

    public BookView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.compound_book_view, this);
        init();
    }

    private void init() {
        epubView = (EpubView) findViewById(R.id.epub_view);
        pageNumber = (TextView)findViewById(R.id.page_number);
        pageTotal = (TextView)findViewById(R.id.page_total);
    }

    public void setBook(InputStream is, final String uid) throws IOException{
        final Book book = (new EpubReader()).readEpub(is);
        this.post(new Runnable() {
            @Override
            public void run() {
                epubView.setBook(book, uid);
                epubView.setBookListener(BookView.this);
                new SetupBook(BookView.this).execute();
            }
        });
    }

    public void setTotalPage(int total){
        pageTotal.setText(String.valueOf(total));
    }

    public int setup() {
        return epubView.setup();
    }

    public void setBookListener(BookListener listener) {
        publicListener = listener;
    }

    public void setDirection(DIRECTION dir){
        epubView.setDirection(dir);
    }

    public EpubView getEpub(){
        return epubView;
    }

    @Override
    public void pageChanged(int page) {
        if(publicListener != null)
            publicListener.pageChanged(page);

        pageNumber.setText(String.valueOf(page));
    }
}
