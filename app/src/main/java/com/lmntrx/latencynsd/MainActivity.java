package com.lmntrx.latencynsd;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    int SocketServerPORT;  // Port should be fetched dynamically in real systems.// NSD Manager and service registration code
    private String SERVICE_NAME = "NSD";
    private String SERVICE_TYPE = "_http._tcp.";
    private NsdManager mNsdManager;
    ServerSocket serverSocket;

    NsdServiceInfo resutNsdServiceInfo;
    int discoveredPORT;
    InetAddress discoveredHost;

    InetAddress handshakeIP;

    Timer countDownTimer;

    boolean serviceHosting = false;
    boolean serviceDiscovering = false;
    Runnable updater;

    long serverTime,currentTime,finalTimer=0;
    Handler handler;
    TextView display;
    String ans;

    int okToContinueClient=0;
    int okToDisplayHandshake=0;
    int okToContinueHost=0;

    long testStartTime;
    long testFinishTime;
    long returnedFinishTime;
    long latency;

     int sendTestPORT;
    InetAddress sendTestHost;

    int sendReplyPORT;
    InetAddress sendReplyHost;

    ServerSocket localserverSocket;

    Timer timer2;



    Handler loadingHandler;
    Runnable loadingRunnable;
    int loadingcount;





    private static final String TAG = "MainActivity";

    NsdManager.RegistrationListener mRegistrationListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.ResolveListener mResolveListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button hostBtn= (Button) findViewById(R.id.hostBTN);
        final  Button discoverBtn= (Button) findViewById(R.id.discoverBTN);
        final LinearLayout buttonsLL = (LinearLayout) findViewById(R.id.ButtonsLinear);
        final TextView handshakeTV= (TextView) findViewById(R.id.handshakeTxtView);
        final  TextView handshakeDots= (TextView) findViewById(R.id.handshakeDots);
        final TextView header= (TextView) findViewById(R.id.header);


//    try
//    {
//        initializeServerSocket();
//    }
//    catch (IOException e)
//    {
//
//    }





        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);




    //--------------------------------TIMER FUNCTIONS---------------------------------------------------------

    //         ASYNC TASK CALL v

        try{
            ans = new Time().execute().get();
            Log.d("Test",ans);
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        catch (ExecutionException ignored) {

        }
    // ASYNC CALL ENDS ^

        serverTime=Long.parseLong(ans);
        currentTime=serverTime;

        handler = new Handler();

        final int[] seconds = {0};
        //display = (TextView) findViewById(R.id.TimeTV);
        updater = new Runnable() {
            @Override
            public void run() {
                seconds[0] += 50;
                currentTime=serverTime+seconds[0];
                finalTimer=currentTime-serverTime;
                //display.setText("t: " + finalTimer);

            }
        };

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(updater);
            }
        };
        countDownTimer = new Timer();
        countDownTimer.schedule(timerTask,50,50);

        //TextView sTime = (TextView) findViewById(R.id.serverTime);
        //final TextView cTime = (TextView) findViewById(R.id.currentTime);

//        sTime.setText("Server Time: "+serverTime);
//
//        currentTimeBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                cTime.setText("Current Timer:"+finalTimer);
//
//            }
//        });



        //----------------------------------------------------------------------------------------------------




        hostBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                // EDAAAA  EEE GONE appo thanne work aavilla. HANDSHAKE KAZHINJITTE UI UPDATE AAVANOLLU :/  IVIDE ENTHU GONE/VISIBLE AAKIYALUM
//
//                Log.d(TAG, "REQUIRED TRY BLOCK 1");
//
//
//                Socket socket = serverSocket.accept();
//                // loadingTimer.cancel();
//
//
//
//                Log.d(TAG, "REQUIRED TRY BLOCK 2");


  //                thaazhe kidakkane ee code execute aayitte ee visibility update aavu :/  KOPP  EVERYTHING WORKS IN LOG
                //
                buttonsLL.setVisibility(View.GONE);
                handshakeTV.setVisibility(View.VISIBLE);
                handshakeDots.setVisibility(View.VISIBLE);

             //   try {
