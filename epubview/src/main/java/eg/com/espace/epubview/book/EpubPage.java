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
package eg.com.espace.epubview.book;

import eg.com.espace.epubview.models.Page;

/**
 * Created by mohheader on 12/06/14.
 */
public class EpubPage {
    static EpubBook book;
    Page page;
    CharSequence text;

    EpubPage(Page page){
        this.page = page;
    }
    public CharSequence getText() {
        if (text == null){
            CharSequence c = book.getChapter(page.getChapter()).getText();
            // TODO : Double Check,!!
            text = c.subSequence(page.getCharStart(),Math.min(page.getCharEnd(),c.length() -1));
        }
        return text;
    }

    public int getChapterNumber(){
        return page.getChapter();
    }

    public int getCharStart(){
        return page.getCharStart();
    }

    public static EpubPage convertModelPage(EpubBook _book, Page mpage) {
        book = _book;
        return new EpubPage(mpage);
    }

    public static int getPageNumberAt(int _chapter, int aChar){
        Page page = book.getPageDB().getPageAt(book.getBookId(),
                book.getContext().getResources().getConfiguration().orientation,
                _chapter,aChar);
        if (page != null) {
            return page.getPageNumber();
        }else {
            return 0;
        }
    }
}
