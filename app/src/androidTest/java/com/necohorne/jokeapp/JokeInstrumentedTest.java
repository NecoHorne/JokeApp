package com.necohorne.jokeapp;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.necohorne.jokeapp.paid.EndpointAsyncTask;
import com.necohorne.jokeapp.paid.MainActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class JokeInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Test
    public void asyncTest() {
        Context context = mainActivityTestRule.getActivity().getApplicationContext();
        EndpointAsyncTask task = new EndpointAsyncTask(context);
        task.execute();
        String jokeFromLib = null;
        try {
            jokeFromLib = task.get(10, TimeUnit.SECONDS);
        } catch(ExecutionException e) {
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch(TimeoutException e) {
            e.printStackTrace();
        }
        Assert.assertNotNull(jokeFromLib);

    }
}
