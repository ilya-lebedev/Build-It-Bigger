package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity {

    private JokeLoadStartListener mJokeLoadStartListener;
    private JokeLoadFinishListener mJokeLoadFinishListener;

    private EndpointsAsyncTask mEndpointsAsyncTask;

    public interface JokeLoadStartListener {
        void onJokeLoadStart();
    }

    public interface JokeLoadFinishListener {
        void onJokeLoadFinished(String joke);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivityFragment mainActivityFragment = new MainActivityFragment();

        try {
            mJokeLoadStartListener = mainActivityFragment;
        } catch (ClassCastException ex) {
            throw new ClassCastException(mainActivityFragment.toString()
                    + " must implement JokeLoadStartListener interface");
        }

        try {
            mJokeLoadFinishListener = mainActivityFragment;
        } catch (ClassCastException ex) {
            throw new ClassCastException(mainActivityFragment.toString()
                    + " must implement JokeLoadFinishListener interface");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container_fragment_main, mainActivityFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        if (mEndpointsAsyncTask != null) {
            mEndpointsAsyncTask.cancel(true);
            mEndpointsAsyncTask = null;
        }
        super.onDestroy();
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
        if (mEndpointsAsyncTask != null) {
            mEndpointsAsyncTask.cancel(true);
        }
        mEndpointsAsyncTask = new EndpointsAsyncTask(mJokeLoadFinishListener);
        mEndpointsAsyncTask.execute();
        mJokeLoadStartListener.onJokeLoadStart();
    }

    private static class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
        private MyApi myApiService = null;

        private WeakReference<JokeLoadFinishListener> jokeLoadFinishListenerWeakReference;

        EndpointsAsyncTask(JokeLoadFinishListener jokeLoadFinishListener) {
            this.jokeLoadFinishListenerWeakReference = new WeakReference<>(jokeLoadFinishListener);
        }

        @Override
        protected String doInBackground(Void... params) {
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(
                                    AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                    throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver
                myApiService = builder.build();
            }

            try {
                return myApiService.getJoke().execute().getJoke();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (jokeLoadFinishListenerWeakReference.get() != null) {
                jokeLoadFinishListenerWeakReference.get().onJokeLoadFinished(result);
            }
        }
    }

}
