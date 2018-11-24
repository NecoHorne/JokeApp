package com.necohorne.jokeandroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeActivity extends AppCompatActivity {

    //android library receives intent and intent string extra and displays a joke.

    public TextView jokeText;
    public static final String JOKE = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        jokeText = findViewById(R.id.joke_activity_tv);
        Intent sendIntent = getIntent();

        if(sendIntent.hasExtra(JOKE)){
            jokeText.setText(sendIntent.getStringExtra(JOKE));
        }
    }
}
