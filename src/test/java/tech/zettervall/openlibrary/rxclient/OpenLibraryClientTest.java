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
import retrofit2.adapter.rxjava3.HttpException;
import tech.zettervall.openlibrary.rxclient.data.Repository;
import tech.zettervall.openlibrary.rxclient.models.*;

import java.io.IOException;
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
    public void workSuccess() {
        openLibraryClient.getRepository().getWork("OL45883W")
                .blockingSubscribe(new DisposableSingleObserver<Work>() {
                    @Override
                    public void onSuccess(@NonNull Work work) {
                        assertEquals(work.getKey(), "/works/OL45883W");
                        System.out.println("Received: " + work.toString());
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
    public void workFail() {
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
    public void editionSuccess() {
        openLibraryClient.getRepository().getEdition("OL7353617M")
                .blockingSubscribe(new DisposableSingleObserver<Edition>() {
                    @Override
                    public void onSuccess(@NonNull Edition edition) {
                        assertEquals(edition.getKey(), "/books/OL7353617M");
                        System.out.println("Received: " + edition.toString());
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
    public void editionByISBNSuccess() {
        openLibraryClient.getRepository().getEditionByISBN("9780140328721")
                .blockingSubscribe(new DisposableSingleObserver<Edition>() {
                    @Override
                    public void onSuccess(@NonNull Edition edition) {
                        assertEquals(edition.getKey(), "/books/OL7353617M");
                        System.out.println("Received: " + edition.toString());
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
    public void editionFail() {
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
    public void bookViewSuccess() {
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
    public void bookDataSuccess() {
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
    public void bookDetailedSuccess() {
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
    public void bookViewFail() {
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
     * Test a successful API fetch of SearchResult.
     */
    @Test
    public void searchSuccess() {
        openLibraryClient.getRepository().search(Repository.SearchType.Q, "the lord of the rings", null)
                .blockingSubscribe(new DisposableSingleObserver<SearchResult>() {
                    @Override
                    public void onSuccess(@NonNull SearchResult searchResult) {
                        assertEquals(100, searchResult.getDocs().length);
                        assertTrue(searchResult.getDocs()[0].getTitle().toLowerCase().contains("the lord of the rings"));
                        System.out.println("Received: " + searchResult.toString());
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
}
