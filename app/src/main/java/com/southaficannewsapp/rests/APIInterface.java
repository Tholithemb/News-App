package com.southaficannewsapp.rests;


import com.southaficannewsapp.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("top-headlines")
    Call<ResponseModel> getLatestNews(@Query("country") String country,
                                      @Query("category") String category,
                                      @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Call<ResponseModel> getSAHeadlines(@Query("country") String country,
                                      @Query("apiKey") String apiKey);




}
