package com.necohorne.jokeapp.paid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.necohorne.jokeapp.R;

public class MainActivity extends AppCompatActivity {

    public static final String JOKE = "joke";
    public Button tellJokeButton;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);

        tellJokeButton = findViewById(R.id.tellJokeButton);
        tellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //My joke library retrieves data via an API on the network so it needs to be run in a separate thread or AsyncTask or app will give an error.
                new EndpointAsyncTask (getApplicationContext()).execute();
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }




}
