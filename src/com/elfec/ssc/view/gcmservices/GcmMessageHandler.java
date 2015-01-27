package com.elfec.ssc.view.gcmservices;

import com.elfec.ssc.R;
import com.elfec.ssc.view.RegisterAccount;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmMessageHandler extends IntentService {

     String mes;
     String tit;
     private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

       tit = extras.getString("title");
       mes=extras.getString("message");
       showToast();
       Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
            	{
            	NotificationManager NM;
            	NM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                 //String subject = "Alerta";
                 String body = mes;
                 Intent notificationIntent = new Intent(getApplicationContext(), RegisterAccount.class);

                 notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                         | Intent.FLAG_ACTIVITY_SINGLE_TOP);   
                 
                 PendingIntent pending=PendingIntent.getActivity(
                 getApplicationContext(),0, notificationIntent,0);
                 NM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                 Notification notify=(new NotificationCompat.Builder(getApplicationContext())).setContentIntent(pending)
                		    .setSmallIcon(R.drawable.logo_elfec)
                		    .setTicker(body)
                		    .setWhen(System.currentTimeMillis())
                		    .setContentTitle(tit)
                		    .setContentText(body).build();
                 NM.notify(0, notify);
           //     Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
            }
            }
         });

    }
}