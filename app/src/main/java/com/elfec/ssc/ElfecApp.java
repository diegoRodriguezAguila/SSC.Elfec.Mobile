package com.elfec.ssc;

import com.activeandroid.app.Application;
import com.cesarferreira.rxpaper.RxPaper;
import com.elfec.ssc.messaging.GcmNotificationBgReceiver;
import com.elfec.ssc.messaging.GcmNotificationReceiver;
import com.elfec.ssc.security.AppPreferences;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer;
import io.paperdb.Paper;
import rx_gcm.internal.RxGcm;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ElfecApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/segoe_ui.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        JodaTimeAndroid.init(this);
        AppPreferences.init(this);
        RxPaper.init(this);
        Paper.addSerializer(DateTime.class, new JodaDateTimeSerializer());
        RxGcm.Notifications.register(this, GcmNotificationReceiver.class,
                GcmNotificationBgReceiver.class).subscribe();
    }

}
