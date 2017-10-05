package com.example.oluwatimilehin.retrofitgithub.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Oluwatimilehin on 05/10/2017.
 * oluwatimilehinadeniran@gmail.com.
 */

public class GithubRepoDeserializer implements JsonDeserializer<GithubRepo> {
    @Override
    public GithubRepo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        GithubRepo githubRepo = new GithubRepo();

        JsonObject repoObject = json.getAsJsonObject();
        githubRepo.name = repoObject.get("name").getAsString();
        githubRepo.url = repoObject.get("url").getAsString();

        JsonObject ownerObject = repoObject.get("owner").getAsJsonObject();
        githubRepo.owner = ownerObject.get("login").getAsString();

        return githubRepo;
    }
}
