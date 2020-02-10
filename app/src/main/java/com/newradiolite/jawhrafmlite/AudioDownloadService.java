package com.newradiolite.jawhrafmlite;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadManager.TaskState;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationUtil;

import static com.newradiolite.jawhrafmlite.C.DOWNLOAD_CHANNEL_ID;
import static com.newradiolite.jawhrafmlite.C.DOWNLOAD_NOTIFICATION_ID;

public class AudioDownloadService extends DownloadService {

  public AudioDownloadService() {
    super(
        DOWNLOAD_NOTIFICATION_ID,
        DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
        DOWNLOAD_CHANNEL_ID,
        R.string.app_name);
  }

  @Override
  protected DownloadManager getDownloadManager() {
    return DownloadUtil.getDownloadManager(this);
  }

  @Nullable
  @Override
  protected Scheduler getScheduler() {
    return null;
  }

  @Override
  protected Notification getForegroundNotification(TaskState[] taskStates) {
    return DownloadNotificationUtil.buildProgressNotification(
        this,
        R.mipmap.icon,
        DOWNLOAD_CHANNEL_ID,
        null,
        null,
        taskStates);
  }
}
