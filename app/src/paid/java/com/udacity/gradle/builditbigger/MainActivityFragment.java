package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import io.github.ilya_lebedev.displayjoke.JokeActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements MainActivity.JokeLoadStartListener, MainActivity.JokeLoadFinishListener {

    private View mContentContainer;
    private ProgressBar mProgressBar;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mContentContainer = root.findViewById(R.id.content_container);
        mProgressBar = root.findViewById(R.id.progress_bar);

        return root;
    }

    @Override
    public void onJokeLoadStart() {
        mContentContainer.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onJokeLoadFinished(String joke) {
        Intent startJokeActivityIntent = JokeActivity.generateIntent(getContext(), joke);
        startActivity(startJokeActivityIntent);
        mContentContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

}
