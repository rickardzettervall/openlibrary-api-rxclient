package tech.zettervall.openlibrary.rxclient.data;

import com.google.gson.JsonObject;
import io.reactivex.rxjava3.core.Single;
import tech.zettervall.openlibrary.rxclient.models.Edition;
import tech.zettervall.openlibrary.rxclient.models.Work;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Open Library API.
 * https://openlibrary.org/dev/docs/api/
 */
interface OpenLibraryApi {

    /**
     * Works API.
     *
     * @param workID Work ID, e.g OL45883W.
     * @param format Must be "json".
     * @return Observable Single<Work>.
     */
    @GET("works/{id}.{format}")
    Single<Work> getWork(
            @Path("id") String workID,
            @Path("format") String format
    );

    /**
     * Editions API.
     *
     * @param editionID EditionsID, e.g. OL7353617M.
     * @param format    Must be "json".
     * @return Observable Single<Edition>.
     */
    @GET("books/{id}.{format}")
    Single<Edition> getEdition(
            @Path("id") String editionID,
            @Path("format") String format
    );

    /**
     * ISBN API.
     *
     * @param isbn   Valid ISBN (10 or 13), e.g. 9780140328721.
     * @param format Must be "json".
     * @return Observable Single<Edition>
     */
    @GET("isbn/{isbn}.{format}")
    Single<Edition> getEditionByIsbn(
            @Path("isbn") String isbn,
            @Path("format") String format
    );

    /**
     * Books API.
     *
     * @param bibkeys String of one or more comma (,) separated identifying keys, e.g. ISBN:0201558025.
     * @param format  Type of response, in this case it must be set to "json".
     * @param cmd     Control how extensive the response should be: 'viewapi', 'tech.zettervall.olrxclient.data' or 'details'
     * @return JsonObject of the API response, conversion to POJO is not possible because the
     * returned labels are dynamic.
     */
    @GET("api/books")
    Single<JsonObject> getBooks(
            @Query("bibkeys") String bibkeys,
            @Query("format") String format,
            @Query("jscmd") String cmd
    );
}
