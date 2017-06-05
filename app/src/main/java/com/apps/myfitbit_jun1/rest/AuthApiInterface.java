package com.apps.myfitbit_jun1.rest;

import com.apps.myfitbit_jun1.Models.AuthToken;
import com.apps.myfitbit_jun1.Models.AuthTokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Kavitha on 6/1/2017.
 */

public interface AuthApiInterface {


    @POST("/oauth2/token")
    //@Headers({"Content-Type:application/x-www-form-urlencoded"})
    public Call<AuthToken> getToken(@Header("Authorization") String autho, @Header("Content-Type") String contentType, @Body AuthTokenRequest getAuthToken);
    //public Call<AuthToken> getToken(@Header("Authorization") String autho,  @Body AuthTokenRequest getAuthToken);
}


