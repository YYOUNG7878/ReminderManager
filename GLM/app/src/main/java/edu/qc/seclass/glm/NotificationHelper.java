package edu.qc.seclass.glm;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.UUID;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    private DataBaseManager mDBM;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        mDBM = DataBaseManager.get(base);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(UUID id) {
        Reminder r = mDBM.getReminder(id);
        String content = r.getName();
        r.setCheckoff(true);
        mDBM.updateCheckOff(r);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("Reminder Alarm!")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher_background);
    }
}
