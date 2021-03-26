package tech.zettervall.openlibrary.rxclient.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class RepositoryTest {

    private final Repository repository = new Repository();

    /**
     * Test cover URL assembler.
     */
    @Test
    public void coverUrl() {
        String isbnS = Repository.getCoverUrl(Repository.CoverKey.ISBN, "x", Repository.CoverSize.S, true);
        assertEquals("http://covers.openlibrary.org/b/isbn/x-S.jpg", isbnS);

        String idM = Repository.getCoverUrl(Repository.CoverKey.ID, "x", Repository.CoverSize.M, true);
        assertEquals("http://covers.openlibrary.org/b/id/x-M.jpg", idM);

        String lccnL = Repository.getCoverUrl(Repository.CoverKey.LCCN, "x", Repository.CoverSize.L, true);
        assertEquals("http://covers.openlibrary.org/b/lccn/x-L.jpg", lccnL);

        String oclcL = Repository.getCoverUrl(Repository.CoverKey.OCLC, "x", Repository.CoverSize.L, true);
        assertEquals("http://covers.openlibrary.org/b/oclc/x-L.jpg", oclcL);

        String olidL = Repository.getCoverUrl(Repository.CoverKey.OLID, "x", Repository.CoverSize.L, false);
        assertEquals("http://covers.openlibrary.org/b/olid/x-L.jpg?default=false", olidL);
    }

    /**
     * Test published range String creator.
     */
    @Test
    public void publishedRange() {
        assertEquals("1990-1992", repository.getPublishedRangeString(1990, 1992));
        assertEquals("1990", repository.getPublishedRangeString(1990, null));
        assertEquals("1992", repository.getPublishedRangeString(null, 1992));
        assertNull(repository.getPublishedRangeString(null, null));
        assertThrows(IllegalArgumentException.class, () -> repository.getPublishedRangeString(1, 2, 3));
    }
}
