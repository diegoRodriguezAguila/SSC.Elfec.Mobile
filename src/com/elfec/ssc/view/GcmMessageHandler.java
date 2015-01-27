package com.elfec.ssc.view;

import com.elfec.ssc.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
            	 String title = tit;
                 String subject = "Alerta";
                 String body = mes;
                 NM=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                 Notification notify=new Notification(R.drawable.logo_elfec
                 ,title,System.currentTimeMillis());
                 
                 Intent notificationIntent = new Intent(getApplicationContext(), RegisterAccount.class);

                 notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                         | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                
                
                 
                 PendingIntent pending=PendingIntent.getActivity(
                 getApplicationContext(),0, notificationIntent,0);
                 notify.setLatestEventInfo(getApplicationContext(),subject,body,pending);
                 notify.flags |= Notification.FLAG_AUTO_CANCEL;
                 
                 NM.notify(0, notify);
           //     Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
            }
            }
         });

    }
}