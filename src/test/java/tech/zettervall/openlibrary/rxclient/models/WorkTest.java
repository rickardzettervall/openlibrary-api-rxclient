package tech.zettervall.openlibrary.rxclient.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Work Object test.
 */
@RunWith(JUnit4.class)
public class WorkTest {

    /**
     * Test the natural sorting of Work Objects.
     */
    @Test
    public void compareTo() {
        List<Work> works = new ArrayList<>();
        works.add(Work.newWorkForTesting("B", 1));
        works.add(Work.newWorkForTesting("A", 2));
        works.add(Work.newWorkForTesting("C", 1));
        works.add(Work.newWorkForTesting("A", 1));
        works.add(Work.newWorkForTesting("A", 3));
        Collections.sort(works);

        // A3
        assertEquals("A", works.get(0).getTitle());
        assertEquals(3, works.get(0).getRevision());

        // A2
        assertEquals("A", works.get(1).getTitle());
        assertEquals(2, works.get(1).getRevision());

        // A1
        assertEquals("A", works.get(2).getTitle());
        assertEquals(1, works.get(2).getRevision());

        // B1
        assertEquals("B", works.get(3).getTitle());
        assertEquals(1, works.get(3).getRevision());

        // C1
        assertEquals("C", works.get(4).getTitle());
        assertEquals(1, works.get(4).getRevision());
    }
}
