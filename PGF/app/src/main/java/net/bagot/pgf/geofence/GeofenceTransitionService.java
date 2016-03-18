package net.bagot.pgf.geofence;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import net.bagot.pgf.util.GeofenceHelper;

import java.util.List;

/* *************************************************************
 * @Author Mathieu BAGOT (mathieu.bagot@univ-ubs.fr)
 * @Date 2016/03/18
 *
 * GeofenceTransitionService
 *
 * This IntentService catches the transition events of the geofences registered with our API key
 * Transfers the transitions events with a "TRANSITION" intent
 *
 * **************************************************************/
public class GeofenceTransitionService extends IntentService {

    protected static final String TAG = "GeofenceTransition";
    public static final String TRANSITION_EVENT = "net.bagot.pgf.event.TRANSITION";

    public GeofenceTransitionService() {
        super(TAG); // worker name
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // retrieve the event from the intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceHelper.getErrorString(this, geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }
        // transfers the transition event to the listener
        int code = geofencingEvent.getGeofenceTransition();
        List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
        notifyTransition(code, geofences);
    }

    private void notifyTransition(int transitionCode, List<Geofence> geofences) {
        Intent i = new Intent(TRANSITION_EVENT);
        i.putExtra("transitionCode", transitionCode);
        i.putExtra("geofences", geofences.toArray());
        GeofenceTransitionService.this.sendBroadcast(i);
    }
}