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
    public void compareTo() {
        List<BookData> books = new ArrayList<>();
        books.add(BookData.newBookDataForTesting("C"));
        books.add(BookData.newBookDataForTesting("A"));
        books.add(BookData.newBookDataForTesting("B"));
        Collections.sort(books);

        // A
        assertEquals("A", books.get(0).getTitle());

        // B
        assertEquals("B", books.get(1).getTitle());

        // C
        assertEquals("C", books.get(2).getTitle());
    }
}
