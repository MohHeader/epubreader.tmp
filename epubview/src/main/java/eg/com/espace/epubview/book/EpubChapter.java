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
