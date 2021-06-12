package tech.zettervall.openlibrary.rxclient.data;

import com.google.gson.JsonObject;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Single;
import tech.zettervall.openlibrary.rxclient.models.*;

import java.security.InvalidParameterException;
import java.util.List;

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
     * @return Observable Single response of Work.
     */
    public Single<Work> getWork(@NonNull String workID) {
        return openLibraryApi.getWork(workID);
    }

    /**
     * Get Edition from Open Library API.
     *
     * "Work metadata will include general umbrella information about a book,
     * whereas an Edition will have a publisher, an ISBN, a book-jacket, and
     * other specific information."
     *
     * @param editionID ID of Edition.
     * @return Observable Single response of Edition.
     */
    public Single<Edition> getEdition(@NonNull String editionID) {
        return openLibraryApi.getEdition(editionID);
    }

    /**
     * Get Edition from Open Library API.
     *
     * "Work metadata will include general umbrella information about a book,
     * whereas an Edition will have a publisher, an ISBN, a book-jacket, and
     * other specific information."
     *
     * @param isbn Valid ISBN (10 or 13).
     * @return Observable Single response of Edition.
     */
    public Single<Edition> getEditionByISBN(@NonNull String isbn) {
        return openLibraryApi.getEditionByIsbn(isbn);
    }

    /**
     * Get Books from Open Library API.
     *
     * "The Book API is a generic, flexible, configurable endpoint which allows
     * requesting information on one or more books using ISBNs, OCLC Numbers,
     * LCCNs and OLIDs (Open Library IDs)."
     *
     * It is advised to use BookView or BookData, BookDetailed is a less stable format.
     *
     * @param bookClass   Book class to receive as response object,
     *                    BookView.class = Least details.
     *                    BookData.class = Default details.
     *                    BookDetailed.class = Most details.
     * @param identifiers Book identifiers to query, e.g. ISBN:0201558025.
     * @return Observable Single response of List<bookClass>.
     */
    public <T extends Book> Single<List<T>> getBooks(@NonNull Class<T> bookClass,
                                                     @NonNull String... identifiers)
            throws InvalidParameterException {
        if (bookClass == Book.class) {
            throw new InvalidParameterException("Book.class not allowed as bookClass parameter");
        }
        StringBuilder identifier = new StringBuilder();
        for (int i = 0; i < identifiers.length; i++) {
            identifier.append(identifiers[i]);
            if (i < identifiers.length - 1) {
                identifier.append(",");
            }
        }
        String jsCmd;
        if (bookClass == BookData.class) {
            jsCmd = JSCMD_DATA;
        } else if (bookClass == BookDetailed.class) {
            jsCmd = JSCMD_DETAILS;
        } else {
            jsCmd = JSCMD_VIEWAPI;
        }
        return openLibraryApi.getBooks(identifier.toString(), FORMAT_JSON, jsCmd)
                .map(jsonObject -> {
                    if (jsonObject.keySet().size() == 0) {
                        throw new Exception(HTTP_404);
                    }
                    return Book.jsonConverter(bookClass, jsonObject);
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
     * @return Observable Single response of SearchResult.
     */
    public Single<SearchResult> search(@NonNull SearchType searchType,
                                       @NonNull String query,
                                       @Nullable Integer page) {
        String reformattedQuery = query.trim().replaceAll(" +", "+");
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
        return response.
                map(searchResult -> {
                    if (searchResult.getResults().length == 0) {
                        throw new Exception(HTTP_404);
                    }
                    return searchResult;
                });
    }

    /**
     * Get Works by Subject from Open Library API.
     * "This API is experimental. Please be aware that this may change in future."
     *
     * @param subjectClass        Subject class to receive as response object.
     * @param subject             The Subject to query.
     * @param eBooks              Set to true to only include the works which have an e-book in the response.
     * @param publishedRangeStart Filter on published year range, start year.
     * @param publishedRangeEnd   Filter on published year range, end year.
     * @param limit               Number of works to include in the response.
     * @param offset              Starting offset in the total works, used for pagination.
     * @return Observable Single response of subjectClass.
     */
    public <T extends Subject> Single<T> getSubject(
            @NonNull Class<T> subjectClass,
            @NonNull String subject,
            @Nullable Boolean eBooks,
            @Nullable Integer publishedRangeStart,
            @Nullable Integer publishedRangeEnd,
            @Nullable Integer limit,
            @Nullable Integer offset) {
        return openLibraryApi.getSubject(
                subject,
                eBooks,
                getPublishedRangeString(publishedRangeStart, publishedRangeEnd),
                limit,
                offset,
                (subjectClass == SubjectDetailed.class ? true : null))
                .map(jsonObject -> {
                    if (jsonObject.keySet().size() == 0) {
                        throw new Exception(HTTP_404);
                    }
                    return Subject.jsonConverter(subjectClass, jsonObject);
                });
    }

    /**
     * Get RecentChanges from Open Library API.
     * "This API is experimental. Please be aware that this may change in future."
     *
     * @param bots         "Use value true to get only bot changes and
     *                     use value false to get only human changes."
     * @param limit        "Maximum number of entries in the response. The default value is
     *                     100 and the allowed maximum limit is 1000 for performance reasons."
     * @param offset       "Number of entries to skip in the response. The default value is
     *                     0 and the allowed maximum is 10000 for performance reasons."
     * @param authorMerges Show recent Author merges (default is false).
     * @param year         Changes in a specific year.
     * @param month        Changes in a specific month (year must also be set).
     * @param day          Changes in a specific day (year & month must also be set).
     * @return Observable Single response of RecentChanges.
     */
    public Single<RecentChanges[]> getRecentChanges(@Nullable Boolean bots,
                                                    @Nullable Integer limit,
                                                    @Nullable Integer offset,
                                                    @Nullable Boolean authorMerges,
                                                    @Nullable Integer year,
                                                    @Nullable Integer month,
                                                    @Nullable Integer day) throws InvalidParameterException {
        if (limit != null && (limit < 0 || limit > 1000)) {
            throw new InvalidParameterException("Limit must be either null or between 0 and 1000!");
        }
        if (offset != null && (offset < 0 || offset > 10000)) {
            throw new InvalidParameterException("Offset must be either null or between 0 and 10000!");
        }

        if (authorMerges != null || year != null || month != null || day != null) { // Custom path
            StringBuilder customPath = new StringBuilder();
            if (year != null) {
                customPath.append(year);
            }
            if (month != null) {
                if (year != null) {
                    customPath.append("/");
                    if (month.toString().length() == 1) {
                        customPath.append("0");
                    }
                    customPath.append(month);
                } else {
                    throw new InvalidParameterException("Must include year when using month parameter.");
                }
            }
            if (day != null) {
                if (year != null && month != null) {
                    customPath.append("/");
                    if (day.toString().length() == 1) {
                        customPath.append("0");
                    }
                    customPath.append(day);
                } else {
                    throw new InvalidParameterException("Must include year & month when using day parameter.");
                }
            }
            if (authorMerges != null && authorMerges) {
                if (year != null) {
                    customPath.append("/");
                }
                customPath.append("merge-authors");
            }
            return openLibraryApi.getRecentChanges(customPath.toString(), limit, offset, bots);
        } else { // Regular path
            return openLibraryApi.getRecentChanges(limit, offset, bots);
        }
    }

    /**
     * Get Read from Open Library API.
     *
     * "The Open Library Read API is intended to make it easy to turn book identifier
     * information (ISBN, LCCN etc.) into direct links to Open Library's online-readable
     * or borrowable books."
     *
     * @param idType  Can be 'isbn', 'lccn', 'oclc' or 'olid' (Open Library Identifier).
     * @param idValue Is the actual library identifier.
     * @return Observable Single response of Read.
     */
    public Single<Read> getRead(@NonNull IdType idType, @NonNull String idValue) {
        String idTypeStr = "";
        switch (idType) {
            case ISBN:
                idTypeStr = "isbn";
                break;
            case OCLC:
                idTypeStr = "oclc";
                break;
            case LCCN:
                idTypeStr = "lccn";
                break;
            case OLID:
                idTypeStr = "olid";
                break;
        }
        return openLibraryApi.getReads(idTypeStr, idValue)
                .map(jsonElement -> {
                    if (jsonElement instanceof JsonObject) {
                        if (((JsonObject) jsonElement).keySet().size() == 0) {
                            throw new Exception(HTTP_404);
                        }
                        return Read.jsonConverter((JsonObject) jsonElement);
                    } else {
                        throw new Exception(HTTP_404);
                    }
                });
    }

    /**
     * Get published range String for Subject API calls.
     *
     * @param range e.g. 1990, 2000 or just 1990.
     * @return Valid year range String, null when no range is used.
     */
    @Nullable
    public String getPublishedRangeString(Integer... range) throws IllegalArgumentException {
        if (range.length == 0 || range.length > 2) {
            throw new IllegalArgumentException("Range must be of length 1-2");
        }
        StringBuilder str = new StringBuilder();
        if (range[0] != null) {
            str.append(range[0]);
        }
        if (range[1] != null) {
            if (!str.toString().isEmpty()) {
                str.append("-");
            }
            str.append(range[1]);
        }
        return !str.toString().isEmpty() ? str.toString() : null;
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

    public enum IdType {
        ISBN, OCLC, LCCN, OLID
    }

    public enum CoverSize {
        S, M, L
    }

    public enum SearchType {
        Q, TITLE, AUTHOR
    }
}
