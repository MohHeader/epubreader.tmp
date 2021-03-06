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
package eg.com.espace.epubview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import eg.com.espace.epubview.tasks.SetupBook;

import java.io.IOException;
import java.io.InputStream;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

/**
 * Created by mohheader on 06/07/14.
 */
public class BookView extends RelativeLayout implements BookListener {
    private EpubView epubView;
    private TextView pageNumber,pageTotal;
    private BookListener publicListener;

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

    private EpubView getEpub(){
        return epubView;
    }

    @Override
    public void pageChanged(int page) {
        if(publicListener != null)
            publicListener.pageChanged(page);

        pageNumber.setText(String.valueOf(page));
    }

    @Override
    public void bookFullyLoaded(int count) {
        if(publicListener != null)
            publicListener.bookFullyLoaded(count);
        setTotalPage(count);
        getEpub().goToPage(getEpub().getCurrentPageNumber());
    }

    // Delegate Methods
    public void setDirection(DIRECTION dir){
        epubView.setDirection(dir);
    }
    public void goToNextPage(){
        getEpub().goToNextPage();
    }
    public void goToPrevPage(){
        getEpub().goToPrevPage();
    }
    public void goToPage(int n){
        getEpub().goToPage(n);
    };
    public int getSize(){
        return getEpub().getSize();
    }
    public int getCurrentPageNumber(){
        return getEpub().getCurrentPageNumber();
    }
    public void setTextIsSelectable(boolean selectable){
        getEpub().setTextIsSelectable(selectable);
    }
    public void setNightMode(boolean nightMode) {
        if(nightMode){
            setBackgroundColor(Color.BLACK);
        }else{
            setBackgroundColor(Color.WHITE);
        }
        getEpub().setNightMode(nightMode);
    }
    public  void onPause() {
        epubView.getBook().getEpubCreator().setInturrupted();
    }
}
