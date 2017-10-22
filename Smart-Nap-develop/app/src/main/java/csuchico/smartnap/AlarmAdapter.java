package csuchico.smartnap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jguil on 10/18/2017.
 */


    public class AlarmAdapter extends ArrayAdapter<AlarmClock> {
        public AlarmAdapter(Activity context, ArrayList<AlarmClock> alarms){

            super(context, 0, alarms);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItemView = convertView;
            if(listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(
                        R.layout.l_item, parent, false);

            }

            AlarmClock currentAlarm = getItem(position);
            TextView alarmTextView = (TextView) listItemView.findViewById(R.id.alarm_text_view);

            alarmTextView.setText(Long.toString((currentAlarm.getTime())));

            TextView defaultTextView = (TextView) listItemView.findViewById(R.id.index_text_view);

            defaultTextView.setText(currentAlarm.getName());
            return listItemView;
        }
    }

