package com.apps.myfitbit_jun1.rest;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.apps.myfitbit_jun1.Models.AuthToken;
import com.apps.myfitbit_jun1.Models.AuthTokenRequest;
import com.apps.myfitbit_jun1.R;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCodeActivity extends AppCompatActivity {

    WebView webView;
    String TAG = getClass().getSimpleName();
String code;
    String access_Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);


        String urls = "https://www.fitbit.com/oauth2/authorize?" +
                "response_type=code" +
                "&client_id=228K7X" +
                "&expires_in=1000" +
                "&scope=profile%20settings%20weight" +
                "&redirect_uri=http://www.chronicwatch.com/" ;

        Log.i(TAG," urls ? "+urls);

        webView.loadUrl(urls);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.i(TAG," onPageFinished ? "+url);
                String result[]=url.split("=");
                String result2 = result[1].split("#")[0];
                code = result2;
                Log.i(TAG," onPageFinished code ? "+code);

                String basic = "228K7X:1f92e3efa01658587abf868ddc6fd284";
                String clientIdclientSecret = null;

                //Get Refresh Token here
                try {
                    clientIdclientSecret= Base64.encodeToString(basic.getBytes(), Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("clientIdclientSecret " + clientIdclientSecret);

                AuthTokenRequest authTokenRequest = new AuthTokenRequest();

                authTokenRequest.setCode(code);
                authTokenRequest.setRedirect_uri("http://www.chronicwatch.com/");
                authTokenRequest.setClientId("228K7X");
                authTokenRequest.setGrant_type("authorization_code");


                AuthApiInterface apiInterface = AuthApiClient.getClient().create(AuthApiInterface.class);



                Log.i(TAG,"Content type ? "+"application/x-www-form-urlencoded");
                Log.i(TAG,"authTokenRequest ? "+"authTokenRequest "+new Gson().toJson(authTokenRequest));




                String auth_header = ("Basic "+clientIdclientSecret).toString().trim();
                Log.i(TAG,"Auth ?  "+"auth_header "+auth_header);
                Call<AuthToken> authTokenCall = apiInterface.getToken(auth_header,"application/x-www-form-urlencoded",authTokenRequest);

                authTokenCall.enqueue(new Callback<AuthToken>() {


                    @Override
                    public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {

                        Log.i(TAG,"onResponse Called 0 "+new Gson().toJson(response));

                        Log.d("Call request", "=================================");
                        Log.d("Call request", call.request().toString());
                        Log.d("Call request header", call.request().headers().toString());
                        Log.d("Response raw header", response.headers().toString());
                        Log.d("Response raw", new Gson().toJson(response.raw().body()));
                        Log.d("Response code", String.valueOf(response.code()));
                        Log.d("Response isSuccessful", "  ? "+response.isSuccessful());
                        Log.d("Response message", "  ? "+response.message());
                        Log.d("Call request", "=================================");

                        if (response.isSuccessful()){
                            AuthToken responseBody = (AuthToken) response.body();
                            //you can do whatever with the response body now...
                            String responseBodyString= responseBody.toString();
                            Log.d("Response body", new Gson().toJson(responseBodyString));

                        }else {
                            Log.d("Response errorBody", new Gson().toJson(response.errorBody()));
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthToken> call, Throwable t) {
                        Log.i(TAG,"onFailure Called "+t.toString());
                    }
                });

            }

        });


    }

}
