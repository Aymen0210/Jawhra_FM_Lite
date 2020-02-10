package com.newradiolite.jawhrafmlite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.DrawableRes;
import androidx.core.app.NotificationCompat;

public final class Samples {

    public static final class Sample {
        public static Uri uri;
        public static String mediaId;
        public static String title;
        public static String description;
        public static int bitmapResource;
        public static int numberrad;

        public Sample(
                String uri, String mediaId, String title, String description, int bitmapResource, int numberrad) {
            this.uri = Uri.parse(uri);
            this.mediaId = mediaId;
            this.title = title;
            this.description = description;
            this.bitmapResource = bitmapResource;
            this.numberrad = 0;
        }


        @Override
        public String toString() {
            return title;
        }
    }

    public static final Sample[] SAMPLES = new Sample[]{
            new Sample(
                    "http://streaming2.toutech.net:8000/jawharafm",
                    " ",
                    "Lecture en cours",
                    "أخبار تونس",
                    R.drawable.j1,
                    1),
    };


    public static MediaDescriptionCompat getMediaDescription(Context context, Sample sample) {
        Bundle extras = new Bundle();
        Bitmap bitmap = getBitmap(context, sample.bitmapResource);
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap);
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap);
        MediaDescriptionCompat.Builder m=new MediaDescriptionCompat.Builder();
        m.setMediaId(sample.mediaId)
                .setIconBitmap(bitmap)
                .setTitle(sample.title)
                .setDescription(sample.description)
                .setExtras(extras)
                .build();
        NotificationCompat.Builder ma=new NotificationCompat.Builder(context);
        ma.setOngoing(false);
        ma.setColor(Color.BLACK);
        ma.setColorized(true);
        ma.setSmallIcon(R.drawable.exo_notification_small_icon);
        ma.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        ma.setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
        return new MediaDescriptionCompat.Builder()
                .setMediaId(sample.mediaId)
                .setIconBitmap(bitmap)
                .setTitle(sample.title)
                .setDescription(sample.description)
                .setExtras(extras)
                .build();
    }

    public static Bitmap getBitmap(Context context, @DrawableRes int bitmapResource) {
        return ((BitmapDrawable) context.getResources().getDrawable(bitmapResource)).getBitmap();
    }


}
