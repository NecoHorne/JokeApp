package com.necohorne.javajokes.Jokes;

import java.io.IOException;

import static com.necohorne.javajokes.Jokes.JokeUtils.*;

public class Jokes {

    public static String getJoke() throws IOException {
        //this method will retrieve a random joke from The Internet Chuck Norris Database
        return getJokeJSON(getResponseFromHttpUrl());
    }

}
