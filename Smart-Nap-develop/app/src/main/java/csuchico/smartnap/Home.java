package csuchico.smartnap;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.orm.SugarDb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    SugarDb smartNapDB = new SugarDb(getApplicationContext());

    //AlarmClock.findById(AlarmClock.class, (long) 1); // Perform this for each SugarRecord  model
    //FlashCard.findById(FlashCard.class, (long) 1); // Perform this for each SugarRecord  model

    //List of AlarmClocks stored in an ArrayList
    FlashCard flash = new FlashCard("a","a");
    ArrayList<AlarmClock> alarms = new ArrayList<AlarmClock>();
    alarms.add(new AlarmClock(11,"Five",flash));

    AlarmAdapter adapter = new AlarmAdapter(this, alarms);

    // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
    // There should be a {@link ListView} with the view ID called list, which is declared in the
    // activity_numbers.xml layout file.
    ListView listView = (ListView) findViewById(R.id.list);

    // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
    // {@link ListView} will display list items for each word in the list of words.
    // Do this by calling the setAdapter method on the {@link ListView} object and pass in
    // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
    listView.setAdapter(adapter);
  }

  public void alarmSetup(View view) {
    Intent openAlarmPage = new Intent(this, AlarmEdit.class);
    startActivity(openAlarmPage);
  }

  public void GotoQuestion(View view){
    Intent openQuestionPage = new Intent(this, Question.class);
    startActivity(openQuestionPage);
  }
}