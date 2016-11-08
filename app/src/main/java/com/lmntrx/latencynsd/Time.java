package com.lmntrx.latencynsd;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by ACJLionsRoar on 11/2/16.
 */

class Time extends AsyncTask<String,String,String> {

    private static final String TIME_SERVER = "time-a.nist.gov";
    String printMessage="NO MESSAGE SET";
    private long timeInMilliseconds;
    private String returnTime;

    private Date time = new Date();

    @Override
    protected String doInBackground(String... params) {
        String converted="Error";

        NTPUDPClient timeClient = new NTPUDPClient();
        String TAG = "Time Class";
        try {
            InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
            TimeInfo timeInfo = timeClient.getTime(inetAddress);

            //long returnTime = timeInfo.getReturnTime();   //local device time
            long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();   //server time


            Date time = new Date(returnTime);
            Log.d(TAG, "Time from " + TIME_SERVER + ": " + time);

            String givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013";
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            try {
                Date mDate = sdf.parse(givenDateString);
                timeInMilliseconds = mDate.getTime();
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

//        TextView timeView = (TextView) find;
//
//        if(time==null)
//        {
//            timeView.setText("INVALID TIME RECEIVED");
//        }
//
//        else
//        {
//            timeView.setText("GOT SOMETHING!!!!!!!!!!");
//        }
    }
}
