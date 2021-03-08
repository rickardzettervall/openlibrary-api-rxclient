package tech.zettervall.openlibrary.rxclient;

import tech.zettervall.openlibrary.rxclient.data.Repository;

/**
 * Entry point for Open Library API requests.
 */
public final class OpenLibraryClient {

    private static OpenLibraryClient INSTANCE = null;
    private final Repository repository;

    private OpenLibraryClient(Repository repository) {
        this.repository = repository;
    }

    /**
     * Get Open Library Client instance.
     */
    public static OpenLibraryClient getInstance() {
        if (INSTANCE == null) {
            synchronized (OpenLibraryClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OpenLibraryClient(new Repository());
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Get the Open Library Repository, use this for all requests.
     */
    public Repository getRepository() {
        return repository;
    }
}
