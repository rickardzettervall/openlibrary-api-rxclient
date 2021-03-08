package tech.zettervall.openlibrary.rxclient.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * BookData Object test.
 */
@RunWith(JUnit4.class)
public class BookDataTest {

    /**
     * Test the natural sorting of Book Objects.
     */
    @Test
    public void naturalSorting() {
        List<BookData> bookList = new ArrayList<>();
        bookList.add(BookData.newBookDataForTesting("C"));
        bookList.add(BookData.newBookDataForTesting("A"));
        bookList.add(BookData.newBookDataForTesting("B"));
        Collections.sort(bookList);

        // A
        assertEquals("A", bookList.get(0).getTitle());

        // B
        assertEquals("B", bookList.get(1).getTitle());

        // C
        assertEquals("C", bookList.get(2).getTitle());
    }
}
