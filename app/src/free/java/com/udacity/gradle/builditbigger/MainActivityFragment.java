package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import io.github.ilya_lebedev.displayjoke.JokeActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements MainActivity.JokeLoadStartListener, MainActivity.JokeLoadFinishListener {

    private View mContentContainer;
    private ProgressBar mProgressBar;

    private InterstitialAd mInterstitialAd;

    private boolean mIsJokeLoaded = false;
    private boolean mIsAdFailedToLoad = false;

    private String mJoke;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mContentContainer = root.findViewById(R.id.content_container);
        mProgressBar = root.findViewById(R.id.progress_bar);

        AdView mAdView = root.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                if (mIsJokeLoaded) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                mIsAdFailedToLoad = true;
                if (mIsJokeLoaded) {
                    startJokeActivity();
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
                startJokeActivity();
            }
        });

        return root;
    }

    @Override
    public void onJokeLoadStart() {
        mContentContainer.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onJokeLoadFinished(String joke) {
        mIsJokeLoaded = true;
        mJoke = joke;
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else if (mIsAdFailedToLoad) {
            startJokeActivity();
        }
    }

    private void startJokeActivity() {
        Intent startJokeActivityIntent = JokeActivity.generateIntent(getContext(), mJoke);
        startActivity(startJokeActivityIntent);
        mContentContainer.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

}
