package csuchico.smartnap;

import android.app.AlarmManager;
import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by caleb on 10/12/17.
 */

public class AlarmClock extends SugarRecord<AlarmClock> {

  long m_alarmTime;
  String m_alarmName;

  FlashCard m_flashCard;

  // Note: Please retain default constructor
  public AlarmClock() {
  }

  // Constructor for alarm with single card
  public AlarmClock(long time, String name, FlashCard card) {
    Log.i("AlarmClock","Constructed alarm: " + name + " for time " + time + ".");
    this.m_alarmTime = time;
    this.m_alarmName = name;
    this.m_flashCard = card;

    m_alarmTime = time;
    m_alarmName = name;
  }
  public long getTime(){
    return m_alarmTime;
  }

  public String getName(){
    return this.m_alarmName;
  }
}
