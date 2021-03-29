package tech.zettervall.openlibrary.rxclient.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Subject Object test.
 */
@RunWith(JUnit4.class)
public class SubjectTest {

    /**
     * Test the natural sorting of Subject Objects.
     */
    @Test
    public void compareTo() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(Subject.newSubjectForTesting("C"));
        subjects.add(Subject.newSubjectForTesting("A"));
        subjects.add(Subject.newSubjectForTesting("B"));
        Collections.sort(subjects);

        // A
        assertEquals("A", subjects.get(0).getName());

        // B
        assertEquals("B", subjects.get(1).getName());

        // C
        assertEquals("C", subjects.get(2).getName());
    }
}
