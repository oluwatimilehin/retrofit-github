package com.example.oluwatimilehin.retrofitgithub.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Oluwatimilehin on 05/10/2017.
 * oluwatimilehinadeniran@gmail.com.
 */

public class GithubIssue {
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public String getComment() {
        return comment;
    }

    String id;
    String title;

    @SerializedName("comments_url")
    String commentsUrl;

    @SerializedName("body")
    String comment;

    @Override
    public String toString() {
        return id + "-" + title;
    }
}
