package net.bagot.pgf.poi;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.location.Geofence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class POIManager {
    private static final String TAG = "POIManager";

    private final Context context;
    private Map<String, POIEntity> entities;

    public POIManager(Context context) {
        this.context = context;
        entities = new HashMap<>();
    }

    public void parse(Uri uri) {
        Log.i(TAG, "parse: uri: " + uri);

        StringBuilder json = readText(uri);
        Log.i(TAG, "parse: json: " + json);

        if (json != null) {
            POIEntity entity = POIParser.parsePOI(json.toString());
            if (entity != null) {
                Log.i(TAG, "parse: entity parsed: " + entity.getGroupName());
                entities.put(entity.getGroupName(), entity);
            }
        }
    }

    public Set<String> getGroupNames() {
        return entities.keySet();
    }

    public List<Geofence> getPOIForGroupName(String groupName) {
        List<Geofence> result = null;

        if (entities.containsKey(groupName)) {
            POIEntity entity = entities.get(groupName);
            Map<String, Geofence> geofences = entity.getPOIMap();
            if (geofences != null) {
                result = new ArrayList<>(geofences.values());
            }
        }
        return result;
    }

    private StringBuilder readText(Uri uri) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder text = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            text = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line);
            }
            reader.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                }
            }

        }
        return text;
    }
}
