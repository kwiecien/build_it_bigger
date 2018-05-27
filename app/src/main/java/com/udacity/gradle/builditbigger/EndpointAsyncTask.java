package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

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
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String joke) {
        listener.onTaskCompleted(joke);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(String joke);
    }

}