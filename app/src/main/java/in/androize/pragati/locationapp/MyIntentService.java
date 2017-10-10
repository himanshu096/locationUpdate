package in.androize.pragati.locationapp;

import android.app.IntentService;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kushagra on 10/10/17.
 */

public class MyIntentService extends IntentService {

    public MyIntentService(){
        super("service");

    }

    private LocationManager mLocationManager = null;


    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("codeo","service intent invoked");

    }
}
