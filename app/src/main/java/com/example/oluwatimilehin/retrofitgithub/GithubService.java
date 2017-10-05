package com.example.oluwatimilehin.retrofitgithub;

import com.example.oluwatimilehin.retrofitgithub.models.GithubIssue;
import com.example.oluwatimilehin.retrofitgithub.models.GithubRepo;

import java.util.List;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Oluwatimilehin on 05/10/2017.
 * oluwatimilehinadeniran@gmail.com.
 */

public interface GithubService {

    @GET("/user/repos?sort=created")
    Single<List<GithubRepo>> getRepos();

    @GET("/repos/{owner}/{repo}/issues")
    Single<List<GithubIssue>> getIssues(@Path("owner") String owner, @Path("repo") String repo);

    @POST()
    Single<RequestBody> postComment(@Url String url, @Body GithubIssue issue);
}
