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
    public void naturalSorting() {
        List<Work> workList = new ArrayList<>();
        workList.add(Work.newWorkForTesting("B", 1));
        workList.add(Work.newWorkForTesting("A", 2));
        workList.add(Work.newWorkForTesting("C", 1));
        workList.add(Work.newWorkForTesting("A", 1));
        workList.add(Work.newWorkForTesting("A", 3));
        Collections.sort(workList);

        // A3
        assertEquals("A", workList.get(0).getTitle());
        assertEquals(3, workList.get(0).getRevision());

        // A2
        assertEquals("A", workList.get(1).getTitle());
        assertEquals(2, workList.get(1).getRevision());

        // A1
        assertEquals("A", workList.get(2).getTitle());
        assertEquals(1, workList.get(2).getRevision());

        // B1
        assertEquals("B", workList.get(3).getTitle());
        assertEquals(1, workList.get(3).getRevision());

        // C1
        assertEquals("C", workList.get(4).getTitle());
        assertEquals(1, workList.get(4).getRevision());
    }
}
