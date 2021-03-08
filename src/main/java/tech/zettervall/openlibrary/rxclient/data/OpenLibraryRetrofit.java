package tech.zettervall.openlibrary.rxclient.data;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class OpenLibraryRetrofit {

    public static final String BASE_URL = "http://openlibrary.org/";

    private static OpenLibraryRetrofit INSTANCE = null;
    private final OpenLibraryApi openLibApi;

    private OpenLibraryRetrofit(OpenLibraryApi openLibApi) {
        this.openLibApi = openLibApi;
    }

    public static OpenLibraryRetrofit getInstance() {
        if (INSTANCE == null) {
            synchronized (OpenLibraryRetrofit.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OpenLibraryRetrofit(
                            new Retrofit.Builder()
                                    .baseUrl(BASE_URL)
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                                    .build()
                                    .create(OpenLibraryApi.class)
                    );
                }
            }
        }
        return INSTANCE;
    }

    public OpenLibraryApi getOpenLibApi() {
        return openLibApi;
    }
}
