package com.adtoapp.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appintop.adbanner.BannerAdContainer;
import com.appintop.adbanner.BannerListener;
import com.appintop.init.AdToApp;
import com.appintop.interstitialads.DefaultInterstitialListener;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "AdToAppSDKDemo";
    static final String ADTOAPP_SDK_KEY =
            "b6e76a3b-f943-422d-aad3-88cf29b1bc59:f1501a50-9524-4814-bdb3-1496b115d72f";

    private TextView bannerStateTextView;
    private TextView interstitialStateTextView;
    private Button showInterstitialButton;
    private Button showAdOnNewActivityButton;
    private BannerAdContainer banner;

    private boolean showAdOnNewActivity = false;

    private DefaultInterstitialListener adtoappInterstitialListener = new DefaultInterstitialListener() {
        @Override
        public void onFirstInterstitialLoad(String adType, String provider) {
            Log.d(TAG, "onFirstInterstitialLoad");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onFirstInterstitialLoad %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
            interstitialStateTextView.setText("Interstitial is ready");
            showInterstitialButton.setEnabled(true);
            showAdOnNewActivityButton.setEnabled(true);
        }

        @Override
        public void onInterstitialStarted(String adType, String provider) {
            Log.d(TAG, "onInterstitialStarted");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialStarted %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onInterstitialClicked(String adType, String provider) {
            Log.d(TAG, "onInterstitialClicked");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialClicked %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onInterstitialClosed(String adType, String provider) {
            Log.d(TAG, "onInterstitialClosed");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialClosed %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();

            if (showAdOnNewActivity) {
                startNewActivity();
                showAdOnNewActivity = false;
            }
        }

        @Override
        public boolean onInterstitialFailedToShow(String adType) {
            Log.d(TAG, "onInterstitialFailedToShow");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialFailedToShow %s", adType),
                    Toast.LENGTH_SHORT);
            toast.show();

            if (showAdOnNewActivity) {
                startNewActivity();
                showAdOnNewActivity = false;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AdToApp.setLogging(true);
        AdToApp.initializeSDK(this, ADTOAPP_SDK_KEY,
                AdToApp.MASK_BANNER | AdToApp.MASK_INTERSTITIAL);

        AdToApp.setInterstitialListener(adtoappInterstitialListener);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bannerStateTextView = (TextView)findViewById(R.id.banner_state);
        bannerStateTextView.setText("Banner is loading...");

        interstitialStateTextView = (TextView)findViewById(R.id.interstitial_state);
        interstitialStateTextView.setText("Interstitial is loading...");

        showInterstitialButton = (Button)findViewById(R.id.button_show_interstitial);
        showInterstitialButton.setEnabled(false);
        showInterstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdToApp.showInterstitialAd();
            }
        });

        showAdOnNewActivityButton = (Button)findViewById(R.id.button_show_interstitial_on_new_activity);
        showAdOnNewActivityButton.setEnabled(false);
        showAdOnNewActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdOnNewActivity = true;
                AdToApp.showInterstitialAd();
            }
        });

        banner = (BannerAdContainer)findViewById(R.id.adtoapp_banner);
        banner.setBannerListener(new BannerListener() {

            @Override
            public void onBannerLoad() {
                Log.d(TAG, "onBannerLoad");
                Toast toast = Toast.makeText(MainActivity.this, "onBannerLoad",
                        Toast.LENGTH_SHORT);
                toast.show();
                bannerStateTextView.setText("Banner is ready");
            }

            @Override
            public void onBannerFailedToLoad() {
                Log.d(TAG, "onBannerFailedToLoad");
                Toast toast = Toast.makeText(MainActivity.this, "onBannerFailedToLoad",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onBannerClicked() {
                Log.d(TAG, "onBannerClicked");
                Toast toast = Toast.makeText(MainActivity.this, "onBannerClicked",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

        });
    }

    void startNewActivity() {
        Intent intent = new Intent(this, AnotherActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdToApp.setInterstitialListener(adtoappInterstitialListener);
        AdToApp.onResume(this);
        banner.resume();
    }

    @Override
    protected void onPause() {
        AdToApp.onPause(this);
        banner.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AdToApp.onDestroy(this);
        banner.destroy();
        super.onDestroy();
    }
}