//
//
//
//
//
//
//
//                    loadingHandler = new Handler();
//
//                    loadingcount=1;
//
//                    loadingRunnable = new Runnable() {
//                        @Override
//                        public void run() {
//
//                            switch (loadingcount)
//                            {
//                                case 1: handshakeDots.setText(".."); loadingcount=2;
//                                    break;
//                                case 2: handshakeDots.setText("..."); loadingcount=3;
//                                    break;
//                                case 3: handshakeDots.setText("...."); loadingcount=4;
//                                    break;
//                                case 4: handshakeDots.setText("."); loadingcount=1;
//                                    break;
//                            }
//
//                        }
//                    };
//
//                    TimerTask loadingtimerTask = new TimerTask() {
//                        @Override
//                        public void run() {
//                            loadingHandler.post(loadingRunnable);
//                        }
//                    };
//                    final Timer loadingTimer = new Timer();
//                    loadingTimer.schedule(loadingtimerTask,500,500);
//
//






                    serviceDiscovering = false;
                    serviceHosting = true;





                    Log.d(TAG, "REGISTERED PORT 1= " + SocketServerPORT);

                    //----WHILE

//
//                        timer2 = new Timer();
//                        timer2.scheduleAtFixedRate(new TimerTask() {
//                            @Override
//                            public void run() {
//




//                                TextView debug= (TextView) findViewById(R.id.debug);
//
//                                debug.setText("Handshake IP:"+handshakeIP);









//                            }
//                        }, 1000, 1000);

                // INGANE CHEYUMBO UI HANGS.

                new HostTask().execute();

                handshakeTV.setText(" ");

                // asynctask execute aayi kazhinjittu oru String Variable il Host IP of 2nd phone kittanam. aa host IP vechittanu 1st fone gonna send a data to 2nd phone and test latency







                    //-------------------WHILE ENDED ^^




            }
        });

        discoverBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Entered Onclick","Entered Onclick");

                buttonsLL.setVisibility(View.GONE);
                new  DiscoverTask().execute();



            }
        });







    }


    //ithanu receiver
    //IP and Port kittumbo async task sends out a broadcast
    //adh eppozhayalum ivide receive cheytholum
    //receive cheyyumbo intentil ninne data textViewsil disp cheyyum
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView header= (TextView) findViewById(R.id.header);
            header.setText("Hosting Successful!");

            TextView port = (TextView) findViewById(R.id.handshakeDots);
            TextView host = (TextView) findViewById(R.id.handshakeTxtView);

            port.setText("Connected Port:"+intent.getStringExtra("PORT"));
            host.setText("Discovered Device IP:"+intent.getStringExtra("IP"));

