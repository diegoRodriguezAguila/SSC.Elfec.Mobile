package com.elfec.ssc.gcmservices;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;

import com.activeandroid.util.Log;
import com.elfec.ssc.helpers.ViewPresenterManager;
import com.elfec.ssc.presenter.LocationServicesPresenter;
import com.elfec.ssc.view.LocationServicesActivity;

import org.json.JSONArray;

/**
 * Maneja las notificaciones de recepción de nuevos puntos de ubicación
 *
 * @author drodriguez
 */
public class PointsUpdateGCMHandler implements IGCMHandler {
    //private final int NOTIF_ID = 3;
    @Override
    public void handleGCMessage(Bundle messageInfo,
                                NotificationManager notifManager, Builder builder) {
        try {
            JSONArray result = new JSONArray(messageInfo.getString("points"));
            LocationServicesPresenter presenter = ViewPresenterManager
                    .getPresenter(LocationServicesPresenter.class);
            if (presenter != null) {
                presenter.loadLocationPoints();
            }
        } catch (Exception ex) {
            Log.i(ex.getMessage());
        }

    }

    @Override
    public Class<? extends Activity> getActivityClass() {
        return LocationServicesActivity.class;
    }

}
