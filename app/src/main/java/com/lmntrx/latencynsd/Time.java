package com.lmntrx.latencynsd;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


class Time extends AsyncTask<String,String,String> {

    private static final String TIME_SERVER = "time-a.nist.gov";

    @Override
    protected String doInBackground(String... params) {
        String converted="Error";

        NTPUDPClient timeClient = new NTPUDPClient();
        String TAG = "Time Class";
        try {
            InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
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
        return converted;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
