package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.common.base.Strings;
import com.kk.androidjokes.JokeActivity;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static String mJoke = "";
    private static ProgressBar mProgressBar;
    private static Button mTellJokeButton;
    private InterstitialAd mInterstitialAd;

    private static void showTellJokeButton() {
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
                "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
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
        new EndpointAsyncTask().execute();
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

    private static class EndpointAsyncTask extends AsyncTask<Void, Void, String> {

        private static MyApi sMyApiService = null;

        @Override
        protected String doInBackground(Void... params) {
            if (sMyApiService == null) {
                String localhostEmulatorIpAddress = "http://10.0.2.2:8080/_ah/api/";
                MyApi.Builder builder = new MyApi.Builder(
                        AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(),
                        null)
                        .setRootUrl(localhostEmulatorIpAddress)
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> request) {
                                request.setDisableGZipContent(true);
                            }
                        });
                sMyApiService = builder.build();
            }
            try {
                return sMyApiService.getJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String joke) {
            mJoke = joke;
            showTellJokeButton();
        }

    }


}
