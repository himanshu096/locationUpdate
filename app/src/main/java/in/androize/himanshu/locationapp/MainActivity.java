package in.androize.himanshu.locationapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText name;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button login = (Button)findViewById(R.id.login_button);
         name = (EditText) findViewById(R.id.login_id);
         password = (EditText) findViewById(R.id.login_password);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String phone=name.getText().toString();
        String pas=password.getText().toString();

        try{
            Ion.with(this)
                    .load("http://web.demoplatform.simplifii.com/api/v1/admin/authenticate")
                    .setBodyParameter("mobile", phone)
                    .setBodyParameter("password", pas)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            Log.d("codeo",result);
                            try {
                                JSONObject obj=new JSONObject(result);
                               String token= String.valueOf(obj.get("token"));
                                Log.d("codeo",token);



                                SharedPreferences prefs = getSharedPreferences(
                                        "myapp", Context.MODE_PRIVATE);

                                prefs.edit().putString("token",token).apply();

                                Intent i=new Intent(MainActivity.this,LocationActivity.class);
                                startActivity(i);

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
