package com.omniroid.currencyconverter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {


    LinearLayout mLayoutOne, mLayoutTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mLayoutOne = findViewById(R.id.ll_layout_theme_One);
        mLayoutTwo = findViewById(R.id.ll_layout_theme_Two);


        mLayoutOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOne = new Intent(SplashActivity.this,MainActivity.class);
                intentOne.putExtra("chooseTheme","One");
                startActivity(intentOne);
            }
        });

        mLayoutTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTwo = new Intent(SplashActivity.this,MainActivity.class);
                intentTwo.putExtra("chooseTheme","Two");
                startActivity(intentTwo);
            }
        });


    }
}
