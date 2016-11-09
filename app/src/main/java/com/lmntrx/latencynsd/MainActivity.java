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

    int discoveredPORT;
    InetAddress discoveredHost;

    InetAddress handshakeIP;

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
                seconds[0] += 500;
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
        Timer timer = new Timer();
        timer.schedule(timerTask,500,500);

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

                handshakeTV.setText("Receiving.....Kittumbo kaanikkam....");

                // asynctask execute aayi kazhinjittu oru String Variable il Host IP of 2nd phone kittanam. aa host IP vechittanu 1st fone gonna send a data to 2nd phone and test latency







                    //-------------------WHILE ENDED ^^




            }
        });

        discoverBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Entered Onclick","Entered Onclick");

                buttonsLL.setVisibility(View.GONE);

                serviceHosting=false;
                serviceDiscovering=true;
                initializeDiscoveryListener();
                mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);

                Log.d(TAG, "DISCOVERED PORT 1= " + discoveredPORT);

                while(okToContinueClient==0)
                {

                    Timer timer;
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            //Generate number
                        }
                    }, 2000, 2000);

                    if (discoveredPORT!=0)
                    {
                        okToContinueClient=1;
                        timer.cancel();
                    }
                }

                new Thread(){

                    @Override
                    public void run(){

                        try {



                    Socket socket = new Socket(discoveredHost,discoveredPORT);
                    DataInputStream dataInputStream = new DataInputStream(
                            socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(
                            socket.getOutputStream());

                    String msgToServer=getIpAddress();

                    dataOutputStream.writeUTF(msgToServer);
                            Log.d(TAG,"Did it work?");

                    //TextView debug= (TextView) findViewById(R.id.debug);
                    //debug.setText("Message Wrote:"+msgToServer);
                }
                catch (IOException e)
                {

                }

                        }
                }.start();



//



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
            ((TextView)findViewById(R.id.header)).setText(intent.getStringExtra("PORT"));
            ((TextView)findViewById(R.id.handshakeTxtView)).setText(intent.getStringExtra("IP"));
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
                        intent.putExtra("PORT",SocketServerPORT);
                        intent.putExtra("IP",handshakeIP+"");
                        sendBroadcast(intent);





                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    //-------------------------
                }
            };



            handshake.start();

            try
            {
                handshake.join();

            }catch (InterruptedException ignored)
            {

            }
            return handshakeIP + "";
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
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

                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);

                int port = mService.getPort();
                InetAddress host = mService.getHost();

                discoveredPORT=port;
                discoveredHost=host;


                Log.d(TAG, "DISCOVERED PORT 2= " + port);
                Log.d(TAG, "HOST ADDRESS= " + host);


                Toast.makeText(MainActivity.this, "Successfully connected",
                        Toast.LENGTH_LONG).show();





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
