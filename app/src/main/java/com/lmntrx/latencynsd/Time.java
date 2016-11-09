package com.lmntrx.latencynsd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

// IMPORT APACHE SERVER. THIS IS USED TO GET SERVER TIME
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


// THIS BACKGROUND TASK GETS A SERVER TIME FROM INTERNET AND USES IT FOR TIMER

class Time extends AsyncTask<String,String,String> {

    private Context context;
    private static final String TIME_SERVER = "time-a.nist.gov";
    ProgressDialog progressDialog;
    Time(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Initializing...");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String converted="Error";
        Log.d("Time","Fetching...");

        NTPUDPClient timeClient = new NTPUDPClient();
        String TAG = "Time Class";
        try {

            InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
            timeClient.setDefaultTimeout(5000);
            TimeInfo timeInfo = timeClient.getTime(inetAddress);


            long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();


            Date time = new Date(returnTime);
            Log.d(TAG, "Time from " + TIME_SERVER + ": " + time);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            try {
                Date mDate = sdf.parse(time.toString());
                long timeInMilliseconds = mDate.getTime();
                converted = timeInMilliseconds + "";
                Log.d(TAG, "Time Converted" + timeInMilliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        catch (IOException io)
        {
            Log.d(TAG,"IO Exception Caught");
        }
        Log.d("END CONVO",converted);
        Intent intent = new Intent();
        intent.setAction(context.getPackageName() + ".TIME");
        intent.putExtra("TIME",converted);
        context.sendBroadcast(intent);
        return converted;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.cancel();
        Log.d("Time","Done");
    }
}
