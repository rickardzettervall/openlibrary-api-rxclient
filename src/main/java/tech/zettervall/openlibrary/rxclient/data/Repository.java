package tech.zettervall.openlibrary.rxclient.data;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;
import tech.zettervall.openlibrary.rxclient.models.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class Repository {

    private static final String FORMAT_JSON = "json";
    private static final String JSCMD_VIEWAPI = "viewapi";
    private static final String JSCMD_DATA = "data";
    private static final String JSCMD_DETAILS = "details";
    private static final String HTTP_404 = "HTTP 404 Not Found";

    private final OpenLibraryApi openLibraryApi = OpenLibraryRetrofit.getInstance().getOpenLibApi();

    /**
     * Get Work from Open Library API.
     *
     * "A Work is a logical collection of similar Editions. "Fantastic Mr. Fox" could
     * be a Work which contains a Spanish translation edition, or perhaps a 2nd edition
     * which has an additional chapter or corrections. Work metadata will include general
     * umbrella information about a book."
     *
     * @param workID ID of Work.
     */
    public Single<Work> getWork(@NonNull String workID) {
        return openLibraryApi.getWork(workID, FORMAT_JSON);
    }

    /**
     * Get Edition from Open Library API.
     *
     * "Work metadata will include general umbrella information about a book,
     * whereas an Edition will have a publisher, an ISBN, a book-jacket, and
     * other specific information."
     *
     * @param editionID ID of Edition.
     */
    public Single<Edition> getEdition(@NonNull String editionID) {
        return openLibraryApi.getEdition(editionID, FORMAT_JSON);
    }

    /**
     * Get Edition from Open Library API.
     *
     * "Work metadata will include general umbrella information about a book,
     * whereas an Edition will have a publisher, an ISBN, a book-jacket, and
     * other specific information."
     *
     * @param isbn Valid ISBN (10 or 13).
     */
    public Single<Edition> getEditionByISBN(@NonNull String isbn) {
        return openLibraryApi.getEditionByIsbn(isbn, FORMAT_JSON);
    }

    /**
     * Get Books from Open Library API.
     *
     * "The Book API is a generic, flexible, configurable endpoint which allows
     * requesting information on one or more books using ISBNs, OCLC Numbers,
     * LCCNs and OLIDs (Open Library IDs)."
     *
     * It is advised to use BookView or BookData, BookDetail is a less stable format.
     *
     * @param bookClass   Decides what type of details each queried Book should have.
     * @param identifiers Book identifiers to query, e.g. ISBN:0201558025.
     */
    public <T extends Book> Single<List<T>> getBooks(@NonNull Class<T> bookClass,
                                                     @NonNull String... identifiers) {
        String identifier = Arrays.stream(identifiers).map(String::trim)
                .collect(Collectors.joining(","));
        String jsCmd;
        if (bookClass == BookData.class) {
            jsCmd = JSCMD_DATA;
        } else if (bookClass == BookDetailed.class) {
            jsCmd = JSCMD_DETAILS;
        } else {
            jsCmd = JSCMD_VIEWAPI;
        }
        return openLibraryApi.getBooks(identifier, FORMAT_JSON, jsCmd)
                .flatMap(jsonObject -> {
                    if (jsonObject.keySet().size() == 0) {
                        throw new Exception(HTTP_404);
                    }
                    return Single.just(Book.jsonConverter(bookClass, jsonObject));
                });
    }

    /**
     * Search the Open Library API.
     * "WARNING: This is an experimental API and can change in future."
     *
     * @param searchType Type of query to perform.
     * @param query      What to query for.
     * @param page       Which page to get the results for. The returned SearchResult has
     *                   a parameter called numFound, this is the number of found matches.
     *                   Each page produce 100 results, e.g. no page (page 1) returns result 1-99,
     *                   page 2 returns results 100-199 and so on.
     */
    public Single<SearchResult> search(@NonNull SearchType searchType,
                                       @NonNull String query,
                                       @Nullable Integer page) {
        String reformattedQuery = query.replaceAll(" ", "+");
        Single<SearchResult> response;
        switch (searchType) {
            case TITLE:
                response = openLibraryApi.search(null, reformattedQuery, null, page);
                break;
            case AUTHOR:
                response = openLibraryApi.search(null, null, reformattedQuery, page);
                break;
            default:
                response = openLibraryApi.search(reformattedQuery, null, null, page);
        }
        return response.flatMap(result -> {
            if (result.getResults().length == 0) {
                throw new Exception(HTTP_404);
            }
            return Single.just(result);
        });
    }

    /**
     * Get cover URL for a book.
     *
     * "Book covers can be accessed using Cover ID (internal cover ID), OLID (Open Library ID),
     * ISBN, OCLC, LCCN and other identifiers like librarything and goodreads."
     * If any IP tries to access more that the allowed limit, the service will return "403 Forbidden" status.
     *
     * @param key                Type of key identifier to use.
     * @param value              Value for the chosen key type.
     * @param size               Image size.
     * @param blankInvalidReturn Set to true to get a blank image from URL when no image exist
     *                           for the request, set to false to return a 404 response.
     * @return URL in String format.
     */
    public static String getCoverUrl(@NonNull CoverKey key, @NonNull String value,
                                     @NonNull CoverSize size, boolean blankInvalidReturn) {
        StringBuilder url = new StringBuilder("http://covers.openlibrary.org/b/");
        switch (key) {
            case ISBN:
                url.append("isbn");
                break;
            case OCLC:
                url.append("oclc");
                break;
            case LCCN:
                url.append("lccn");
                break;
            case OLID:
                url.append("olid");
                break;
            case ID:
                url.append("id");
                break;
        }
        url.append("/").append(value).append("-");
        switch (size) {
            case S:
                url.append("S");
                break;
            case M:
                url.append("M");
                break;
            case L:
                url.append("L");
                break;
        }
        url.append(".jpg");
        if (!blankInvalidReturn) {
            url.append("?default=false");
        }
        return url.toString();
    }

    public enum CoverKey {
        ISBN, OCLC, LCCN, OLID, ID
    }

    public enum CoverSize {
        S, M, L
    }

    public enum SearchType {
        Q, TITLE, AUTHOR
    }
}