//            ((TextView)findViewById(R.id.handshakeDots)).setText("Connected Port:"+intent.getStringExtra("PORT"));
//            ((TextView)findViewById(R.id.handshakeTxtView)).setText("Discovered Device IP:"+intent.getStringExtra("IP"));

            TextView yourdevice= (TextView) findViewById(R.id.handshakeInitial);
            yourdevice.setVisibility(View.VISIBLE);
            yourdevice.setText("Your Device:"+getIpAddress());




            Button test = (Button) findViewById(R.id.testLatency);
            test.setVisibility(View.VISIBLE);



            test.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {




                    new sendTestData().execute();


                }
            });




        }
    };

    BroadcastReceiver receiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            TextView yourdevice= (TextView) findViewById(R.id.handshakeInitial);
            TextView handshakeOnDiscovery = (TextView) findViewById(R.id.handshakeTxtView);
            TextView handshakePortOnDiscovery= (TextView) findViewById(R.id.handshakeDots);
            handshakeOnDiscovery.setVisibility(View.VISIBLE);
            handshakeOnDiscovery.setText("Hosted Device IP:"+intent.getStringExtra("HOST"));

            handshakePortOnDiscovery.setVisibility(View.VISIBLE);
            handshakePortOnDiscovery.setText("Connected Port:"+intent.getStringExtra("PORT"));

            yourdevice.setVisibility(View.VISIBLE);
            yourdevice.setText("Your Device:"+getIpAddress());

            TextView header = (TextView) findViewById(R.id.header);
            header.setText("Discovery Successful!");

            TextView waiting = (TextView) findViewById(R.id.waitingOnDiscovery);
            waiting.setVisibility(View.VISIBLE);

            sendReplyPORT=Integer.parseInt(intent.getStringExtra("PORT"));

            try
            {
                sendReplyHost = InetAddress.getByName(intent.getStringExtra("IP"));;
            } catch(UnknownHostException e)
            {

            }

            new sendTestReply().execute();



        }
    };



    BroadcastReceiver finalReceiverHostDevice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Button testLatency = (Button) findViewById(R.id.testLatency);

            testLatency.setVisibility(View.GONE);

            TextView latencyTxtVw = (TextView) findViewById(R.id.latencyDisplay);

            latencyTxtVw.setVisibility(View.VISIBLE);

            long validate = Long.parseLong(intent.getStringExtra("LATENCY"));

            if(validate==0)
                latencyTxtVw.setText("Latency less than 50ms");

            else
            latencyTxtVw.setText("Latency="+intent.getStringExtra("LATENCY")+" ms");



        }
    };

    BroadcastReceiver finalReceiverClientDevice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            TextView updateWaiting = (TextView) findViewById(R.id.waitingOnDiscovery);

            updateWaiting.setText("Latency Test Completed!");



        }
    };





    private class HostTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            Thread handshake= new Thread() {

                @Override
                public void run() {
                    //------------------------------


                    try {

                        initializeServerSocket();
                        registerService(SocketServerPORT);
                        Intent intent = new Intent();
                        intent.setAction(getPackageName());
                        //header.setText("Hosted on Port:"+SocketServerPORT);




                        Log.d(TAG, "REQUIRED TRY BLOCK 1");


                        Socket socket = serverSocket.accept();
                        // loadingTimer.cancel();



                        Log.d(TAG, "REQUIRED TRY BLOCK 2");


                        DataInputStream dataInputStream = new DataInputStream(
                                socket.getInputStream());

                        Log.d(TAG, "REQUIRED TRY BLOCK 3");
                        DataOutputStream dataOutputStream = new DataOutputStream(
                                socket.getOutputStream());

                        Log.d(TAG, "REQUIRED TRY BLOCK 4");


                        String messageFromClient;

                        Log.d(TAG,"BEFORE READING FROM INPUT STREAM");

                        //If no message sent from client, this code will block the program
                        messageFromClient = dataInputStream.readUTF();
                        Log.d(TAG,"AFTER READING FROM INPUT STREAM");


                        Log.d(TAG,"messagefromClient:: "+messageFromClient);





                        Log.d(TAG, "MESSAGE::::" + messageFromClient);

                        handshakeIP=InetAddress.getByName(messageFromClient);

                        sendTestPORT=SocketServerPORT;

                        sendTestHost=handshakeIP;


                        intent.putExtra("PORT",SocketServerPORT+"");
                        intent.putExtra("IP",handshakeIP+"");
                        sendBroadcast(intent);





                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //-------------------------
                }
            };



            handshake.start();

//            try
//            {
//                handshake.join();
//
//            }catch (InterruptedException ignored)
//            {
//
//            }
            return handshakeIP + "";
        }
    }


    private class DiscoverTask extends AsyncTask<Void,Void,NsdServiceInfo>{
        @Override
        protected NsdServiceInfo doInBackground(Void... params) {
            Thread discover= new Thread() {

                @Override
                public void run() {
                    //------------------------------

                    serviceHosting=false;
                    serviceDiscovering=true;

                    initializeDiscoveryListener();
                    mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);



                    //-------------------------
                }
            };

            discover.start();
//            try{
//                discover.join();
//            }
//           catch (InterruptedException e)
//           {
//
//           }

            // --------------------------------------------------ITHU NOKK----------------------------------------------------



            // ee return cheyyunna value null aa.  discover.join() should have executed this return after thread is completed.
            // but angane ittitum  work aayilla.
            // this return is executed before the thread is completed and discovery is resolved. atha null varane.
            // ee return cheyyunna value il resolve cheyyunna method il initialise cheytha value kitticha baaki work aayikolum
            // automatically.
            return  resutNsdServiceInfo;


            // this return is passed to onPostExecute which is on the main UI thread.


        }

        @Override
        protected void onPostExecute(NsdServiceInfo resultNsdServiceInfo) {
            super.onPostExecute(resultNsdServiceInfo);

            TextView handshakeOnDiscovery = (TextView) findViewById(R.id.handshakeTxtView);
            TextView header = (TextView) findViewById(R.id.header);
            header.setText("Discover");
            handshakeOnDiscovery.setVisibility(View.VISIBLE);
            handshakeOnDiscovery.setText("Searching...");


        }
    }

    private class sendTestData extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            Thread discover= new Thread() {

                @Override
                public void run() {
                    //------------------------------

                    try {
                        Socket socket = new Socket(sendTestHost,sendTestPORT);
                        DataInputStream dataInputStream = new DataInputStream(
                                socket.getInputStream());
                        DataOutputStream dataOutputStream = new DataOutputStream(
                                socket.getOutputStream());

                        testStartTime=currentTime;
                        String testData=testStartTime+"";


                        dataOutputStream.writeUTF(testData);

                        String gotReply=dataInputStream.readUTF();

                        returnedFinishTime=Long.parseLong(gotReply);


                        Log.d(TAG,"Test Data Send:"+testData);
                        Log.d(TAG,"Got Reply:"+gotReply);
                        Log.d(TAG,"Converted Reply:"+returnedFinishTime);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }




                    latency= returnedFinishTime-testStartTime;
                    if(latency<0)
                        latency=0;

                    Intent intent = new Intent();
                    intent.setAction(getPackageName() + ".LATENCY");

                    intent.putExtra("LATENCY",latency+"");
                    sendBroadcast(intent);




                    //-------------------------
                }
            };

            discover.start();
