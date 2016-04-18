package com.adtoapp.android.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appintop.common.AdProvider;
import com.appintop.common.TargetingParam;
import com.appintop.init.AdToApp;
import com.appintop.interstitialads.InterstitialListener;
import com.appintop.interstitialads.rewarded.RewardedAdValues;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "AdToAppSDKDemo";
    static final String ADTOAPP_SDK_KEY =
            "b6e76a3b-f943-422d-aad3-88cf29b1bc59:f1501a50-9524-4814-bdb3-1496b115d72f";

    private TextView sdkStateTextView;
    private TextView coinsTextView;
    private Button showRewardedAdButton;

    private int coins = 0;

    private InterstitialListener adtoappInterstitialListener = new InterstitialListener() {
        @Override
        public void onFirstInterstitialLoad(String adType, String provider) {
            Log.d(TAG, "onFirstInterstitialLoad");

            RewardedAdValues rewardedSettings = AdToApp.getRewardedAdValues();
            String rewardedName = rewardedSettings.getName();
            String rewardedValue = rewardedSettings.getValue();

            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onFirstInterstitialLoad %s %s, rewarded name: %s, value: %s", adType, provider, rewardedName, rewardedValue),
                    Toast.LENGTH_SHORT);
            toast.show();
            sdkStateTextView.setText("SDK is ready");
            showRewardedAdButton.setEnabled(true);
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
        }

        @Override
        public boolean onInterstitialFailedToShow(String adType) {
            Log.d(TAG, "onInterstitialFailedToShow");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialFailedToShow %s", adType),
                    Toast.LENGTH_SHORT);
            toast.show();

            return false;
        }

        @Override
        public void onRewardedCompleted(String adProvider, String currencyName, String currencyValue) {
            Log.d(TAG, "onRewardedCompleted");
            Toast toast = Toast.makeText(MainActivity.this, String.format("onRewardedCompleted %s %s %s", adProvider, currencyName, currencyValue), Toast.LENGTH_SHORT);
            toast.show();

            coins++;
            refreshCoins();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AdToApp.setTestMode(true);
        //AdToApp.disableAdNetwork(AdToApp.MASK_REWARDED, AdProvider.ADCOLONY, AdProvider.UNITYADS);
        //AdToApp.setTargetingParam(TargetingParam.USER_GENDER, TargetingParam.USER_GENDER_MALE);
        //AdToApp.setTargetingParam(TargetingParam.USER_AGE, "18");
        AdToApp.setLogging(true);
        AdToApp.initializeSDK(this, ADTOAPP_SDK_KEY, AdToApp.MASK_REWARDED);

        AdToApp.setInterstitialListener(adtoappInterstitialListener);

        setContentView(R.layout.activity_main);

        sdkStateTextView = (TextView)findViewById(R.id.sdk_state);
        coinsTextView = (TextView)findViewById(R.id.coins);

        showRewardedAdButton = (Button)findViewById(R.id.earn_coin);
        showRewardedAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdToApp.showInterstitialAd(AdToApp.REWARDED);
            }
        });
    }

    void refreshCoins() {
        coinsTextView.setText(coins + " coins");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdToApp.onResume(this);
    }

    @Override
    protected void onPause() {
        AdToApp.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AdToApp.onDestroy(this);
        super.onDestroy();
    }
}
