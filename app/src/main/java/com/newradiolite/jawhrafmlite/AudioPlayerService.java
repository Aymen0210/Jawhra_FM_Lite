package com.newradiolite.jawhrafmlite;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.newradiolite.jawhrafmlite.C.MEDIA_SESSION_TAG;
import static com.newradiolite.jawhrafmlite.C.PLAYBACK_CHANNEL_ID;
import static com.newradiolite.jawhrafmlite.C.PLAYBACK_NOTIFICATION_ID;
import static com.newradiolite.jawhrafmlite.Samples.SAMPLES;


public class AudioPlayerService extends Service {

    public static SimpleExoPlayer player;
    public static ConcatenatingMediaSource concatenatingMediaSource;
    public static CacheDataSourceFactory cacheDataSourceFactory;
    public static DefaultDataSourceFactory dataSourceFactory;
    public static PlayerNotificationManager playerNotificationManager;
    public static MediaSessionCompat mediaSession;
    public static MediaSessionConnector mediaSessionConnector;
    public static MediaSource mediaSource;
    public static Boolean etatplayer = false;
    public static int numberrad1;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        super.onCreate();
        final Context context = this;

        initializePlayer(context);
        player.setPlayWhenReady(true);
        etatplayer = true;
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context,
                PLAYBACK_CHANNEL_ID,
                R.string.app_name,
                PLAYBACK_NOTIFICATION_ID,
                new MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return SAMPLES[player.getCurrentWindowIndex()].title;
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return SAMPLES[player.getCurrentWindowIndex()].description;
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, BitmapCallback callback) {
                        return Samples.getBitmap(
                                context, SAMPLES[player.getCurrentWindowIndex()].bitmapResource);
                    }
                }
        );
        playerNotificationManager.setNotificationListener(new NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
                createCurrentContentIntent(player);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
                // ((MainActivity) getBaseContext()).finish();
            }

        });
        playerNotificationManager.setPlayer(player);
        playerNotificationManager.setOngoing(false);
        playerNotificationManager.setUseChronometer(true);
        playerNotificationManager.setSmallIcon(R.drawable.exo_icon_play);
        playerNotificationManager.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);

        playerNotificationManager.setUseNavigationActions(false);
        playerNotificationManager.setFastForwardIncrementMs(0);
        playerNotificationManager.setRewindIncrementMs(0);
        mediaSession = new MediaSessionCompat(context, MEDIA_SESSION_TAG);
        mediaSession.setActive(true);
        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());
        mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
            @Override
            public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
                return Samples.getMediaDescription(context, SAMPLES[windowIndex]);
            }
        });
        mediaSessionConnector.setPlayer(player, null);
    }

    public static void initializePlayer(Context context) {

        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        dataSourceFactory = new DefaultDataSourceFactory(
                context, Util.getUserAgent(context, context.getString(R.string.app_name)));
        cacheDataSourceFactory = new CacheDataSourceFactory(
                DownloadUtil.getCache(context),
                dataSourceFactory,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (Samples.Sample sample : SAMPLES) {
            mediaSource = new ExtractorMediaSource.Factory(cacheDataSourceFactory)
                    .createMediaSource(sample.uri);
            concatenatingMediaSource.addMediaSource(mediaSource);

            numberrad1 = SAMPLES[player.getCurrentWindowIndex()].numberrad;
        }
        player.prepare(concatenatingMediaSource);
    }

    @Override
    public void onDestroy() {
        mediaSession.release();
        etatplayer = false;
        mediaSessionConnector.setPlayer(null, null);
        playerNotificationManager.setPlayer(null);
        player.release();
        player = null;
        super.onDestroy();
    }

    public PendingIntent createCurrentContentIntent(Player player) {
        Intent intent = new Intent(AudioPlayerService.this, MainActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (AudioPlayerService.this, 0, intent, 0);
        return contentPendingIntent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