//
            return  "hi";



        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }

    private class sendTestReply extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            Thread discover= new Thread() {

                @Override
                public void run() {
                    //------------------------------

                    try {
                        localserverSocket = new ServerSocket(sendReplyPORT);
                        Socket socket = localserverSocket.accept();
                        testFinishTime=currentTime;

                        Log.d("TAG","Test Finish Time:"+testFinishTime);

                        DataOutputStream dataOutputStream = new DataOutputStream(
                                socket.getOutputStream());

                        String replyData=testFinishTime+"";

                        dataOutputStream.writeUTF(replyData);



                    }catch (IOException e)
                    {

                    }

                    Intent intent = new Intent();
                    intent.setAction(getPackageName() + ".REPLY");

                    intent.putExtra("REPLY","SUCCESS");
                    sendBroadcast(intent);


                    //-------------------------
                }
            };

            discover.start();
//
            return  "hi";



        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

        }
    }









    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unregisterReceiver(receiver2);
        unregisterReceiver(finalReceiverClientDevice);
        unregisterReceiver(finalReceiverHostDevice);
       if(serviceHosting)
       {
           mNsdManager.unregisterService(mRegistrationListener);
           Log.d(TAG,"Paused Hosting Correctly");
       }

        else if(serviceDiscovering)
       {
           mNsdManager.stopServiceDiscovery(mDiscoveryListener);
           Log.d(TAG,"Paused Discovering Correctly");
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(getPackageName());
        registerReceiver(receiver,intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(getPackageName() + ".HOST");
        registerReceiver(receiver2,intentFilter2);
        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction(getPackageName() + ".LATENCY");
        registerReceiver(finalReceiverHostDevice,intentFilter3);
        IntentFilter intentFilter4 = new IntentFilter();
        intentFilter4.addAction(getPackageName() + ".REPLY");
        registerReceiver(finalReceiverClientDevice,intentFilter4);
        if (serviceHosting)
        {

            registerService(SocketServerPORT);
            Log.d(TAG,"Resumed Hosting Correctly");
        }

        else if (serviceDiscovering)
        {

            initializeDiscoveryListener();
            mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
            Log.d(TAG,"Resumed Discovering Correctly");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(serviceHosting)
        {
            mNsdManager.unregisterService(mRegistrationListener);
            Log.d(TAG,"Destroyed Hosting Correctly");
        }

        else if(serviceDiscovering)
        {
            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            Log.d(TAG,"Destroyed Discovering Correctly");
        }

        handler.removeCallbacks(updater);

        countDownTimer.cancel();

        super.onDestroy();
    }




    public void registerService(int port) {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);


        initializeRegistrationListener();
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    public void initializeServerSocket() throws IOException {
        // Initialize a server socket on the next available port.
        serverSocket = new ServerSocket(0);

        // Store the chosen port.
        SocketServerPORT =  serverSocket.getLocalPort();


    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
                String mServiceName = NsdServiceInfo.getServiceName();
                SERVICE_NAME = mServiceName;
                Toast.makeText(MainActivity.this, "Hosted Successfully.WAITING FOR OTHER DEVICE...",
                        Toast.LENGTH_LONG).show();



                Log.d("NsdserviceOnRegister", "Registered name : " + mServiceName);
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo,
                                             int errorCode) {

                Toast.makeText(MainActivity.this, "registration failed",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
                // NsdManager.unregisterService() called and passed in this listener.
                Log.d("NsdserviceOnUnregister",
                        "Service Unregistered : " + serviceInfo.getServiceName());
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo,
                                               int errorCode) {
                //Fail
            }
        };

    }

    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!  Do something with it.
                Log.d(TAG, "Service discovery success" + service);


                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.

                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(SERVICE_NAME)) {
                    // The name of the service tells the user what they'd be
                    // connecting to. It could be "Bob's Chat App".

                    Log.d(TAG, "Same machine: " + SERVICE_NAME);

                    initializeResolveListener();
                    mNsdManager.resolveService(service, mResolveListener);


                }

                else if (service.getServiceName().contains("NSD")){

                    Log.d(TAG, "ENTERED RESOLVING IF CONDITION");
                    initializeResolveListener();
                    mNsdManager.resolveService(service, mResolveListener);
                }


            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.

                Log.e(TAG, "service lost" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Log.d(TAG, "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {

                NsdServiceInfo mService = serviceInfo;
                resutNsdServiceInfo = serviceInfo;

                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);

                int port = mService.getPort();
                InetAddress host = mService.getHost();

                discoveredPORT=port;
                discoveredHost=host;


                Log.d(TAG, "DISCOVERED PORT 2= " + port);
                Log.d(TAG, "HOST ADDRESS= " + host);


                Toast.makeText(MainActivity.this, "Successfully connected",
                        Toast.LENGTH_LONG).show();


                try {
                    Socket socket = new Socket(discoveredHost,discoveredPORT);
                    DataInputStream dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    String msgToServer=getIpAddress();

                    dataOutputStream.writeUTF(msgToServer);
                    Log.d(TAG,"Did it work?");
                } catch (IOException e) {
                    e.printStackTrace();
                }

//
//                TextView handshakeOnDiscovery = (TextView) findViewById(R.id.handshakeTxtView);
//
//                handshakeOnDiscovery.setVisibility(View.VISIBLE);
//                handshakeOnDiscovery.setText(discoveredHost +"");

                Intent intent = new Intent();
                intent.putExtra("HOST",discoveredHost+"")
                        .setAction(getPackageName() + ".HOST");
                intent.putExtra("PORT",discoveredPORT+"")
                        .setAction(getPackageName() + ".HOST");

                sendBroadcast(intent);

                if (serviceInfo.getServiceName().equals(SERVICE_NAME)) {
                    Log.d(TAG, "Same IP.");

                }


            }
        };
    }


    private String getIpAddress() {
        String ip=null;
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }






}
