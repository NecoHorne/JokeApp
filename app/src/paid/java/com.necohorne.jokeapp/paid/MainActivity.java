package com.necohorne.jokeapp.paid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.necohorne.jokeandroidlibrary.JokeActivity;
import com.necohorne.jokeapp.MainIdlingResource;
import com.necohorne.jokeapp.R;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String JOKE = "joke";
    public Button tellJokeButton;
    private ProgressBar mProgressBar;
    private MainIdlingResource mIdlingResource;
    private static String mJoke;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new MainIdlingResource();
        }
        return mIdlingResource;
    }

    @VisibleForTesting
    @NonNull
    public static String getJoke(){
        return mJoke;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = findViewById(R.id.progressBar);
        getIdlingResource();

        tellJokeButton = findViewById(R.id.tellJokeButton);
        tellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //My joke library retrieves data via an API on the network so it needs to be run in a separate thread or AsyncTask or app will give an error.
                new EndpointsAsyncTask().execute();
                mProgressBar.setVisibility(View.VISIBLE);

            }
        });
    }

    public void launchActivityIntent(String joke){
        //this method sends the joke to the android library to display the joke.
        Intent intent = new Intent(this, JokeActivity.class);
        intent.putExtra(JOKE, joke);
        startActivity(intent);
    }

    class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
        private MyApi myApiService = null;

        @Override
        protected String doInBackground(Void... voids) {
            if(myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });

                myApiService = builder.build();
            }
            try {
                return myApiService.jokes().execute().getData();
            } catch (IOException e) {
                return  "";
            }
        }

        @Override
        protected void onPostExecute(String joke) {
            //Toast.makeText(MainActivity.this, joke, Toast.LENGTH_LONG).show();
            mJoke = joke;
            mIdlingResource.setIdleState(true);
            mProgressBar.setVisibility(View.GONE);
            launchActivityIntent(joke);
            super.onPostExecute(joke);
        }
    }
}
