package eg.com.espace.epubview;

/**
 * Created by mohheader on 07/07/14.
 */
public interface BookListener {
    void pageChanged(int page);
    void bookFullyLoaded(int count);
}
