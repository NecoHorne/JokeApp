package com.necohorne.javajokes.Jokes;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class JokeUtils {

    //random joke api on the internet that will feed the app jokes.
    public final static String JOKEAPIURL = "https://api.icndb.com/jokes/random";
    public final static String JSON_TYPE = "type";
    public final static String JSON_VALUE = "value";
    public final static String JSON_ID = "id";
    public final static String JSON_JOKE = "joke";
    public final static String JSON_CAT = "categories";
    public final static String JSON_SUCCESS = "success";

    private static final String TAG = JokeUtils.class.getSimpleName();

    public static URL recipeUrl() {
        URI builtUri = URI.create(JOKEAPIURL);

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl() throws IOException {
        URL url = recipeUrl();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String getJokeJSON(String joke){

        JSONObject jokeJson = new JSONObject(joke);

        if (jokeJson.optString(JSON_TYPE).equals(JSON_SUCCESS)){
            JSONObject value = jokeJson.optJSONObject(JSON_VALUE);
            return value.optString(JSON_JOKE);
        }
        return "Please again!";
    }

}
