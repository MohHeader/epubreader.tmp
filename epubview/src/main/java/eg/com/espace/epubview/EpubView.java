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
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;

import eg.com.espace.epubview.book.EpubBook;
import eg.com.espace.epubview.book.EpubPage;
import eg.com.espace.epubview.models.PageDBHandler;

/**
 * Created by mohheader on 11/05/14.
 */
public class EpubView extends TextSwitcher implements Text.TouchListener {
    BookListener listener;
    EpubBook book;
    Text textView;
    PageDBHandler pageDB;

    int currentPageNumber = 1;
    private DIRECTION direction = DIRECTION.LTR;

    public EpubView(final Context context) {
        this(context, null);
    }

    public EpubView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                textView = new Text(context,EpubView.this);
                return textView;
            }
        });
        book = new EpubBook(getContext(), this);
        pageDB = new PageDBHandler(getContext());
    }

    public int setup(){
        return book.setup();
    }

    @Override
    public void onSwipeRight(){
        setAnimation(DIRECTION.LTR);
        if(direction == DIRECTION.LTR){
            goToPrevPage();
        }
        else {
            goToNextPage();
        }
    }

    @Override
    public void onSwipeLeft(){
        setAnimation(DIRECTION.RTL);
        if(direction == DIRECTION.LTR) {
            goToNextPage();
        } else {
            goToPrevPage();
        }
    }

    public void setBook(nl.siegmann.epublib.domain.Book _book, String book_id){
        book.setBook(_book,book_id);
    }

    public void goToPage(int number){
        currentPageNumber = number;
        if(number > getBook().getSize())
            return;
        renderPage(book.getPage(number).getText());

        if(listener != null)
            listener.pageChanged(currentPageNumber);
    }

    public void goToNextPage(){
        if (currentPageNumber < getBook().getSize() )
            goToPage(currentPageNumber + 1);
    }
    public void goToPrevPage(){
        if (currentPageNumber > 1)
            goToPage(currentPageNumber - 1);
    }

    private void setAnimation(DIRECTION dir) {
        Animation in,out;
        if(dir == DIRECTION.LTR){
            in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
            out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        }else{
            in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
            out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        }
        setInAnimation(in);
        setOutAnimation(out);
    }

    private void renderPage(CharSequence text){
        setText(text);
    }

    public EpubBook getBook(){
        return book;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setBookListener(BookListener listener) {
        this.listener = listener;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public TextPaint getTextPaint(){
        return textView.getPaint();
    }

    public int getSize() {
        return book.getSize();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("currentChapter", getBook().getPage(getCurrentPageNumber()).getChapterNumber());
        bundle.putInt("currentChar", getBook().getPage(getCurrentPageNumber()).getCharStart());
        return bundle;
    }
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            int currentChapter = bundle.getInt("currentChapter");
            int currentChar = bundle.getInt("currentChar");
            currentPageNumber = EpubPage.getPageNumberAt(currentChapter, currentChar);
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    public void setTextIsSelectable(boolean selectable){
        textView.setTextIsSelectable(selectable);
    }

    public void setNightMode(boolean nightMode) {
        for (int i = 0;i < getChildCount();i++){
            TextView text = (TextView) getChildAt(i);
            if(nightMode){
                text.setTextColor(Color.WHITE);
            }else{
                text.setTextColor(Color.BLACK);
            }
        }
    }
}
