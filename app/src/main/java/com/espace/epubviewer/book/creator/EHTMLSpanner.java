package com.espace.epubviewer.book.creator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;

import com.espace.epubviewer.EpubView;
import com.osbcp.cssparser.CSSParser;
import com.osbcp.cssparser.Rule;

import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.htmlspanner.SpanStack;
import net.nightwhistler.htmlspanner.css.CSSCompiler;
import net.nightwhistler.htmlspanner.handlers.ImageHandler;
import net.nightwhistler.htmlspanner.handlers.StyleNodeHandler;

import org.htmlcleaner.TagNode;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;

/**
 * Created by mohheader on 12/05/14.
 */
public class EHTMLSpanner extends HtmlSpanner {
    Book book;
    EpubView epubView;
    public EHTMLSpanner(Book book, EpubView epubView){
        super();
        this.book = book;
        this.epubView = epubView;
        registerHandler("img", new EpubImageHandler());
        registerHandler("link", new StyleHandler());
    }



    public class EpubImageHandler extends ImageHandler {
        @Override
        protected Bitmap loadBitmap(String url) {
            if(url.startsWith("../"))
                url = url.substring(3,url.length());
            url = url.replace("%20"," ");
            Bitmap bitmap = null;

            Resource resource;
            try {
                resource = book.getResources().getByHref(url);
                byte[] data = resource.getData();
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            } catch (Exception e) { }
                return scaleBitmap(bitmap);
        }
    }

    public class StyleHandler extends StyleNodeHandler {
        @Override
        public void handleTagNode(TagNode node, SpannableStringBuilder builder, int start, int end, SpanStack spanStack) {
            String url = node.getAttributeByName("href");
            if(url.startsWith("../"))
                url = url.substring(3,url.length());

            try {
                Resource resource = book.getResources().getByHref(url);
                byte[] data = resource.getData();
                if (getSpanner().isAllowStyling()) {
                    parseCSSFromText(new String(data), spanStack);
                }
            } catch (Exception e) { }
        }

        private void parseCSSFromText( String text, SpanStack spanStack ) {
            try {
                for ( Rule rule: CSSParser.parse(text) ) {
                    spanStack.registerCompiledRule(CSSCompiler.compile(rule, getSpanner()));
                }
            } catch ( Exception e ) { }
        }
    }

    private Bitmap scaleBitmap(Bitmap bm) {
        if (bm == null)
            return bm;
        float width = bm.getWidth();
        float height = bm.getHeight();
        float maxWidth = epubView.getWidth();
        float maxHeight = epubView.getHeight();

        if(width == 0 || height == 0 || maxWidth == 0 || maxHeight == 0 )
            return bm;
        if(width < maxWidth && height < maxHeight)
            return bm;

        float ratioW = width/maxWidth;
        float ratioH = height/maxHeight;

        float ratio = Math.max(ratioH,ratioW);

        if (ratio != 0) {
            height = height / ratio;
            width = width / ratio;
        }
        return Bitmap.createScaledBitmap(bm, (int)(width), (int)(height), true);
    }
}
