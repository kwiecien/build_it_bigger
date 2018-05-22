package com.kk.androidjokes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class JokeActivity extends AppCompatActivity {

    private static final String JOKE_KEY = "com.kk.androidjokes.joke_key";

    public static Intent newIntent(Context context, String joke) {
        Intent intent = new Intent(context, JokeActivity.class);
        intent.putExtra(JOKE_KEY, joke);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
    }

}
