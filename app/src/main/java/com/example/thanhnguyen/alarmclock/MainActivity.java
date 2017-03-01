package com.example.thanhnguyen.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    TimePicker alarmTimePicker;
    TextView updateStatus;
    Context context;
    PendingIntent pendingIntent;
    String hourString;
    String minString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);

        updateStatus = (TextView) findViewById(R.id.updateStatus);

        final Calendar calendar = Calendar.getInstance();

        Button alarmOn = (Button) findViewById(R.id.alarmOn);

        final Intent alarmIntend = new Intent(this.context,AlarmReceiver.class);

        alarmOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int hourAlarm =  alarmTimePicker.getHour();
                int minAlarm = alarmTimePicker.getMinute();

                calendar.set(Calendar.HOUR_OF_DAY,hourAlarm);
                calendar.set(Calendar.MINUTE, minAlarm);

                if (hourAlarm > 12) {
                    hourString = String.valueOf(hourAlarm - 12);
                }

                if (minAlarm < 10) {
                    minString = "0" + String.valueOf(minAlarm);
                }

                setAlarmText("Alarm set to " + hourString + ":" + minString);

                alarmIntend.putExtra("extra", "alarm on");

                pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntend, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
            }
        });

        Button alarmOff = (Button) findViewById(R.id.alarmOff);
        alarmOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlarmText("Alarm off!");

                alarmIntend.putExtra("extra", "alarm off");

                alarmManager.cancel(pendingIntent);
                sendBroadcast(alarmIntend);
            }
        });


    }

    public void setAlarmText(String text){
        updateStatus.setText(text);
    }
}
