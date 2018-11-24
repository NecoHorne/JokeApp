package com.necohorne.jokeapp.paid;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.necohorne.jokeandroidlibrary.JokeActivity;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

import static com.necohorne.jokeapp.paid.MainActivity.JOKE;

public class EndpointAsyncTask extends AsyncTask<Void, Void, String>{
        private MyApi myApiService = null;
        private Context mContext;

    public EndpointAsyncTask(Context context) {
        this.mContext = context;
    }

    public void launchActivityIntent(String joke){
        //this method sends the joke to the android library to display the joke.
        Intent intent = new Intent(mContext, JokeActivity.class);
        intent.putExtra(JOKE, joke);
        mContext.startActivity(intent);
    }

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
            launchActivityIntent(joke);
            super.onPostExecute(joke);
        }
}
