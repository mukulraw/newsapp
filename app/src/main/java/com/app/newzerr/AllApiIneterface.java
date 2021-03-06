package com.app.newzerr;

import com.app.newzerr.breakingPOJO.breakingBean;
import com.app.newzerr.catPOJO.catBean;
import com.app.newzerr.cityPOJO.cityBean;
import com.app.newzerr.newsDetailsPOJO.newsDetailsBean;
import com.app.newzerr.newsPOJO.newsBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AllApiIneterface {

    @GET("newsapp/api/getCat.php")
    Call<catBean> getCat();

    @GET("newsapp/api/getBreaking.php")
    Call<breakingBean> getBreaking();

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

    @Multipart
    @POST("newsapp/api/login.php")
    Call<loginBean> login(
            @Part("email") String email,
            @Part("password") String password,
            @Part("name") String name,
            @Part("token") String token
    );

}
