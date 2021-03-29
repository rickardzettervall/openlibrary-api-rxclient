package tech.zettervall.openlibrary.rxclient.models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class Subject {

    @SerializedName("key")
    private final String key;

    @SerializedName("name")
    private final String name;

    @SerializedName("subject_type")
    private final String subjectType;

    @SerializedName("work_count")
    private final int workCount;

    @SerializedName("works")
    private final Work[] works;

    public Subject(String key, String name, String subjectType, int workCount, Work[] works) {
        this.key = key;
        this.name = name;
        this.subjectType = subjectType;
        this.workCount = workCount;
        this.works = works;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public int getWorkCount() {
        return workCount;
    }

    public Work[] getWorks() {
        return works;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", workCount=" + workCount +
                '}';
    }

    /**
     * Convert Subject API response to Subject.
     */
    public static <T extends Subject> T jsonConverter(Class<T> subjectClass, JsonObject obj) {
        return new Gson().fromJson(obj, subjectClass);
    }
}
