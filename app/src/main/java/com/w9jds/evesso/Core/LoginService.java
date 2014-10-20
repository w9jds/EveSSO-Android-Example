package com.w9jds.evesso.Core;

import android.util.Base64;
import com.w9jds.evesso.Classes.Constants;

import java.util.Map;
import java.util.TreeMap;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by Jeremy Shore on 10/19/2014.
 */
public class LoginService {

    private interface AuthService {
        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        @POST("/oauth/token")
        void getToken(@QueryMap Map<String, String> queries, Callback<Object> callback);

        @Headers({"Content-Type: application/x-www-form-urlencoded"})
        @GET("/oauth/verify")
        void getVerification(Callback<Object> callback);
    }

    public static void getToken(String code, Callback<Object> callback) {
        TreeMap<String, String> queries = new TreeMap<String, String>();
        queries.put("grant_type", "authorization_code");
        queries.put("code", code);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Constants.BASE_URI)
                .setRequestInterceptor(buildAuthRequestInterceptor(buildClientAuthValue()))
                .build();

        AuthService authService = restAdapter.create(AuthService.class);
        authService.getToken(queries, callback);
    }

    public static void getVerification(String token, Callback<Object> callback) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Constants.BASE_URI)
                .setRequestInterceptor(buildAuthRequestInterceptor(token))
                .build();

        AuthService authService = restAdapter.create(AuthService.class);
        authService.getVerification(callback);
    }

    private static String buildClientAuthValue() {
        return Constants.HEADER_AUTHORIZATION_VALUE_PREFIX +
                Base64.encodeToString(String.format(
                    "%s:%s",
                    Constants.CLIENT_ID,
                    Constants.SECRET).getBytes(), Base64.NO_WRAP);
    }

    private static RequestInterceptor buildAuthRequestInterceptor(final String authValue) {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Authorization", authValue);
                request.addHeader("Host", Constants.BASE_URI.split("//")[1]);
            }
        };
    }


}
