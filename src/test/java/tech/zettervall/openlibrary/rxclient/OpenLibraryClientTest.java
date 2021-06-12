package tech.zettervall.openlibrary.rxclient;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import tech.zettervall.openlibrary.rxclient.data.Repository;
import tech.zettervall.openlibrary.rxclient.models.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Client test with real API calls.
 */
@RunWith(JUnit4.class)
public class OpenLibraryClientTest {

    private final String API_URL = "http://openlibrary.org/";
    private final OkHttpClient client = new OkHttpClient();
    private final Request request = new Request.Builder()
            .url(API_URL)
            .build();
    private final OpenLibraryClient openLibraryClient = OpenLibraryClient.getInstance();

    @Before
    public void init() {
        // Make sure API server is online
        try {
            Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                fail("API server appears to be offline..");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test a successful API fetch of Work.
     */
    @Test
    public void getWorkSuccess() {
        openLibraryClient.getRepository().getWork("OL45883W")
                .blockingSubscribe(new DisposableSingleObserver<Work>() {
                    @Override
                    public void onSuccess(@NonNull Work work) {
                        assertEquals(work.getKey(), "/works/OL45883W");
                        System.out.println("Received: " + work);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a failed API fetch of Work.
     */
    @Test
    public void getWorkFail() {
        openLibraryClient.getRepository().getWork("")
                .blockingSubscribe(new DisposableSingleObserver<Work>() {
                    @Override
                    public void onSuccess(@NonNull Work work) {
                        fail();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        assertTrue(e.toString().contains("404"));
                    }
                });
    }

    /**
     * Test a successful API fetch of Edition.
     */
    @Test
    public void getEditionSuccess() {
        openLibraryClient.getRepository().getEdition("OL7353617M")
                .blockingSubscribe(new DisposableSingleObserver<Edition>() {
                    @Override
                    public void onSuccess(@NonNull Edition edition) {
                        assertEquals(edition.getKey(), "/books/OL7353617M");
                        System.out.println("Received: " + edition);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a successful API fetch of Edition by ISBN.
     */
    @Test
    public void getEditionByISBNSuccess() {
        openLibraryClient.getRepository().getEditionByISBN("9780140328721")
                .blockingSubscribe(new DisposableSingleObserver<Edition>() {
                    @Override
                    public void onSuccess(@NonNull Edition edition) {
                        assertEquals(edition.getKey(), "/books/OL7353617M");
                        System.out.println("Received: " + edition);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a failed API fetch of Edition.
     */
    @Test
    public void getEditionFail() {
        openLibraryClient.getRepository().getEdition("")
                .blockingSubscribe(new DisposableSingleObserver<Edition>() {
                    @Override
                    public void onSuccess(@NonNull Edition edition) {
                        fail();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        assertTrue(e.toString().contains("404"));
                    }
                });
    }

    /**
     * Test a successful API fetch of BookView.
     */
    @Test
    public void getBookViewSuccess() {
        openLibraryClient.getRepository().getBooks(
                BookView.class, "ISBN:0385472579", "LCCN:62019420"
        ).blockingSubscribe(new DisposableSingleObserver<List<BookView>>() {
            @Override
            public void onSuccess(@NonNull List<BookView> bookViews) {
                assertEquals("ISBN:0385472579", bookViews.get(0).getBibKey());
                assertEquals("LCCN:62019420", bookViews.get(1).getBibKey());
                System.out.println("Received: " + bookViews.get(0));
                System.out.println("Received: " + bookViews.get(1));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail();
            }
        });
    }

    /**
     * Test a successful API fetch of BookData.
     */
    @Test
    public void getBookDataSuccess() {
        openLibraryClient.getRepository().getBooks(
                BookData.class, "ISBN:0385472579", "LCCN:62019420"
        ).blockingSubscribe(new DisposableSingleObserver<List<BookData>>() {
            @Override
            public void onSuccess(@NonNull List<BookData> bookData) {
                assertEquals("0385472579", bookData.get(0).getIdentifiers().getIsbn10()[0]);
                assertEquals("62019420", bookData.get(1).getIdentifiers().getLccn()[0]);
                System.out.println("Received: " + bookData.get(0));
                System.out.println("Received: " + bookData.get(1));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail();
            }
        });
    }

    /**
     * Test a successful API fetch of BookDetailed.
     */
    @Test
    public void getBookDetailedSuccess() {
        openLibraryClient.getRepository().getBooks(
                BookDetailed.class, "ISBN:9780980200447", "ISBN:9780982615416"
        ).blockingSubscribe(new DisposableSingleObserver<List<BookDetailed>>() {
            @Override
            public void onSuccess(@NonNull List<BookDetailed> bookDetailed) {
                assertEquals("ISBN:9780980200447", bookDetailed.get(0).getBibKey());
                assertEquals("ISBN:9780982615416", bookDetailed.get(1).getBibKey());
                System.out.println("Received: " + bookDetailed.get(0));
                System.out.println("Received: " + bookDetailed.get(1));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail();
            }
        });
    }

    /**
     * Test a failed API fetch of BookView.
     */
    @Test
    public void getBookViewFail() {
        openLibraryClient.getRepository().getBooks(
                BookView.class, ""
        ).blockingSubscribe(new DisposableSingleObserver<List<BookView>>() {
            @Override
            public void onSuccess(@NonNull List<BookView> bookViews) {
                fail();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                assertTrue(e.toString().contains("404"));
            }
        });
    }

    /**
     * Test a successful API fetch of SearchResult (Q).
     */
    @Test
    public void searchByQSuccess() {
        openLibraryClient.getRepository().search(Repository.SearchType.Q, "the lord of the rings", null)
                .blockingSubscribe(new DisposableSingleObserver<SearchResult>() {
                    @Override
                    public void onSuccess(@NonNull SearchResult searchResult) {
                        assertEquals(100, searchResult.getResults().length);
                        assertTrue(searchResult.getResults()[0].getTitle().toLowerCase().contains("the lord of the rings"));
                        System.out.println("Received: " + searchResult);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a successful API fetch of SearchResult (Title).
     */
    @Test
    public void searchByTitleSuccess() {
        openLibraryClient.getRepository().search(Repository.SearchType.TITLE, "the lord of the rings", null)
                .blockingSubscribe(new DisposableSingleObserver<SearchResult>() {
                    @Override
                    public void onSuccess(@NonNull SearchResult searchResult) {
                        assertEquals(100, searchResult.getResults().length);
                        assertTrue(searchResult.getResults()[0].getTitle().toLowerCase().contains("the lord of the rings"));
                        System.out.println("Received: " + searchResult);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a successful API fetch of SearchResult (Author).
     */
    @Test
    public void searchByAuthorSuccess() {
        openLibraryClient.getRepository().search(Repository.SearchType.AUTHOR, "tolkien", null)
                .blockingSubscribe(new DisposableSingleObserver<SearchResult>() {
                    @Override
                    public void onSuccess(@NonNull SearchResult searchResult) {
                        assertEquals(100, searchResult.getResults().length);
                        assertTrue(Arrays.toString(searchResult.getResults()[0].getAuthorNames()).toLowerCase().contains("tolkien"));
                        System.out.println("Received: " + searchResult);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a failed API fetch of SearchResult.
     */
    @Test
    public void searchFail() {
        openLibraryClient.getRepository().search(Repository.SearchType.Q, "", null)
                .blockingSubscribe(new DisposableSingleObserver<SearchResult>() {
                    @Override
                    public void onSuccess(@NonNull SearchResult searchResult) {
                        fail();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        assertTrue(e.toString().contains("404"));
                    }
                });
    }

    /**
     * Test a successful API fetch of Subject.
     */
    @Test
    public void getSubjectSuccess() {
        openLibraryClient.getRepository().getSubject(
                Subject.class,
                "cats",
                null,
                null,
                2000,
                null,
                null)
                .blockingSubscribe(new DisposableSingleObserver<Subject>() {
                    @Override
                    public void onSuccess(@NonNull Subject subject) {
                        assertTrue(subject.getWorkCount() > 0);
                        assertTrue(subject.getWorks().length > 0);
                        System.out.println("Received: " + subject);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a successful API fetch of SubjectDetailed.
     */
    @Test
    public void getSubjectDetailedSuccess() {
        openLibraryClient.getRepository().getSubject(
                SubjectDetailed.class,
                "cats",
                null,
                null,
                null,
                null,
                null)
                .blockingSubscribe(new DisposableSingleObserver<SubjectDetailed>() {
                    @Override
                    public void onSuccess(@NonNull SubjectDetailed subjectDetailed) {
                        assertTrue(subjectDetailed.getWorkCount() > 0);
                        assertTrue(subjectDetailed.getWorks().length > 0);
                        assertTrue(subjectDetailed.getAuthors().length > 0);
                        assertTrue(subjectDetailed.getPublishers().length > 0);
                        assertTrue(subjectDetailed.getSubjects().length > 0);
                        assertTrue(subjectDetailed.getPeople().length > 0);
                        assertTrue(subjectDetailed.getPlaces().length > 0);
                        assertTrue(subjectDetailed.getTimes().length > 0);
                        assertTrue(subjectDetailed.getPublishingHistory().length > 0);
                        System.out.println("Received: " + subjectDetailed);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a successful API fetch of RecentChanges (with no custom path).
     */
    @Test
    public void getRecentChangesWithoutCustomPathSuccess() {
        openLibraryClient.getRepository().getRecentChanges(
                false, 3, null, null, null, null, null
        ).blockingSubscribe(new DisposableSingleObserver<RecentChanges[]>() {
            @Override
            public void onSuccess(@NonNull RecentChanges[] recentChanges) {
                assertEquals(3, recentChanges.length);
                System.out.println("Received: " + Arrays.toString(recentChanges));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail();
            }
        });
    }

    /**
     * Test a successful API fetch of RecentChanges (with custom path).
     */
    @Test
    public void getRecentChangesWithCustomPathSuccess() {
        openLibraryClient.getRepository().getRecentChanges(
                false, 3, null, true, 2021, 1, 10
        ).blockingSubscribe(new DisposableSingleObserver<RecentChanges[]>() {
            @Override
            public void onSuccess(@NonNull RecentChanges[] recentChanges) {
                assertEquals(3, recentChanges.length);
                System.out.println("Received: " + Arrays.toString(recentChanges));
            }

            @Override
            public void onError(@NonNull Throwable e) {
                fail();
            }
        });
    }

    /**
     * Test a successful API fetch of Read.
     */
    @Test
    public void getReadSuccess() {
        openLibraryClient.getRepository().getRead(Repository.IdType.ISBN, "0596156715")
                .blockingSubscribe(new DisposableSingleObserver<Read>() {
                    @Override
                    public void onSuccess(@NonNull Read read) {
                        assertNotNull(read.getRecords()[0]);
                        System.out.println("Received: " + read);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fail();
                    }
                });
    }

    /**
     * Test a failed API fetch of Read.
     */
    @Test
    public void getReadFail() {
        openLibraryClient.getRepository().getRead(Repository.IdType.ISBN, "00000000000")
                .blockingSubscribe(new DisposableSingleObserver<Read>() {
                    @Override
                    public void onSuccess(@NonNull Read read) {
                        fail();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        assertTrue(e.toString().contains("404"));
                    }
                });
    }
}
