package com.example.android.readysetbake;


import retrofit2.Call;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Aiman Nabeel on 04/06/2018.
 */

public interface RecipesApiService {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();
}
