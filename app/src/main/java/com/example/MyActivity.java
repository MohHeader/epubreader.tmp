package com.example;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;

import com.espace.epubviewer.BookListener;
import com.espace.epubviewer.BookView;
import com.espace.epubviewer.R;
import com.espace.epubviewer.DIRECTION;

import java.io.IOException;
import java.io.InputStream;


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
        b.setDirection(DIRECTION.RTL);

//Done,
//        b.getEpub().goToNextPage();
//        b.getEpub().goToPrevPage();


        //TODO :
        //b.goToPage(int);
        //b.getPagesCount();
        //b.getCurrentPageNumber();
        //Listener BookFullyLoaded();

    }

    @Override
    public void pageChanged(int page) {

    }
}
