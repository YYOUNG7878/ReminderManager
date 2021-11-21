package edu.qc.seclass.glm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.UUID;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UUID id = (UUID) intent.getSerializableExtra("reminder_id");

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(id);
        notificationHelper.getManager().notify(1, nb.build());
    }
}
