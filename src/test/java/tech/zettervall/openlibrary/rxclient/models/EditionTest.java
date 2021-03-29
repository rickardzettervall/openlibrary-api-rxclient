package tech.zettervall.openlibrary.rxclient.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Edition Object test.
 */
@RunWith(JUnit4.class)
public class EditionTest {

    /**
     * Test the natural sorting of Edition Objects.
     */
    @Test
    public void compareTo() {
        List<Edition> editions = new ArrayList<>();
        editions.add(Edition.newEditionForTesting("B", 1));
        editions.add(Edition.newEditionForTesting("A", 2));
        editions.add(Edition.newEditionForTesting("C", 1));
        editions.add(Edition.newEditionForTesting("A", 1));
        editions.add(Edition.newEditionForTesting("A", 3));
        Collections.sort(editions);

        // A1
        assertEquals("A", editions.get(0).getTitle());
        assertEquals(3, editions.get(0).getRevision());

        // A2
        assertEquals("A", editions.get(1).getTitle());
        assertEquals(2, editions.get(1).getRevision());

        // A3
        assertEquals("A", editions.get(2).getTitle());
        assertEquals(1, editions.get(2).getRevision());

        // B1
        assertEquals("B", editions.get(3).getTitle());
        assertEquals(1, editions.get(3).getRevision());

        // C1
        assertEquals("C", editions.get(4).getTitle());
        assertEquals(1, editions.get(4).getRevision());
    }
}
