package csuchico.smartnap;

import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class AlarmService extends IntentService {

    private NotificationManager alarmNotificationManager;
    private AlertDialog alarmDialog;

    public AlarmService() {
        super("AlarmService");
    }

    static final int ALARM_SILENCE_REQUEST = 1;
    @Override
    public void onHandleIntent(Intent intent) {


        // Any code to setup the service must be here
        // before we call completeWakefulIntent(intent)
        startDialogActivity(intent);

        // completeWakefulIntent() releases the CPU wake lock set by the receiver
        // This ensures that onHandleIntent() runs its code above while holding the CPU awake
        Log.i("AlarmService", "AlarmService calling completeWakefulIntent on AlarmReceiver");
        AlarmReceiver.completeWakefulIntent(intent);
    }

    /**
     * private void startDialogActivity()
     *
     *  @desc:  Launches the activity for the alarm dialog to allow users to silence the alarm.
     *
     */
    private void startDialogActivity(Intent intent) {
        Intent dialogIntent = new Intent(this, AlarmDialog.class);
        dialogIntent.putExtras(intent.getExtras());
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }
}
