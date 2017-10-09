package in.androize.himanshu.locationapp;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by himanshu on 7/10/17.
 */

public class MyService extends Service {



    private static final String TAG = "codeo";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0;





    private String lat;
    private String token;
    private String longitude;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);


            Log.d("codeo",mLastLocation.getLatitude()+" ,"+mLastLocation );
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            lat= String.valueOf(location.getLatitude());
            longitude= String.valueOf(location.getLongitude());
            sendData();
            mLastLocation.set(location);

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
            if(provider.equals(LocationManager.GPS_PROVIDER )){
                initializeLocationManager();

            }
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
            initializeLocationManager();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };



    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d("Testing", "Service got created");
        Toast.makeText(this, "ServiceClass.onCreate()", Toast.LENGTH_LONG).show();

        SharedPreferences prefs = getSharedPreferences(
                "myapp", Context.MODE_PRIVATE);

        token = prefs.getString("token",null);

        Log.d("codeo",token+ "kmdk");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
        initializeLocationManager();

//        try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                    mLocationListeners[1]);
//
//
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//        }
//        try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                    mLocationListeners[0]);
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//        }

    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        Log.e(TAG, "onDestroy");
        super.onDestroy();

        destroyLocationListner();
    }

    public void destroyLocationListner(){
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Toast.makeText(this, "ServiceClass.onStart()", Toast.LENGTH_LONG).show();
        Log.d("Testing", "Service got started");


        //TODO Code that runs repeatedly




    }



    private void initializeLocationManager() {

        Log.d("codeo","init loc man");


            if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                //invoke location from phone


                try {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            mLocationListeners[1]);


                } catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "network provider does not exist, " + ex.getMessage());
                }
                try {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            mLocationListeners[0]);
                } catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                }

            }else {


                Log.d("codeo","call handler");


                final Handler handler = new Handler();
                final int delay = 1000*10; //milliseconds

                handler.postDelayed(new Runnable(){
                    public void run(){
                        //do something
                        handler.postDelayed(this, delay);
                        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                            initializeLocationManager();
                        }else {
                            Log.d("codeo","call google api");
                            callGoogleApi();
                        }

                        Log.d("codeo","vjldfnvljdfnbl");
                    }
                }, delay);





//                TODO build object of google api and handle response
            }
    }

    private void sendData(){

        JsonObject object=new JsonObject();

            object.addProperty("entity","Location");
            object.addProperty("action","Create");
            object.addProperty("lat",lat);
            object.addProperty("lng",longitude);

        Log.d("codeo",object.toString());


        try{

            Ion.with(this)
                    .load("http://web.demoplatform.simplifii.com/api/v1/cards")
                    .setHeader("Authorization","bearer "+token)
                    .setJsonObjectBody(object)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            Log.d("codeo",result);
                        }
                    });

            Toast.makeText(this,"result called",Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            Toast.makeText(this, "result exceptiom", Toast.LENGTH_SHORT).show();
            Log.d("codeo", "error");
        }
    }

    private void sendData(String lati,String lng){


        JsonObject object=new JsonObject();

        object.addProperty("entity","Location");
        object.addProperty("action","Create");
        object.addProperty("lat",lati);
        object.addProperty("lng",lng);

        Log.d("codeo",object.toString());


        try{
            // String urlstring="10.1.1.1:8090/login.xml";
            Ion.with(this)
                    .load("http://web.demoplatform.simplifii.com/api/v1/cards")
                    .setHeader("Authorization","bearer "+token)
                    .setJsonObjectBody(object)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            Log.d("codeo",result);
                        }
                    });

            Toast.makeText(this,"result called",Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            Toast.makeText(this, "result exceptiom", Toast.LENGTH_SHORT).show();
            Log.d("codeo", "error");
        }
    }



    //call google api to get location
    public void callGoogleApi() {
        Log.d("codeo","get data called");
        int mcc = 0, mnc = 0, cid = 0 , lac = 0;

        final TelephonyManager telephony = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            final GsmCellLocation location = (GsmCellLocation) telephony.getCellLocation();
            if (location != null) {
                cid = location.getCid();
                lac = location.getLac();

                Log.d("codeo", location.getCid() + "   " + location.getLac());

            }
            String networkOperator = telephony.getNetworkOperator();

            if (networkOperator != null) {
                mcc = Integer.parseInt(networkOperator.substring(0, 3));
                mnc = Integer.parseInt(networkOperator.substring(3));
            }
            Log.d("codeo", mcc + "  " + mnc);


        }
        JsonObject object = new JsonObject();
        JsonObject jsonArrayObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();


        jsonArrayObject.addProperty("cellId",cid);
        jsonArrayObject.addProperty("locationAreaCode",lac);
        jsonArrayObject.addProperty("mobileCountryCode",mcc);
        jsonArrayObject.addProperty("mobileNetworkCode",mnc);

        jsonArray.add(jsonArrayObject);

        object.add("cellTowers", jsonArray);
        Log.d("codeo",jsonArrayObject.toString());


        try{
            Ion.with(this)
                    .load("https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyBnF1iPIWboObGjMSXcLgZ9sPXsB5HPFHk")
                    .setJsonObjectBody(object)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            Log.d("codeo",result);
                            JSONObject obj= null;
                            try {
                                obj = new JSONObject(result);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            try {
                                JSONObject location= obj.getJSONObject("location");
                                String latit=location.getString("lat");
                                String langi=location.getString("lng");

                                Log.d("codeo",latit+"  "+langi);

                                sendData(latit,langi);

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }
                    });

            Toast.makeText(this,"result called",Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            Toast.makeText(this, "result exceptiom", Toast.LENGTH_SHORT).show();
            Log.d("codeo", "error");
        }


    }





}
