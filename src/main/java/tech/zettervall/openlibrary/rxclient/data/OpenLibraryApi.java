package tech.zettervall.openlibrary.rxclient.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import tech.zettervall.openlibrary.rxclient.models.Edition;
import tech.zettervall.openlibrary.rxclient.models.RecentChanges;
import tech.zettervall.openlibrary.rxclient.models.SearchResult;
import tech.zettervall.openlibrary.rxclient.models.Work;

/**
 * Open Library API.
 * https://openlibrary.org/dev/docs/api/
 */
interface OpenLibraryApi {

    /**
     * Works API.
     *
     * @param workID Work ID, e.g OL45883W.
     * @return Observable Single<Work>.
     */
    @GET("works/{id}.json")
    Single<Work> getWork(
            @Path("id") String workID
    );

    /**
     * Editions API.
     *
     * @param editionID EditionsID, e.g. OL7353617M.
     * @return Observable Single<Edition>.
     */
    @GET("books/{id}.json")
    Single<Edition> getEdition(
            @Path("id") String editionID
    );

    /**
     * ISBN API.
     *
     * @param isbn Valid ISBN (10 or 13), e.g. 9780140328721.
     * @return Observable Single<Edition>
     */
    @GET("isbn/{isbn}.json")
    Single<Edition> getEditionByIsbn(
            @Path("isbn") String isbn
    );

    /**
     * Books API.
     *
     * @param bibkeys String of one or more comma (,) separated identifying keys, e.g. ISBN:0201558025.
     * @param format  Type of response, in this case it must be set to "json".
     * @param cmd     Control how extensive the response should be: 'viewapi', 'data' or 'details'
     * @return JsonObject of the API response, conversion to POJO is not possible because the
     * returned labels are dynamic.
     */
    @GET("api/books")
    Single<JsonObject> getBooks(
            @Query("bibkeys") String bibkeys,
            @Query("format") String format,
            @Query("jscmd") String cmd
    );

    /**
     * Search API.
     * "WARNING: This is an experimental API and can change in future."
     *
     * @param query       General query.
     * @param titleQuery  Query for book title.
     * @param authorQuery Query for authors.
     * @param page        Which page to get results from.
     * @return Observable Single<SearchResult>
     */
    @GET("search.json")
    Single<SearchResult> search(
            @Query("q") String query,
            @Query("title") String titleQuery,
            @Query("author") String authorQuery,
            @Query("page") Integer page
    );

    /**
     * Subjects API.
     * "This API is experimental. Please be aware that this may change in future."
     *
     * @param subject   The Subject to query.
     * @param eBooks    Set to true to only include the works which have an e-book in the response.
     * @param published Filter on published year range, e.g. 1990-2000 or just 1990.
     * @param limit     Number of works to include in the response.
     * @param offset    Starting offset in the total works, used for pagination.
     * @param details   Receive more detailed response: Related subjects, prominent publishers,
     *                  prolific authors and publishing_history.
     * @return JsonObject of the API response, generics isn't supported here so it's converted
     * in Subject jsonConverter method.
     */
    @GET("subjects/{subject}.json")
    Single<JsonObject> getSubject(
            @Path("subject") String subject,
            @Query("ebooks") Boolean eBooks,
            @Query("published_in") String published,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset,
            @Query("details") Boolean details
    );

    /**
     * RecentChanges API.
     * "This API is experimental. Please be aware that this may change in future."
     *
     * @param path   Used to set custom path such as date and/or author merges.
     * @param limit  "Maximum number of entries in the response. The default value is
     *               100 and the allowed maximum limit is 1000 for performance reasons."
     * @param bots   "Use value true to get only bot changes and
     *               use value false to get only human changes."
     * @param offset "Number of entries to skip in the response. The default value is
     *               0 and the allowed maximum is 10000 for performance reasons."
     * @return Observable Single<RecentChanges[]>
     */
    @GET("recentchanges/{path}.json")
    Single<RecentChanges[]> getRecentChanges(
            @Path("path") String path,
            @Query("limit") Integer limit,
            @Query("offset") Integer offset,
            @Query("bots") Boolean bots
    );

    /**
     * RecentChanges API.
     * "This API is experimental. Please be aware that this may change in future."
     *
     * @param limit  "Maximum number of entries in the response. The default value is
     *               100 and the allowed maximum limit is 1000 for performance reasons."
     * @param bots   "Use value true to get only bot changes and
     *               use value false to get only human changes."
     * @param offset "Number of entries to skip in the response. The default value is
     *               0 and the allowed maximum is 10000 for performance reasons."
     * @return Observable Single<RecentChanges[]>
     */
    @GET("recentchanges.json")
    Single<RecentChanges[]> getRecentChanges(
            @Query("limit") Integer limit,
            @Query("offset") Integer offset,
            @Query("bots") Boolean bots
    );

    /**
     * Read API.
     *
     * @param idType  Can be 'isbn', 'lccn', 'oclc' or 'olid' (Open Library Identifier).
     * @param idValue Is the actual library identifier.
     * @return JsonObject of the API response, conversion to POJO is not possible because the
     * returned labels are dynamic.
     */
    @GET("api/volumes/brief/{id-type}/{id-value}.json")
    Single<JsonElement> getReads(
            @Path("id-type") String idType,
            @Path("id-value") String idValue
    );
}
