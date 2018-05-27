package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;

public class EndpointAsyncTask extends AsyncTask<Void, Void, String> {

    private static MyApi sMyApiService = null;
    private OnTaskCompleted listener;

    public EndpointAsyncTask(OnTaskCompleted onTaskCompleted) {
        listener = onTaskCompleted;
    }

    @Override
    @Nullable
    protected String doInBackground(Void... params) {
        if (sMyApiService == null) {
            String localhostEmulatorIpAddress = BuildConfig.URL;
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
            Log.e(EndpointAsyncTask.class.getSimpleName(), e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(@Nullable String joke) {
        listener.onTaskCompleted(joke != null ? joke : "Couldn't fetch the joke");
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String joke);
    }

}