package eg.com.espace.epubview.book;

import java.io.IOException;

import nl.siegmann.epublib.domain.Resource;

/**
 * Created by mohheader on 12/06/14.
 */
public class EpubChapter {
    EpubBook book;
    CharSequence text;
    String title;

    public EpubChapter(EpubBook book, Resource r) {
        this.book = book;
        try {
            byte[] data = r.getData();
            text = book.getSpan().fromHtml(new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CharSequence getText() {
        return text;
    }
}
