package com.mukeshdev.hitexi.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.mukeshdev.hitexi.MainActivity;
import com.mukeshdev.hitexi.R;

public class SplashScreenActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        initSplash();

    }

    private void initSplash() {
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               /* Create an Intent that will start the Menu-Activity. */
               Intent mainIntent = new Intent(SplashScreenActivity.this, SignupActivity.class);
               startActivity(mainIntent);
               finish();
           }
       }, SPLASH_DISPLAY_LENGTH);
    }
}