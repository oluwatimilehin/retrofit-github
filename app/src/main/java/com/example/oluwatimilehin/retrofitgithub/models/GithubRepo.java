package com.example.oluwatimilehin.retrofitgithub.models;

/**
 * Created by Oluwatimilehin on 05/10/2017.
 * oluwatimilehinadeniran@gmail.com.
 */

public class GithubRepo {

    String name;
    String url;
    String owner;

    @Override
    public String toString() {
        return(name + " " +  url);
    }
}
