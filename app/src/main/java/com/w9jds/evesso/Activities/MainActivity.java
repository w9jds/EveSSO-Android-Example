package com.w9jds.evesso.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.w9jds.evesso.Classes.Constants;
import com.w9jds.evesso.R;


public class MainActivity extends Activity implements View.OnClickListener {

    private final int LOGIN = 1;

    private ImageButton mLogin;
    private TextView mWelcomeMessage;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWelcomeMessage = (TextView)findViewById(R.id.welcome_message);
        mLogin = (ImageButton)findViewById(R.id.sso_button);
        mLogin.setOnClickListener(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sso_button:
                Intent iLogin = new Intent(this, SignInActivity.class);
                startActivityForResult(iLogin, LOGIN);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LOGIN:
                    mLogin.setVisibility(View.GONE);
                    mWelcomeMessage.setText(String.format("Welcome %s!", mSharedPreferences.getString(Constants.CHARACTER_NAME, "")));
                    break;
            }
        }
    }
}
