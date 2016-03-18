package net.bagot.pgf.util;

import android.content.Context;
import android.content.res.Resources;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;

import net.bagot.pgf.R;

import java.util.List;

/* *************************************************************
 * @Author Mathieu BAGOT (mathieu.bagot@univ-ubs.fr)
 * @Date 2016/03/18
 *
 * GeofenceHelper
 *
 * Maps errors & transitions codes with strings from values/strings.geofence.xml
 * Assist in the creation of the request to register a list of geofence
 *
 * **************************************************************/
public class GeofenceHelper {

    private GeofenceHelper() {}

    /**
     * Maps geofence transition types to their human-readable equivalents.
     */
    public static String getTransitionString(Context context, int transitionType) {
        Resources mResources = context.getResources();
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return mResources.getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return mResources.getString(R.string.geofence_transition_exited);
            default:
                return mResources.getString(R.string.unknown_geofence_transition);
        }
    }

    /**
     * Returns the error string for a geofencing error code.
     */
    public static String getErrorString(Context context, int errorCode) {
        Resources mResources = context.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return mResources.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return mResources.getString(R.string.geofence_too_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return mResources.getString(R.string.geofence_too_many_pending_intents);
            default:
                return mResources.getString(R.string.unknown_geofence_error);
        }
    }

    /**
     * Builds and returns a GeofencingRequest.
     * Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     */
    public static GeofencingRequest getGeofencingRequest(List<Geofence> geofenceList, boolean initialTrigger) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        if (initialTrigger) {
            builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        }
        builder.addGeofences(geofenceList);
        return builder.build();
    }
}
