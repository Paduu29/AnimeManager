package com.padunime.animemanager;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;


import java.io.IOException;


public class SettingsActivity extends AppCompatActivity implements AnimeAdapter.OnItemClickListener{

    private static final String option_1 = "Settings 1";
    private static final String option_2 = "Settings 2";
    private static final String option_3 = "Settings 3";
    private static final String option_4 = "Settings 4";
    private static final String option_5 = "Settings 5";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_layout);

        final Button option_11 = findViewById(R.id.option_1);
        final Button option_22 = findViewById(R.id.option_2);
        final Button option_33 = findViewById(R.id.option_3);
        final Button option_44 = findViewById(R.id.option_4);
        final Button option_55 = findViewById(R.id.option_5);
        option_11.setText(option_1);
        option_22.setText(option_2);
        option_33.setText(option_3);
        option_44.setText(option_4);
        option_55.setText(option_5);
        AdView mAdView = findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void onItemClick(int position) {

    }
}
