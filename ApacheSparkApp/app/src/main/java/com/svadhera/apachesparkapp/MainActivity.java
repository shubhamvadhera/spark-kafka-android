package com.svadhera.apachesparkapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    protected static String restURL;
    protected static String restGetURL;
    //static final String defaultUrl = "http://cmpe-277-shubhamvadhera.c9users.io:8080/sparkrest";
    EditText eTUrl;
    SensorManager mSensorManager;
    private Sensor mLight;
    TextView tvSensorData;
    TextView tvResult;
    boolean streamingOn = false;
    String sparkResult = "Results not received yet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eTUrl = (EditText) findViewById(R.id.eTUrl);

        Button btnGo = (Button) findViewById(R.id.btnGo);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Stream started", Toast.LENGTH_SHORT).show();
                saveURL();
            }
        });

        Button btnPause = (Button) findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streamingOn = false;
                Toast.makeText(getBaseContext(), "Streaming paused", Toast.LENGTH_SHORT).show();
            }
        });

        tvSensorData = (TextView) findViewById(R.id.tvSensorData);
        tvResult = (TextView) findViewById(R.id.tvResult);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        boolean sensorExists = mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_FASTEST);
        if (!sensorExists) tvSensorData.setText("Sensor not supported!");
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        String data = Float.toString(event.values[0]);
        tvSensorData.setText(data);
        if (streamingOn) {
            //new HTTPPostAsyncTask().execute(System.currentTimeMillis() + " : Android : " + data);
            //new HTTPGetAsyncTask().execute();
            new HTTPPostAsyncTask().execute(data);
            new HTTPGetAsyncTask().execute();
            //new HTTPGetAsyncTask().execute();
            //new HTTPGetAsyncTask().execute();
        }
        tvResult.setText(sparkResult);
    }

    private void saveURL () {
        if (eTUrl.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter a URL and try again: ", Toast.LENGTH_SHORT).show();
            //restURL = defaultUrl;
            return;
        } else {
            restURL = eTUrl.getText().toString() + "/postSomething";
            restGetURL = eTUrl.getText().toString() + "/getSomething";
        }
        runProcess();
    }

    private void runProcess () {
        streamingOn = true;
    }

    class HTTPPostAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Sending Data to network");
        }

        protected String doInBackground(String... params) {
            System.out.println(HttpHelper.postData(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("Network POST complete");
        }
    }

    class HTTPGetAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Receiving Data from network");
        }

        protected String doInBackground(String... params) {
            String x = HttpHelper.getData();
            if (!x.equals("error")) sparkResult = x;
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("Network GET complete");
        }
    }
}
