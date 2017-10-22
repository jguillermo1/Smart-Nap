package csuchico.smartnap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
import java.util.ListIterator;

// The following import statements would allow us to use simpler code below where only
// the FLAG_* is necessary versus the full location.
// Think about "using std::cout;" and then not having to import the entire <iostream>
// and being able to type "cout <<" and not have to use "std::cout <<" every time
/*
import static android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static android.view.WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
*/


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class AlarmDialog extends AppCompatActivity {

  // Setup some private member definitions
  private Ringtone mAlarmTone;
  private View mContentView;
  private View mControlsView;
  private TextView m_cardQuestionText;
  private TextView m_cardAnswerText;
  private FlashCard m_FlashCard;

  /**
   * Whether or not the system UI should be auto-hidden after
   * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
   */
  private static final boolean AUTO_HIDE = true;

  /**
   * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
   * user interaction before hiding the system UI.
   */
  private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

  /**
   * Some older devices needs a small delay between UI widget updates
   * and a change of the status and navigation bar.
   */
  private static final int UI_ANIMATION_DELAY = 300;

  private final Handler mHideHandler = new Handler();
  private boolean mVisible;

  private final Runnable mHidePart2Runnable = new Runnable() {
    @SuppressLint("InlinedApi")
    @Override
    public void run() {
      // Delayed removal of status and navigation bar

      // Note that some of these constants are new as of API 16 (Jelly Bean)
      // and API 19 (KitKat). It is safe to use them, as they are inlined
      // at compile-time and do nothing on earlier devices.
      mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
              | View.SYSTEM_UI_FLAG_FULLSCREEN
              | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
              | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
              | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
              | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
  };

  private final Runnable mShowPart2Runnable = new Runnable() {
    @Override
    public void run() {
      // Delayed display of UI elements
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.show();
      }
      mControlsView.setVisibility(View.VISIBLE);
    }
  };



  private final Runnable mHideRunnable = new Runnable() {
    @Override
    public void run() {
      hide();
    }
  };

  /**
   * Touch listener to use for in-layout UI controls to delay hiding the
   * system UI. This is to prevent the jarring behavior of controls going away
   * while interacting with activity UI.
   */
  private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
      if (AUTO_HIDE) {
        delayedHide(AUTO_HIDE_DELAY_MILLIS);
      }
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // First we need to setup some flags on the creation of this window so that we can
    // access it and see it appropriately even when our phone is asleep and/or locked

    // FLAG_KEEP_SCREEN_ON ensures alarm dialog is visible until silenced
    // FLAG_SHOW_WHEN_LOCKED ensured dialog is accessible when screen is locked by keyguard
    // FLAG_TURN_SCREEN_ON allows this activity to turn the screen on when its created
    this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

    setContentView(R.layout.activity_alarm_dialog); // window decoration is created

    mVisible = true;
    mControlsView = findViewById(R.id.fullscreen_content_controls);
    mContentView = findViewById(R.id.fullscreen_layout);

    // Set up the user interaction to manually show or hide the system UI.
    mContentView.setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                toggle();
              }
            });

    // Upon interacting with UI controls, delay any scheduled hide()
    // operations to prevent the jarring behavior of controls going away
    // while interacting with the UI.
    findViewById(R.id.button_silenceAlarm).setOnTouchListener(mDelayHideTouchListener);


    Intent alarmIntent = getIntent();
    Bundle alarmData = alarmIntent.getExtras();
    long alarmID = (long) alarmData.getInt("alarmID");

    AlarmClock alarm = AlarmClock.findById(AlarmClock.class, alarmID);
    FlashCard card = alarm.m_flashCard;

    String cardQuestion = card.m_question;
    String cardAnswer = card.m_answer;

    m_cardQuestionText = (TextView) findViewById(R.id.fc_question);
    m_cardAnswerText = (TextView) findViewById(R.id.fc_answer);

    m_cardQuestionText.setText(cardQuestion);
    m_cardAnswerText.setText(cardAnswer);

    playTone(); // play the ringtone
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    // Trigger the initial hide() shortly after the activity has been
    // created, to briefly hint to the user that UI controls
    // are available.
    delayedHide(100);
  }

  /**
   * @function    playTone()
   * @desc        Starts playing the users chosen ringtone for the specified alarm
   */
  private void playTone() {
    Log.i("AlarmDialog", "AlarmDialog initialized, playing tone for alarm");

    // first try to get the default alarm sound
    Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    if (alarmUri == null) {         // if default alarm sound isnt available
      // then get the default notification sound
      alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      if (alarmUri == null) {     // if default notification sound isnt available
        // then get the default ringtone sound
        alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
      }
    }

    mAlarmTone = RingtoneManager.getRingtone(this, alarmUri);

    // Using minSDKVersion 21 is not what we originally intended for this project, but we
    // may be able to setup some kind of filter system that uses an older deprecated method
    // to play the alarm tone regardless of phones ringtone volume. As of right now, the
    // AudioAttributes method requires a minimum SDK of 21. However, there may be an older
    // method that allows us to use setStreamMethod.
    // See the following thread:
    // https://stackoverflow.com/questions/15578812/troubles-play-sound-in-silent-mode-on-android

    Log.i("AlarmDialog","Setting up AudioAttributes to attach to our ringtone USAGE_ALARM flag");
    AudioAttributes alarmSound = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM) // play alarm even if phone silenced
            .build();
    mAlarmTone.setAudioAttributes(alarmSound);
    mAlarmTone.play();
  } // playTone()

  /**
   * @function    onSilenceAlarm()
   * @desc        Called when a user interacts with the Silence AlarmEdit button
   */
  public void onSilenceAlarm(View view) {
    Log.i("AlarmDialog", "User has chosen to silence alarm");
    mAlarmTone.stop();
    finish();
  }

  private void toggle() {
    if (mVisible) {
      hide();
    } else {
      show();
    }
  }

  private void hide() {
    // Hide UI first
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }
    mControlsView.setVisibility(View.GONE);
    mVisible = false;

    // Schedule a runnable to remove the status and navigation bar after a delay
    mHideHandler.removeCallbacks(mShowPart2Runnable);
    mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
  }

  @SuppressLint("InlinedApi")
  private void show() {
    // Show the system bar
    mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    mVisible = true;
    // Schedule a runnable to display UI elements after a delay
    mHideHandler.removeCallbacks(mHidePart2Runnable);
    mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
  }

  /**
   * Schedules a call to hide() in [delay] milliseconds, canceling any
   * previously scheduled calls.
   */
  private void delayedHide(int delayMillis) {
    mHideHandler.removeCallbacks(mHideRunnable);
    mHideHandler.postDelayed(mHideRunnable, delayMillis);
  }

  /**
   * set fcquestion TextView visibility to gone and set fcanswer TextView visibility to visible
   */
  public void to_answer(View view){
    TextView answer = (TextView) findViewById(R.id.fc_answer);
    TextView question = (TextView) findViewById(R.id.fc_question);
    question.setVisibility(View.GONE);
    answer.setVisibility(View.VISIBLE);
  }

  /**
   * set fcanswer TextView visibility to gone and set fcquestion TextView visibility to visible
   */
  public void to_question(View view){
    TextView answer = (TextView) findViewById(R.id.fc_answer);
    TextView question = (TextView) findViewById(R.id.fc_question);
    answer.setVisibility(View.GONE);
    question.setVisibility(View.VISIBLE);
  }
}
