package eg.com.espace.epubview.book.creator;

import android.graphics.Canvas;
import android.text.Layout;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.RelativeLayout;

import eg.com.espace.epubview.book.EpubBook;
import eg.com.espace.epubview.book.EpubPage;
import eg.com.espace.epubview.models.Page;
import eg.com.espace.epubview.EpubView;
import eg.com.espace.epubview.models.PageDBHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohheader on 10/06/14.
 */
public class EpubCreator {

    public boolean end = false;
    private float relativeSize;
    private EpubView epubView;

    public EpubCreator(EpubView _epubView){
        epubView = _epubView;
        textPaint = epubView.getTextPaint();
        boundedWidth = epubView.getWidth();
    }

    private int storedStartPosition = 0;
    private int storedEndPosition = 0;
    private int chapterNumber = 0;
    private int lastChapterNumber = 0;
    private int totalPages = 0;
    private int MAX_CHARACTERS;

    private CharSequence chapterText;

    private volatile boolean isInturrupted = false;

    public List<EpubPage> countPages(float size) {
        return countPages(size, true);
    }
    public List<EpubPage> countPages(float size, boolean render) {
        resetVariables(size);
        Log.i("CREATOR", "Width Size : " + boundedWidth);
        Log.i("CREATOR", "Height Size : " + epubView.getHeight());
        List<Page> mpages = loadPagesFromDB();
        List<EpubPage> pages = new ArrayList<EpubPage>();

        if (isNotComplete(mpages)){
            int initChapter = 0;
            if(mpages.size() > 0){
                initChapter = mpages.get(mpages.size() - 1).getChapter();
                pages = startFromPreviousState(mpages);
            }
            chapterNumber = initChapter;
            chapterText = epubView.getBook().getChapter(initChapter).getText();
            pages = renderPages(pages, render, size);
        }else if(render){
            for(Page mpage : mpages){
                pages.add(EpubPage.convertModelPage(epubView.getBook(), mpage));
            }
        }
        return pages;
    }

    private List<EpubPage> startFromPreviousState(List<Page> mpages) {
        List<EpubPage> pages = new ArrayList<EpubPage>();
        storedStartPosition = mpages.get(mpages.size() - 1).getCharEnd() + 1;
        totalPages = mpages.get(mpages.size() - 1).getPageNumber();
        for(Page mpage : mpages){
            pages.add(EpubPage.convertModelPage(epubView.getBook(), mpage));
        }
        return pages;
    }

    private List<Page> loadPagesFromDB() {
        EpubBook book = epubView.getBook();
        PageDBHandler pageDB = book.getPageDB();
        return pageDB.list(book.getBookId(),
                book.getContext().getResources().getConfiguration().orientation);
    }

    private boolean isNotComplete(List<Page> mpages){
        return ( mpages.size() == 0 ||
                mpages.get(mpages.size() - 1).getChapter() != epubView.getBook().getContentsSize() - 1 );
    }

    private List<EpubPage> renderPages(List<EpubPage> pages, boolean render, float size) {
        int orientation = epubView.getBook().getContext().getResources().getConfiguration().orientation;
        while(pageNext() && !isInturrupted){
            if(chapterNumber > lastChapterNumber && render){
                lastChapterNumber = chapterNumber;
                epubView.getBook().setIntervalPages(pages);
            }
            Page newPage = new Page(orientation,epubView.getBook().getBookId(),chapterNumber,++totalPages,storedStartPosition,storedEndPosition, size);
            //TODO: Refactor 4 Performance : Bulk Insert
            epubView.getBook().getPageDB().add(newPage);
            pages.add(EpubPage.convertModelPage(epubView.getBook(), newPage));
        }
        return pages;
    }

    private void resetVariables(float size) {
        storedStartPosition = 0;
        storedEndPosition = 0;
        chapterNumber = 0;
        totalPages = 0;
        MAX_CHARACTERS =  2048;//epubView.getBook().getContext().getResources().getInteger(R.integer.characters_per_page);
        relativeSize = size;
        isInturrupted = false;
        lastChapterNumber = 0;
    }

    private Boolean pageNext() {
        int totalLength = chapterText.length();

        if ( storedEndPosition >= chapterText.length() -1 ) {
            if(chapterNumber >= epubView.getBook().getContentsSize() -1){
                return false;
            }
            chapterText = epubView.getBook().getChapter(++chapterNumber).getText();
            storedStartPosition = 0;
        }else{
            this.storedStartPosition = Math.min(storedEndPosition, totalLength -1 );
        }

        this.storedStartPosition = Math.max(0, this.storedStartPosition);
        this.storedStartPosition = Math.min(chapterText.length() -1, this.storedStartPosition);

        storedEndPosition = Math.min(chapterText.length() -1, this.storedStartPosition + MAX_CHARACTERS);

        CharSequence cutOff = chapterText.subSequence(storedStartPosition, storedEndPosition );
        if ( chapterText.length() != 0 ) {
            updatePage(cutOff);
        }
        return true;
    }
    static TextPaint textPaint;
    static int boundedWidth;

    private void updatePage(CharSequence cutOff){
        Spannable text = (Spannable) cutOff;

        text.setSpan(new RelativeSizeSpan(relativeSize), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        StaticLayout layout;
        // TODO : Temp WorkAround for Bug in Android 4.1
        // Check : https://code.google.com/p/android/issues/detail?id=35466 - https://code.google.com/p/android/issues/detail?id=35412
        // https://gist.github.com/pyricau/3424004 ( Another Way To Solve it )
        try{
            layout = new StaticLayout(text, textPaint, boundedWidth , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }catch(ArrayIndexOutOfBoundsException e){
            text.setSpan(new RelativeSizeSpan(1.1f), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            layout = new StaticLayout(text, textPaint, boundedWidth , Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        }

        layout.draw(new Canvas());

        int bottomLine = layout.getLineForVertical( ((RelativeLayout)epubView.getParent()).getHeight() - ( 2 * 15) );
        bottomLine = Math.max( 1, bottomLine );

        if ( layout.getHeight() >= ((RelativeLayout)epubView.getParent()).getHeight() && chapterText.length() > 10) {
            int offset = layout.getLineStart(bottomLine - 1);
            offset = Math.max(1,offset);
            storedEndPosition = storedStartPosition + offset;
        }
    }

    public void setInturrupted(){
        isInturrupted = true;
    }
}
