package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.kk.androidjokes.JokeActivity;


public class MainActivity extends AppCompatActivity implements EndpointAsyncTask.OnTaskCompleted {

    private String mJoke = "";
    private ProgressBar mProgressBar;
    private Button mTellJokeButton;

    private void showTellJokeButton() {
        mProgressBar.setVisibility(View.GONE);
        mTellJokeButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsById();
    }

    private void findViewsById() {
        mProgressBar = findViewById(R.id.progress_bar);
        mTellJokeButton = findViewById(R.id.tell_joke_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressBar();
        new EndpointAsyncTask(this).execute();
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTellJokeButton.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        if (Strings.isNullOrEmpty(mJoke)) {
            Toast.makeText(this, "Has not downloaded a joke yet...", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(JokeActivity.newIntent(this, mJoke));
        }
    }

    @Override
    public void onTaskCompleted(String joke) {
        mJoke = joke;
        showTellJokeButton();
    }
}