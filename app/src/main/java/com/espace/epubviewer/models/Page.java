package com.espace.epubviewer.models;

import java.io.Serializable;

/**
 * Created by mohheader on 14/05/14.
 */

public class Page implements Serializable {

    private int id;

    protected String book_id;

    protected int chapter;

    protected int charStart;

    protected int charEnd;

    private int orientation;

    protected int pageNumber;

    protected float font_size;

    public Page(){}
    public Page(int orientation,
                String book_id,
                int chapter,
                int pageNumber,
                int charStart,
                int charEnd,
                float font_size){
        this.orientation = orientation;
        this.book_id = book_id;
        this.chapter = chapter;
        this.pageNumber = pageNumber;
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.font_size = font_size;
    }
    public Page(int chapter, int charStart){
        this.chapter = chapter;
        this.charStart = charStart;
    }
    public int getChapter(){
        return chapter;
    }
    public int getCharStart(){
        return charStart;
    }
    public int getCharEnd(){
        return charEnd;
    }
    public int getPageNumber() {
        return pageNumber;
    }
    public int getId() {return id;}
    public int getOrientation() {
        return orientation;
    }

    public String getBookUID() {
        return book_id;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public void setCharStart(int charStart) {
        this.charStart = charStart;
    }

    public void setCharEnd(int charEnd) {
        this.charEnd = charEnd;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setBookId(String bookId) {
        this.book_id = bookId;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}