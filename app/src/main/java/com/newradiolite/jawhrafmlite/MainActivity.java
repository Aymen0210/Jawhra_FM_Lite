package com.newradiolite.jawhrafmlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.onesignal.OneSignal;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private SimpleExoPlayer player;
    private BandwidthMeter bandwidthMeter;
    private ExtractorsFactory extractorsFactory;
    private TrackSelection.Factory trackSelectionFactory;
    private TrackSelector trackSelector;
    private DefaultBandwidthMeter defaultBandwidthMeter;
    private DataSource.Factory dataSourceFactory;
    private MediaSource mediaSource;
    Date currentTime;
    public static int firstTimeOpened = 1;

    Intent starintent, stopintent;
    String linkradionow = "http://streaming2.toutech.net:8000/jawharafm";
    String radioname = "Radio Jawhara";
    String radioname_ar = "جوهرة اف ام";
    Boolean stateplaying = false;
    Boolean Enpromo = false;
    Boolean AdsShowedNow = false;
    int adstype = 0;
    Boolean adsInterLoaded = false;
    Boolean adsRewLoaded = false;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;

    ImageView logo, image_play, image_reglage, image_info, image_no_ads, image_video_ads, image_exit, timer_sleep;
    TextView solde, promotio_text, timer_text_sleep;
    ConstraintLayout layout_main;

    public static final String FIRST_COLOR_MODE = "FIRST_COLOR_MODE";
    public static final String FIRST_TIME_OPENED = "FIRST_TIME_OPENED";
    public static final String SOLDE_NOW = "Solde";
    public static final String ADS_Enabled = "ADS_ENABLED";
    public static String THEME_NOW = "THEME_NOW";
    public static String theme_selected = "0";
    public String ch = "";
    public int mSolde = 0;
    private SharedPreferences mPreferences;
    private Boolean firstTime = true;
    boolean serviceRunningStatus;
    Boolean AdsEnable = true;
    Chronometer simpleChronometer;
    Boolean btnAdsClicked = false;

    private long backPressedTime = 0;
    int timeToClose_mn = 0;
    long timeToClose_ml_sc = 0;
    long timeToClose_sc = 0;
    Boolean timeToCloseOK = false;
    String url = "https://www.jawharafm.net/fr/live/56";
    public static TextView title_prog_now, date_prog_now, desc_prog_now, type_prog_now;
    Bitmap bitmap;
    Element el0;
    Elements el1;
    public static ImageView img_prog_now;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        title_prog_now = findViewById(R.id.title_prog_now);
        date_prog_now = findViewById(R.id.date_prog_now);
        desc_prog_now = findViewById(R.id.desc_prog_now);
        type_prog_now = findViewById(R.id.type_prog_now);

        img_prog_now = findViewById(R.id.img_prog_now);
        Content c = new Content(getBaseContext());
        c.execute();
        image_play = findViewById(R.id.btn_transition_time);
        image_no_ads = findViewById(R.id.no_ads);
        timer_sleep = findViewById(R.id.timer_sleep);
        solde = findViewById(R.id.solde);
        layout_main = findViewById(R.id.layout_main);
        logo = findViewById(R.id.logo);
        image_reglage = findViewById(R.id.btn_reglage);
        image_info = findViewById(R.id.btn_info);
        image_video_ads = findViewById(R.id.btn_video_ads);
        image_exit = findViewById(R.id.home);
        promotio_text = findViewById(R.id.promotio_text);
        timer_text_sleep = findViewById(R.id.timer_text_sleep);
        simpleChronometer = findViewById(R.id.timePlaying); // initiate a chronometer
        mAdView = findViewById(R.id.adView);
        layout_main = findViewById(R.id.layout_main);
        mPreferences = getSharedPreferences(this.getString(R.string.sharedpreference), MODE_PRIVATE);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        currentTime = Calendar.getInstance().getTime();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(150));
        Glide.with(this)
                .load(R.drawable.j1)
                .apply(requestOptions)
                .into(logo);

        testPermission();
        initializeAds();
        timer_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choixTimeToExit(getBaseContext(), v);
            }
        });
        image_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveTaskToBack(true);
            }
        });
        image_reglage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), Setting_Activity.class);
                intent.putExtra(THEME_NOW, theme_selected);
                startActivity(intent);
                showingAds();
            }
        });
        image_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), infoActivity.class);
                intent.putExtra(THEME_NOW, theme_selected);
                startActivity(intent);
                showingAds();
            }
        });
        image_video_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAdsClicked = true;
                image_video_ads.setVisibility(View.INVISIBLE);
                loadRewardedVideoAd();
            }
        });
        image_no_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final View customLayout = getLayoutInflater().inflate(R.layout.activity_no_ads, null);
                builder.setView(customLayout);
                builder.setPositiveButton(getResources().getString(R.string.main_oui), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data from the AlertDialog to the Activity
                        if (mSolde >= 100) {
                            mSolde = mSolde - 100;
                            image_no_ads.setVisibility(View.INVISIBLE);
                            mPreferences.edit().putInt(SOLDE_NOW, mSolde).apply();
                            solde.setText(String.valueOf(mSolde));
                            AdsEnable = false;
                            simpleChronometer.setBase(SystemClock.elapsedRealtime());
                            simpleChronometer.start();
                            simpleChronometer.setVisibility(View.VISIBLE);
                            DisableEnableBan1(false);
                            promotio_text.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.main_solde_insuffisant),
                                    Toast.LENGTH_LONG).show();
                            DisableEnableBan1(true);
                        }
                    }
                });

                builder.setNegativeButton(getResources().getString(R.string.main_non), null);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        simpleChronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

            @Override

            public void onChronometerTick(Chronometer chronometer) {
                if (simpleChronometer.getText().equals("59:59")) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.main_temps_promo_terminer),
                            Toast.LENGTH_LONG).show();

                    AdsEnable = true;
                    image_no_ads.setVisibility(View.VISIBLE);
                    simpleChronometer.stop();
                    simpleChronometer.setVisibility(View.INVISIBLE);
                    DisableEnableBan1(true);
                    promotio_text.setVisibility(View.INVISIBLE);
                } else {

                }
            }

        });

        image_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_pause_Methode();
            }
        });
        simpleChronometer = findViewById(R.id.timePlaying);
        checkSetting(1);
        testPermission();
        initializeAds();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                AdsShowedNow = false;
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdLoaded() {
                adsInterLoaded = true;
                if (AdsEnable) {
                    if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        mInterstitialAd.show();
                        Log.i("AdsTest", "Intersetial Loaded");
                    }
                }
            }

            @Override
            public void onAdOpened() {
                AdsShowedNow = true;
                adsInterLoaded = false;
                Log.i("AdsTest", "Intersetial showed");
            }
        });
        checkStreamingPlaying();
    }

    public static void setProgramNow(Bitmap bitmap, String title, String desc, String type, String date) {
        img_prog_now.setImageBitmap(bitmap);
        title_prog_now.setText(title);
        desc_prog_now.setText(desc);
        type_prog_now.setText(type);
        date_prog_now.setText(date);
    }

    public void TimeToExit(long ml_sc) {
        timeToCloseOK = true;
        new CountDownTimer(ml_sc, 1000) { //40000 milli seconds is total time, 1000 milli seconds is time interval

            public void onTick(long millisUntilFinished) {
                timeToClose_sc--;
                if (Locale.getDefault().getLanguage() == "ar") {
                    timer_text_sleep.setText("" + (timeToClose_sc / 60) + " دق");
                } else {
                    timer_text_sleep.setText("" + (timeToClose_sc / 60) + " mn");
                }
            }

            public void onFinish() {

                if (Build.VERSION.SDK_INT >= 21) {
                    if (stateplaying)
                        stopingservice();
                }
                finish();
            }
        }.start();

    }

    public void choixTimeToExit(Context c, View v) {
        PopupMenu popupMenu = new PopupMenu(c, v);
        Menu menu = popupMenu.getMenu();
        int t = 0;
        for (int i = 0; i < 24; i++) {
            t = t + 5;
            if (Locale.getDefault().getLanguage() == "ar") {
                menu.add("" + t + " دق ");
            } else {
                menu.add("" + t + " mn ");
            }

        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int dest;
                if (String.valueOf(item.getTitle().subSequence(4, 5)).equals("m")) {
                    dest = Integer.parseInt(String.valueOf(item.getTitle().subSequence(0, 3)));
                } else if (String.valueOf(item.getTitle().subSequence(3, 4)).equals("m")) {
                    dest = Integer.parseInt(String.valueOf(item.getTitle().subSequence(0, 2)));
                } else {
                    dest = Integer.parseInt(String.valueOf(item.getTitle().subSequence(0, 1)));
                }

                timeToClose_mn = dest;
                timeToClose_sc = dest * 60;
                timeToClose_ml_sc = (timeToClose_sc * 1000);
                TimeToExit(timeToClose_ml_sc);
                return true;
            }
        });
        popupMenu.show();
    }

    private void initializePlayer(String playnownew) {
        bandwidthMeter = new DefaultBandwidthMeter();
        extractorsFactory = new DefaultExtractorsFactory();
        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(trackSelectionFactory);
        defaultBandwidthMeter = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, radioname), defaultBandwidthMeter);
        mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(playnownew));
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        player.prepare(mediaSource);
    }

    public void startingservice() {
        starintent = new Intent(this, AudioPlayerService.class);
        Util.startForegroundService(this, starintent);
    }

    public void stopingservice() {
        stopintent = new Intent(this, AudioPlayerService.class);
        stopService(stopintent);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void testPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasAudioPermission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            int hasInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);
            List<String> permissions = new ArrayList<>();
            if (hasAudioPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
        }

    }

    public void play_pause_Methode() {
        if (stateplaying == false) {
            if (Build.VERSION.SDK_INT >= 21) {
                if (Locale.getDefault().getLanguage() == "ar") {
                    Samples.Sample.title = radioname_ar;
                } else {
                    Samples.Sample.title = radioname;
                }
                Samples.Sample.mediaId = radioname;
                Samples.Sample.uri = Uri.parse(linkradionow);
                startingservice();
            } else {
                // code pour version 4.4 or -
                initializePlayer(linkradionow);
                player.setPlayWhenReady(true);
            }
            if (theme_selected == "0")
                image_play.setImageResource(R.drawable.ic_stop_bl);
            else
                image_play.setImageResource(R.drawable.ic_stop_bl);
            stateplaying = true;
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                stopingservice();
            } else {
                // code pour version 4.4 or -
                player.stop();
                initializePlayer(linkradionow);
            }
            if (theme_selected == "0")
                image_play.setImageResource(R.drawable.ic_play_bl);
            else
                image_play.setImageResource(R.drawable.ic_play_bl);
            stateplaying = false;
            showingAds();
        }
    }

    public void initializeAds() {
        MobileAds.initialize(this, getResources().getString(R.string.app_admob_aps));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.app_admob_int1));

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        if (firstTime) {
            showingAds();
            firstTime = false;
        }
    }

    public static void setfirstTimeOpened(int s) {
        firstTimeOpened = s;

    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getResources().getString(R.string.app_admob_rew1),
                new AdRequest.Builder().build());
    }

    public void checkSetting(int from) {
        if (from == 1) {
            firstTimeOpened = mPreferences.getInt(FIRST_TIME_OPENED, 1);
            if (firstTimeOpened == 1) {
                Intent intent;
                intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
                //  mPreferences.edit().putInt(FIRST_TIME_OPENED, 0).apply();
                mPreferences.edit().putString(FIRST_COLOR_MODE, "colorWhiteMode").apply();
                mPreferences.edit().putInt(SOLDE_NOW, 50).apply();
                mSolde = 50;
            } else {
                mSolde = mPreferences.getInt(SOLDE_NOW, 50);
                AdsEnable = mPreferences.getBoolean(ADS_Enabled, true);
            }
        }
        if (AdsEnable)
            DisableEnableBan1(true);
        else
            DisableEnableBan1(false);
        solde.setText("" + mSolde);
        ch = mPreferences.getString(FIRST_COLOR_MODE, "colorWhiteMode");
        if (ch.equals("colorWhiteMode")) {
            theme_selected = "0";
            if (stateplaying == true) {
                image_play.setImageResource(R.drawable.ic_stop_bl);
            } else {
                image_play.setImageResource(R.drawable.ic_play_bl);
            }
            layout_main.setBackgroundResource(R.drawable.gradient2);
            image_reglage.setImageResource(R.drawable.ic_settings_bl);
            image_info.setImageResource(R.drawable.ic_info_bl);
            image_video_ads.setImageResource(R.drawable.gift);
            image_exit.setImageResource(R.drawable.ic_close_bl);
            timer_sleep.setImageResource(R.drawable.ic_timer_bl);
        } else {
            theme_selected = "1";
            if (stateplaying == true) {
                image_play.setImageResource(R.drawable.ic_stop_bl);
            } else {
                image_play.setImageResource(R.drawable.ic_play_bl);
            }
            layout_main.setBackgroundResource(R.color.colorDarkMode);
            image_reglage.setImageResource(R.drawable.ic_settings_bl);
            image_info.setImageResource(R.drawable.ic_info_bl);
            image_video_ads.setImageResource(R.drawable.gift);
            image_exit.setImageResource(R.drawable.ic_close_bl);
            timer_sleep.setImageResource(R.drawable.ic_timer_bl);
        }
    }


    public void checkStreamingPlaying() {
        if (Build.VERSION.SDK_INT >= 21) {
            serviceRunningStatus = isServiceRunning(AudioPlayerService.class);
            if (serviceRunningStatus) {
                if (AudioPlayerService.etatplayer == true) {
                    if (theme_selected == "0") {
                        image_play.setImageResource(R.drawable.ic_stop);
                    } else {
                        image_play.setImageResource(R.drawable.ic_stop_bl);
                    }
                    stateplaying = true;
                } else {
                    stopingservice();
                    stateplaying = false;
                }
            }
        } else {
            // code pour version 4.4 or -
            initializePlayer(linkradionow);

        }
    }

    public void DisableEnableBan1(Boolean c) {
        if (c) mAdView.setVisibility(View.VISIBLE);
        else
            mAdView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        mPreferences.edit().putInt(SOLDE_NOW, mSolde).apply();
        mPreferences.edit().putBoolean(ADS_Enabled, AdsEnable).apply();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        checkSetting(2);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        super.onResume();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        adsRewLoaded = true;
        btnAdsClicked = false;
        if (AdsEnable) {
            if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                mRewardedVideoAd.show();
                adsRewLoaded = false;
                Log.i("AdsTest", "Rewarded Loaded");
            }
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {
        AdsShowedNow = true;
        adsRewLoaded = false;
        image_video_ads.setVisibility(View.VISIBLE);
        Log.i("AdsTest", "Rewarded showed");

    }

    @Override
    public void onRewardedVideoAdClosed() {
        AdsShowedNow = false;
        image_video_ads.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        mSolde = mSolde + 50;
        mPreferences.edit().putInt(SOLDE_NOW, mSolde).apply();
        solde.setText(mSolde);
        image_video_ads.setVisibility(View.VISIBLE);
        Toast.makeText(getBaseContext(), getResources().getString(R.string.main_felicitation),
                Toast.LENGTH_LONG).show();
        AdsShowedNow = false;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        if (btnAdsClicked) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.main_pas_ads),
                    Toast.LENGTH_LONG).show();
        }
        image_video_ads.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRewardedVideoCompleted() {
        image_video_ads.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_pub_app);
        TextView titleApps = dialog.findViewById(R.id.titleAppsId);
        Button but1 = dialog.findViewById(R.id.exit);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();

            }
        });
        Button but2 = dialog.findViewById(R.id.rate);
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
                finish();
            }
        });
        Button but3 =  dialog.findViewById(R.id.tryItId);
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.newradiolite.radiotn"));
                startActivity(intent);

            }
        });

        dialog.show();
    }

    public void showingAds() {
        if (mInterstitialAd.isLoaded() == false) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            if (mRewardedVideoAd.isLoaded() == false) {
                loadRewardedVideoAd();
            } else {
                if (!AdsShowedNow) {
                    if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        mRewardedVideoAd.show();
                        AdsShowedNow = true;
                    }
                }
            }
        } else {
            if (!AdsShowedNow) {
                if (ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                    mInterstitialAd.show();
                    AdsShowedNow = true;
                }
            }
        }
    }
}