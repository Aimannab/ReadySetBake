package com.example.android.readysetbake;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aiman Nabeel on 04/06/2018.
 */

//Ref: https://github.com/nikosvaggalis/Udacity-Advanced-Developer-Nanodegree-Baking-App-2017/blob/master/app/src/main/java/com/example/android/recipe/retrofit/RetrofitBuilder.java
public class RetrofitBuilder {

    static RecipesApiService iRecipe;

    public static RecipesApiService Retrieve() {

        Gson gson = new GsonBuilder().create();

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();


        iRecipe = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build().create(RecipesApiService.class);

        return iRecipe;
    }
}
