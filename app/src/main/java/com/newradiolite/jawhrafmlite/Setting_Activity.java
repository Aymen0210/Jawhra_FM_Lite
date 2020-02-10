package com.newradiolite.jawhrafmlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class Setting_Activity extends AppCompatActivity {

    Switch dark_test;
    ConstraintLayout layout_setting;
    ImageView retour;
    TextView text_themeSombre;

    public static final String FIRST_COLOR_MODE = "FIRST_COLOR_MODE";
    public static String THEME_NOW = "THEME_NOW";
    public static String theme_selected = "0";
    private SharedPreferences mPreferences;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_);
        dark_test = findViewById(R.id.dark_test);
        layout_setting = findViewById(R.id.layout_setting);
        retour = findViewById(R.id.retour_btn);
        text_themeSombre = findViewById(R.id.text_themeSombre);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        mPreferences = getSharedPreferences(this.getString(R.string.sharedpreference), MODE_PRIVATE);
        editor = mPreferences.edit();
        final Intent intent = getIntent();
        theme_selected = intent.getStringExtra(MainActivity.THEME_NOW);
        if (theme_selected.equals("0")) {
            dark_test.setChecked(false);
            layout_setting.setBackgroundResource(R.drawable.gradient2);
            text_themeSombre.setTextColor(Color.BLACK);
            dark_test.setTextColor(Color.BLACK);
            retour.setImageResource(R.drawable.ic_back_bl);
        } else {
            dark_test.setChecked(true);
            layout_setting.setBackgroundResource(R.color.colorDarkMode);
            text_themeSombre.setTextColor(Color.parseColor("#FFFFFF"));
            dark_test.setTextColor(Color.parseColor("#FFFFFF"));
            retour.setImageResource(R.drawable.ic_back_bl);
        }

        dark_test.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (dark_test.isChecked()) {
                    mPreferences.edit().putString(FIRST_COLOR_MODE, "colorDarkMode").apply();
                    theme_selected = "1";
                    editor.putInt("background_resource", R.color.colorDarkMode).apply();
                } else {
                    mPreferences.edit().putString(FIRST_COLOR_MODE, "colorWhiteMode").apply();
                    theme_selected = "0";
                    editor.putInt("background_resource", R.color.colorWhiteMode).apply();
                }
                changetheme();
                MainActivity.theme_selected = theme_selected;
            }
        });

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void changetheme() {
        if (theme_selected == "0") {
            layout_setting.setBackgroundResource(R.drawable.gradient2);
            text_themeSombre.setTextColor(Color.BLACK);
            dark_test.setTextColor(Color.BLACK);
            retour.setImageResource(R.drawable.ic_back_bl);
        } else {
            layout_setting.setBackgroundResource(R.color.colorDarkMode);
            text_themeSombre.setTextColor(Color.parseColor("#FFFFFF"));
            dark_test.setTextColor(Color.parseColor("#FFFFFF"));
            retour.setImageResource(R.drawable.ic_back_bl);
        }
    }
}
