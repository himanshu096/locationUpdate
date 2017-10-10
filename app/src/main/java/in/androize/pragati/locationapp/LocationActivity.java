package in.androize.pragati.locationapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import in.androize.pragati.locationapp.R;

public class LocationActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_CODE = 855;
    private static final int  PERMISSION_CODE = 8785;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

//        Calendar cal = Calendar.getInstance();
//        Intent intent = new Intent(this, MyService.class);
//        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30 * 1000, pintent);

        CircularImageView imageView = (CircularImageView) findViewById(R.id.image_view);

//        showSettingsAlert("NETWORK");



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!Settings.canDrawOverlays(this)) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_CODE);



            } else {

                if (isStoragePermissionsAllowed(this)) {
                    startService();
                } else {
                    requestStoragePermissions(this, LOCATION_PERMISSION_CODE);
                    startService(new Intent(this, MyService.class));

                }
            }


        } else {
            startService();
        }

//        startService();

        Picasso.with(this).load("http://chat.connectinn.tk/128x128/83119d/fff/Vatsal.png&text=VS").into(imageView);
    }


    public void showSettingsAlert(String provider) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);

        alertDialog.setTitle(provider + " SETTINGS");

        alertDialog
                .setMessage(provider + " is not enabled! Want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }


    public void startService(){
        Intent i = new Intent(this , MyService.class);
        startService(i);
    }

    public static void requestStoragePermissions(Activity activity, int PERMISSION_REQUEST_CODE) {
        java.lang.String[] perms = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION","android.permission.READ_PHONE_STATE"};
        ActivityCompat.requestPermissions(activity, perms, PERMISSION_REQUEST_CODE);
    }

    public static boolean isStoragePermissionsAllowed(Activity activity) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return activity.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED
                    &&
                    activity.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED
            &&
                    activity.checkSelfPermission("android.permission.READ_PHONE_STATE") == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }



    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_CODE) {
            if (Settings.canDrawOverlays(this)) {
                if (isStoragePermissionsAllowed(this)) {
                    startService();
                } else {
                    requestStoragePermissions(this, LOCATION_PERMISSION_CODE);
                    startService(new Intent(this, MyService.class));
                }

            }

        }
    }

}
