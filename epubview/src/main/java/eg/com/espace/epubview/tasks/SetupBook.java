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
        bookView.setTotalPage(count);
        bookView.getEpub().goToPage(1);
    }
}