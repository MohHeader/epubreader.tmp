package com.example;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;


import com.espace.epubviewer.R;

import java.io.IOException;
import java.io.InputStream;

import eg.com.espace.epubview.BookListener;
import eg.com.espace.epubview.BookView;
import eg.com.espace.epubview.DIRECTION;

public class MyActivity extends Activity implements BookListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epub_activity);

        AssetManager assetManager = getAssets();
        BookView b = (BookView) findViewById(R.id.book_view);
        try {
            InputStream is = assetManager.open("sample.epub");
            b.setBook(is,"2");
        } catch (IOException e) {
            e.printStackTrace();
        }

        b.setBookListener(this);
        b.setDirection(DIRECTION.LTR);

//Done,
//        b.goToNextPage();
//        b.goToPrevPage();
//        b.goToPage(3);
//        b.getSize();
//        b.getCurrentPageNumber();
        b.setTextIsSelectable(true);
    }

    @Override
    public void pageChanged(int page) {

    }

    @Override
    public void bookFullyLoaded(int count) {

    }
}
