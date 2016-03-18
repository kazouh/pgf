package net.bagot.pgf.poi;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.location.Geofence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/* *************************************************************
 * @Author Mathieu BAGOT (mathieu.bagot@univ-ubs.fr)
 * @Date 2016/03/18
 *
 * POIParser
 *
 * Parses a json file to a POIEntity object
 *
 * --------------------------
 * JSON scheme :
 *
 * {
 *      "groupName":"String",
 *      "poiList": [
 *          {
 *              "id":"String",
 *              "lat":"Double",
 *              "long":"Double",
 *              "radius","Int"
 *          }, {...}
 *      ]
 *  }
 *
 * **************************************************************/
public class POIParser {
    private static final String TAG = "POIParser";

    /**
     * Parses the input JSON string to a POIEntity object
     *
     * @param json string to parse
     * @return The POIEntity objects instantiated from the input string
     */
    public static POIEntity parsePOI(String json) {
        POIEntity poiEntity = null;
        JSONObject jsonPOI = null;

        try {
            jsonPOI = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        if (jsonPOI != null) {
            try {
                // read groupName & poiEntity list
                String name = jsonPOI.getString("groupName");
                Map<String, Geofence> poiMap = readPOI(name, jsonPOI.getJSONArray("poiList"));
                if (poiMap != null && !poiMap.isEmpty()) {
                    poiEntity = new POIEntity();
                    poiEntity.setGroupName(name);
                    poiEntity.setPOIMap(poiMap);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "parseTrack: Failed to read json object from input:" + json);
            }
        } else {
            Log.e(TAG, "parseTrack: Failed to parse to json:" + json);
        }
        return poiEntity;
    }

    /**
     * Parses the list of POIEntity from a JSON array
     *
     * @param groupName   groupName to which the poi belongs
     * @param jsonPOIList A jsonArray which contains the list of POIEntity
     * @return A map of the parsed POIEntity (key=id, value=geofence)
     */
    private static Map<String, Geofence> readPOI(String groupName, JSONArray jsonPOIList) {
        Map<String, Geofence> poiMap = new HashMap<>();

        for (int i = 0; i < jsonPOIList.length(); i++) {
            try {
                JSONObject poi = jsonPOIList.getJSONObject(i);

                String id = poi.getString("id");
                if (id != null && !id.isEmpty()) {
                    id = groupName + "-" + id;
                }
                double latitude = poi.getDouble("lat");
                double longitude = poi.getDouble("lon");
                long radius = poi.getInt("radius");

                // build the geofence
                Geofence geofence = new Geofence.Builder()
                        .setRequestId(id)
                        .setCircularRegion(
                                latitude,
                                longitude,
                                radius
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(
                                Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_EXIT
                        )
                        .build();

                if (geofence != null) {
                    poiMap.put(id, geofence);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "readPOI: Failed to parse POIEntity from groupName:" + groupName);
                return null;
            }
        }
        return poiMap;
    }
}
