package com.newradiolite.jawhrafmlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class noAdsActivity extends AppCompatActivity {
    public static final String FIRST_COLOR_MODE = "FIRST_COLOR_MODE";
    public static final String SOLDE_NOW = "Solde";
    public static String THEME_NOW = "THEME_NOW";
    public static String theme_selected = "0";
    public String ch = "";
    public int mSolde = 0;
    public int firstTimeOpened = 1;
    private SharedPreferences mPreferences;
    ConstraintLayout layout_no_Ads;
    TextView Solde_From_No_Ads, Solde_Text, Description_No_ADS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_ads);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        mPreferences = getSharedPreferences(this.getString(R.string.sharedpreference), MODE_PRIVATE);
        checkSetting();
        Toast.makeText(getBaseContext(), "Afterno Ads",
                Toast.LENGTH_LONG).show();

    }

    public void checkSetting() {
        ch = mPreferences.getString(FIRST_COLOR_MODE, "colorWhiteMode");
        if (ch.equals("colorWhiteMode")) {
            theme_selected = "0";
            layout_no_Ads.setBackgroundResource(R.color.colorWhiteMode);
            Solde_Text.setTextColor(Color.BLACK);
            Solde_From_No_Ads.setTextColor(Color.parseColor("#D81B60"));
        } else {
            theme_selected = "1";
            layout_no_Ads.setBackgroundResource(R.color.colorDarkMode);
            Solde_Text.setTextColor(Color.parseColor("#FFFFFF"));
            Solde_From_No_Ads.setTextColor(Color.parseColor("#299FBE"));
        }
    }
}
