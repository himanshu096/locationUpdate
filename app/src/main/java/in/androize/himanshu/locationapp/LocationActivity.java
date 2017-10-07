package in.androize.himanshu.locationapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        String timeinput = String.valueOf(1000 * 60 *  10);

//        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, MyService.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                Long.parseLong(timeinput),
//                Long.parseLong(timeinput), alarmIntent);

        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, MyService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);

        CircularImageView imageView=(CircularImageView)findViewById(R.id.image_view);

        Picasso.with(this).load("http://chat.connectinn.tk/128x128/83119d/fff/Vatsal.png&text=VS").into(imageView);
    }
}