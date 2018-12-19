package com.example.android.bakingapp.network;

import com.example.android.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeRestAdapter {

    @GET(UrlManager.BACKING_JSON)
    Call<List<Recipe>> getRecipes();

}
