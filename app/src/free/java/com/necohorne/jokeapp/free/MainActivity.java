package com.necohorne.jokeapp.free;

import android.os.Bundle;
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
import com.necohorne.jokeapp.R;

public class MainActivity extends AppCompatActivity {

    public static final String JOKE = "joke";
    public Button tellJokeButton;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupAds();
        mProgressBar = findViewById(R.id.progressBar);
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
                new EndpointAsyncTask (getApplicationContext()).execute();
                super.onAdClosed();
            }
        });
    }

}
