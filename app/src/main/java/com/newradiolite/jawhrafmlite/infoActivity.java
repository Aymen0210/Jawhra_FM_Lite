package com.newradiolite.jawhrafmlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class infoActivity extends AppCompatActivity {

    ImageView retour, img_em, img_fb;
    TextView mversion, tx, tx1, tx2, tx3, tx4, tx5, tx6, tx7;
    ConstraintLayout layout_info;

    public static String theme_selected = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        mversion = findViewById(R.id.version_apps);
        retour = findViewById(R.id.retour_btn);
        layout_info = findViewById(R.id.layout_info);
        img_em = findViewById(R.id.img_em);
        img_fb = findViewById(R.id.img_fb);
        tx = findViewById(R.id.textView);
        tx1 = findViewById(R.id.textView1);
        tx2 = findViewById(R.id.textView2);
        tx3 = findViewById(R.id.textView3);
        tx4 = findViewById(R.id.textView4);
        tx5 = findViewById(R.id.textView5);
        tx6 = findViewById(R.id.textView6);
        tx7 = findViewById(R.id.textView7);

        final Intent intent = getIntent();
        theme_selected = intent.getStringExtra(MainActivity.THEME_NOW);
        if (theme_selected.equals("1")) {
            layout_info.setBackgroundResource(R.color.colorDarkMode);
            retour.setImageResource(R.drawable.ic_back_bl);
            tx.setTextColor(Color.parseColor("#FFFFFF"));
            tx1.setTextColor(Color.parseColor("#FFFFFF"));
            tx2.setTextColor(Color.parseColor("#FFFFFF"));
            tx3.setTextColor(Color.parseColor("#FFFFFF"));
            tx4.setTextColor(Color.parseColor("#FFFFFF"));
            tx5.setTextColor(Color.parseColor("#FFFFFF"));
            tx6.setTextColor(Color.parseColor("#FFFFFF"));
            tx7.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            layout_info.setBackgroundResource(R.drawable.gradient2);
            retour.setImageResource(R.drawable.ic_back_bl);
            tx.setTextColor(Color.BLACK);
            tx1.setTextColor(Color.BLACK);
            tx2.setTextColor(Color.BLACK);
            tx3.setTextColor(Color.BLACK);
            tx4.setTextColor(Color.BLACK);
            tx5.setTextColor(Color.BLACK);
            tx6.setTextColor(Color.BLACK);
            tx7.setTextColor(Color.BLACK);
        }
        String versionCode = "";
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mversion.setText(versionCode);
        final String finalVersionCode = versionCode;
        img_em.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","ab-aymen@live.fr", null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "From Jawhara FM Apps "+ finalVersionCode);
                intent.putExtra(Intent.EXTRA_TEXT, "Hi Developer");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
        img_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = getOpenFacebookIntent(getBaseContext());
                startActivity(facebookIntent);
            }
        });
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://fr-fr.facebook.com/aymen.belgouthi")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/arkverse")); //catches and opens a url to the desired page
        }
    }
}
