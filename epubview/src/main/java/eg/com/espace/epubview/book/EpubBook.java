package eg.com.espace.epubview.book;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import eg.com.espace.epubview.book.creator.EpubCreator;
import eg.com.espace.epubview.models.Page;
import eg.com.espace.epubview.book.creator.EHTMLSpanner;

import eg.com.espace.epubview.EpubView;
import eg.com.espace.epubview.models.PageDBHandler;

import net.nightwhistler.htmlspanner.HtmlSpanner;


import java.util.List;

import nl.siegmann.epublib.domain.Book;

/**
 * Created by mohheader on 12/06/14.
 */
public class EpubBook {
    final private Object lock = new Object();

    private EpubView epubView;
    private Context context;
    private SparseArray<EpubChapter> chapters;
    private List<EpubPage>  pages;
    private String book_id = "-1";

    private HtmlSpanner span;
    private float font_size = 1f;

    private EpubCreator epubCreator;
    private int resources_size = -1;
    private PageDBHandler pageDB;

    public EpubBook(Context _context, EpubView _epubView){
        epubView = _epubView;
        context = _context;
        pageDB = new PageDBHandler(getContext());
    }
    Book book;
    public void setBook(Book _book, String book_id) {
        this.book_id = book_id;
        book = _book;
        span = new EHTMLSpanner(book,epubView);
        resources_size = book.getContents().size();
        chapters = new SparseArray<EpubChapter>(resources_size);
        epubCreator = new EpubCreator(epubView);
    }

    public int setup() {
        font_size = 1f;
        pages = null;
        pages = epubCreator.countPages(font_size);
        return pages.size();
    }

    public EpubChapter getChapter(int number){
        Log.d("DEBUG","getChapter : "+number);
        if(chapters.get(number) == null){
            chapters.put(number, new EpubChapter(this, book.getContents().get(number)));
        }
        return chapters.get(number);
    }
    public EpubPage getPage(int number) {
        if (pages == null)
            setup();
        return pages.get(number - 1);
    }

    public HtmlSpanner getSpan() {
        return span;
    }

    public Context getContext() {
        return context;
    }

    public Page getCurrentPageModel() {
        if (pages == null)
            return null;
        return pages.get(Math.min(epubView.getCurrentPageNumber(),pages.size())-1).getPageModel();
    }

    public int getSize() {
        if (pages != null)
            return pages.size();
        else
            return 0;
    }

    public SparseArray<EpubChapter> getChapters() {
        synchronized (lock) {
            while (chapters == null) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notify();
            return chapters;
        }
    }

    public String getBookId() {
        return book_id;
    }

    public void setIntervalPages(final List<EpubPage> intervalPages) {
        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pages == null) {
                    pages = intervalPages;
                    epubView.goToPage(1);
                } else if(epubView.getCurrentPageNumber() + 10 > pages.size()  ){
                    pages = intervalPages;
                }
            }
        });
    }

    public int getContentsSize(){
        synchronized (lock) {
            while (resources_size == -1) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notify();
        }
        return resources_size;
    }

    public PageDBHandler getPageDB() {
        return pageDB;
    }
    public EpubCreator getEpubCreator(){
        return epubCreator;
    }
}
