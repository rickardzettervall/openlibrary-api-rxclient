package tech.zettervall.openlibrary.rxclient.models;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public final class Search {

    @SerializedName("start")
    private final String start;

    @SerializedName("num_found")
    private final int numFound;

    @SerializedName("docs")
    private final Doc[] docs;

    public Search(String start, int numFound, Doc[] docs) {
        this.start = start;
        this.numFound = numFound;
        this.docs = docs;
    }

    public String getStart() {
        return start;
    }

    public int getNumFound() {
        return numFound;
    }

    public Doc[] getDocs() {
        return docs;
    }

    @Override
    public String toString() {
        return "Search{" +
                "start='" + start + '\'' +
                ", numFound=" + numFound +
                ", docs=" + Arrays.toString(docs) +
                '}';
    }

    public static final class Doc {

        @SerializedName("cover_i")
        private final int coverIdentifier;

        @SerializedName("has_fulltext")
        private final boolean hasFullText;

        @SerializedName("edition_count")
        private final int editionCount;

        @SerializedName("title")
        private final String title;

        @SerializedName("author_name")
        private final String[] authorsNames;

        @SerializedName("first_publish_year")
        private final int firstPublishYear;

        @SerializedName("key")
        private final String key;

        @SerializedName("ia")
        private final String[] ias;

        @SerializedName("author_key")
        private final String[] authorKeys;

        @SerializedName("public_scan_b")
        private final boolean publicScanB;

        public Doc(int coverIdentifier, boolean hasFullText, int editionCount, String title,
                   String[] authorsNames, int firstPublishYear, String key, String[] ias,
                   String[] authorKeys, boolean publicScanB) {
            this.coverIdentifier = coverIdentifier;
            this.hasFullText = hasFullText;
            this.editionCount = editionCount;
            this.title = title;
            this.authorsNames = authorsNames;
            this.firstPublishYear = firstPublishYear;
            this.key = key;
            this.ias = ias;
            this.authorKeys = authorKeys;
            this.publicScanB = publicScanB;
        }

        public int getCoverIdentifier() {
            return coverIdentifier;
        }

        public boolean isHasFullText() {
            return hasFullText;
        }

        public int getEditionCount() {
            return editionCount;
        }

        public String getTitle() {
            return title;
        }

        public String[] getAuthorsNames() {
            return authorsNames;
        }

        public int getFirstPublishYear() {
            return firstPublishYear;
        }

        public String getKey() {
            return key;
        }

        public String[] getIas() {
            return ias;
        }

        public String[] getAuthorKeys() {
            return authorKeys;
        }

        public boolean isPublicScanB() {
            return publicScanB;
        }

        @Override
        public String toString() {
            return "Doc{" +
                    "coverIdentifier=" + coverIdentifier +
                    ", hasFullText=" + hasFullText +
                    ", editionCount=" + editionCount +
                    ", title='" + title + '\'' +
                    ", authorsNames=" + Arrays.toString(authorsNames) +
                    ", firstPublishYear=" + firstPublishYear +
                    ", key='" + key + '\'' +
                    ", ia=" + Arrays.toString(ias) +
                    ", authorKeys=" + Arrays.toString(authorKeys) +
                    ", publicScanB=" + publicScanB +
                    '}';
        }
    }
}
