package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.common.base.Strings;
import com.kk.androidjokes.JokeActivity;


public class MainActivity extends AppCompatActivity implements EndpointAsyncTask.OnTaskCompleted {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressBar mProgressBar;
    private Button mTellJokeButton;
    private String mJoke = "";
    private InterstitialAd mInterstitialAd;

    private void showTellJokeButton() {
        mProgressBar.setVisibility(View.GONE);
        mTellJokeButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewsById();
        setUpAds();
    }

    private void findViewsById() {
        mProgressBar = findViewById(R.id.progress_bar);
        mTellJokeButton = findViewById(R.id.tell_joke_button);
    }

    private void setUpAds() {
        MobileAds.initialize(this,
                getString(R.string.init_ad_unit_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                loadNewAd();
                showJoke();
            }
        });
        loadNewAd();
    }

    private void showJoke() {
        if (Strings.isNullOrEmpty(mJoke)) {
            Toast.makeText(MainActivity.this, "Has not downloaded a joke yet...", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(JokeActivity.newIntent(MainActivity.this, mJoke));
        }
    }

    private void loadNewAd() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
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
        showInterstitialAd();
    }

    private void showInterstitialAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, "The interstitial wasn't loaded yet.");
        }
    }

    @Override
    public void onTaskCompleted(String joke) {
        mJoke = joke;
        showTellJokeButton();
    }

}
