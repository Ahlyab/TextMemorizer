package com.example.cbmtesting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);

//        // Set the title for the Toolbar
//        getSupportActionBar().setTitle(R.string.app_name);

        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    private void setStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

}