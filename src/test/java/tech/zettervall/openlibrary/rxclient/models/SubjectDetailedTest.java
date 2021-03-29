package tech.zettervall.openlibrary.rxclient.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * SubjectDetailed Object test.
 */
@RunWith(JUnit4.class)
public class SubjectDetailedTest {

    /**
     * Test the natural sorting of SubjectDetailed Objects.
     */
    @Test
    public void compareTo() {
        List<SubjectDetailed> subjectDetailedList = new ArrayList<>();
        subjectDetailedList.add(SubjectDetailed.newSubjectDetailedForTesting("C"));
        subjectDetailedList.add(SubjectDetailed.newSubjectDetailedForTesting("A"));
        subjectDetailedList.add(SubjectDetailed.newSubjectDetailedForTesting("B"));
        Collections.sort(subjectDetailedList);

        // A
        assertEquals("A", subjectDetailedList.get(0).getName());

        // B
        assertEquals("B", subjectDetailedList.get(1).getName());

        // C
        assertEquals("C", subjectDetailedList.get(2).getName());
    }
}
