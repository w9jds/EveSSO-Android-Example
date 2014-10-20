package com.w9jds.evesso.Activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.internal.LinkedTreeMap;
import com.w9jds.evesso.Classes.Constants;
import com.w9jds.evesso.Classes.Utility;
import com.w9jds.evesso.Core.LoginService;
import com.w9jds.evesso.R;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SignInActivity extends Activity {

    private WebView mWebView;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mWebView = (WebView)findViewById(R.id.sign_in_browser);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.startsWith(Constants.CALLBACK)) {
                    String response = Uri.parse(url).getQueryParameter("code");
                    if (!response.equals("")){
                        LoginService.getToken(response, mTokenCallback);
                        mWebView.stopLoading();
                    }
                    else {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
                super.onPageStarted(view, url, favicon);
            }
        });

        mWebView.loadUrl(Utility.buildAuthUrL());

    }

    private Callback<Object> mTokenCallback = new Callback<Object>() {
        @Override
        public void success(Object json, Response response) {
            LinkedTreeMap<String, String> token = (LinkedTreeMap<String, String>)json;

            LoginService.getVerification(
                    String.format("%s %s", token.get("token_type"), token.get("access_token")),
                    new Callback<Object>() {
                        @Override
                        public void success(Object json, Response response) {
                            LinkedTreeMap<String, Object> character = (LinkedTreeMap<String, Object>) json;

                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString(Constants.CHARACTER_ID, Double.toString((Double) character.get("CharacterID")));
                            editor.putString(Constants.CHARACTER_NAME, character.get("CharacterName").toString());
                            editor.apply();

                            setResult(RESULT_OK);
                            finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    });
        }

        @Override
        public void failure(RetrofitError error) {
            setResult(RESULT_CANCELED);
            finish();
        }
    };
}
