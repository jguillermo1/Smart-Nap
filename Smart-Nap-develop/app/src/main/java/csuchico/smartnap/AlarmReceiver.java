package csuchico.smartnap;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d("AlarmReceiver", "onReceive has been called!");

        // Defining and creating a new ComponentName allows us to ensure that the proper
        // component is defined. In our case, this component is alarmService
        ComponentName alarmService = new ComponentName(
                context.getPackageName(),
                AlarmService.class.getName()
        );

        intent.setComponent(alarmService); // attach the component to our intent
        Log.i("AlarmReceiver", "Passing intent to startWakefulService");
        startWakefulService(context, intent); // startWakefulService using the intent
        setResultCode(Activity.RESULT_OK);
    }
}