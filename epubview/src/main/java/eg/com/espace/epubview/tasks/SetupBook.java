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
package eg.com.espace.epubview.tasks;

import android.os.AsyncTask;

import eg.com.espace.epubview.BookView;

/**
 * Created by mohheader on 06/07/14.
 */
public class SetupBook extends AsyncTask<Integer,Void,Integer> {
    private BookView bookView;
    public SetupBook(BookView bookView){
        this.bookView = bookView;
    }
    @Override
    protected Integer doInBackground(Integer... bundle) {
        return bookView.setup();
    }

    @Override
    protected void onPostExecute(Integer count) {
        super.onPostExecute(count);
        bookView.bookFullyLoaded(count);
    }
}