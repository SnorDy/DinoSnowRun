package com.example.dinosnowrun;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_DinoSnowRun);
        super.onCreate(savedInstanceState);
        setContentView(new GameView(this));
        //скрываем крышечку с навзанием приложенпия
        getSupportActionBar().hide();


//        setContentView(R.layout.activity_main);
    }
}