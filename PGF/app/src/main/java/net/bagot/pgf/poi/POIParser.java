package net.bagot.pgf.poi;

import android.util.Log;

import com.google.android.gms.location.Geofence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* *************************************************************
 * @Author Mathieu BAGOT (mathieu.bagot@univ-ubs.fr)
 * @Date 2016/03/18
 *
 * POIParser
 *
 * Parses a json file to a POI object
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
     * Parses the input JSON file to a POI object
     * @param input file to parse
     * @return The POI objects instantiated from the input file
     */
    public static POI parsePOI(File input) {
        POI poi = null;

        JSONObject jsonPOI = readJSON(input);
        if (jsonPOI != null) {
            try {
                // read groupName & poi list
                String name = jsonPOI.getString("groupName");
                Map<String, Geofence> poiMap = readPOI(name, jsonPOI.getJSONArray("poiList"));
                if (poiMap != null && !poiMap.isEmpty()) {
                    poi = new POI();
                    poi.setGroupName(name);
                    poi.setPOIMap(poiMap);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "parseTrack: Failed to read json object from input:" + input.getName());
            }
        } else {
            if (input != null) {
                Log.e(TAG, "parseTrack: Failed to parse to json:" + input.getName());
            } else {
                Log.e(TAG, "parseTrack: Input track is null");
            }
        }
        return poi;
    }

    /**
     * Parses the list of POI from a JSON array
     * @param groupName groupName to which the poi belongs
     * @param jsonPOIList A jsonArray which contains the list of POI
     * @return A map of the parsed POI (key=id, value=geofence)
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
                Log.e(TAG, "readPOI: Failed to parse POI from groupName:" + groupName);
                return null;
            }
        }
        return poiMap;
    }

    /**
     * Reads a file then create a JSONObject from its content
     * @param input file to read
     * @return An JSONObject created from the content of the input file
     */
    private static JSONObject readJSON(File input) {
        JSONObject jsonObject;
        String jsonString;

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(input));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            jsonString = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }
}
