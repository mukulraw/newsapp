package com.app.newsapp;

import com.app.newsapp.cityPOJO.cityBean;
import com.app.newsapp.newsDetailsPOJO.newsDetailsBean;
import com.app.newsapp.newsPOJO.newsBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {

    @GET("newsapp/api/getCities.php")
    Call<cityBean> getCities();

    @Multipart
    @POST("newsapp/api/getNews.php")
    Call<newsBean> getNews(
            @Part("city") String city
    );

    @Multipart
    @POST("newsapp/api/getNewsById.php")
    Call<newsDetailsBean> getNewsById(
            @Part("id") String id
    );

    @Multipart
    @POST("newsapp/api/getCityId.php")
    Call<cityIdBean> getCityId(
            @Part("lat") String lat,
            @Part("lng") String lng
    );

}