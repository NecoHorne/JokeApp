package com.necohorne.jokeapp.free;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.necohorne.jokeandroidlibrary.JokeActivity;
import com.necohorne.jokeapp.R;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String JOKE = "joke";
    public Button tellJokeButton;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
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
        setupAds();
        mProgressBar = findViewById(R.id.progressBar);
        getIdlingResource();

        tellJokeButton = findViewById(R.id.tellJokeButton);
        tellJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //My joke library retrieves data via an API on the network so it needs to be run in a separate thread or AsyncTask or app will give an error.
//                new EndpointsAsyncTask().execute();
                mProgressBar.setVisibility(View.VISIBLE);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

            }
        });
    }

    public void setupAds(){
        //admob setup - I chose to go with the firebase setup as its quick and easy.
        MobileAds.initialize(this, "ca-app-pub-8837476093017718~5245371529");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                new EndpointsAsyncTask().execute();
                super.onAdClosed();
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
                return "";
            }
        }

        @Override
        protected void onPostExecute(String joke) {
            //Toast.makeText(MainActivity.this, joke, Toast.LENGTH_LONG).show();
            mJoke = joke;
            mProgressBar.setVisibility(View.GONE);
            mIdlingResource.setIdleState(true);
            launchActivityIntent(joke);
            super.onPostExecute(joke);
        }
    }
}
