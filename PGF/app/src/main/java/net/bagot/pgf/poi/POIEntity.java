package net.bagot.pgf.poi;

import com.google.android.gms.location.Geofence;

import java.util.Map;

/* *************************************************************
 * @Author Mathieu BAGOT (mathieu.bagot@univ-ubs.fr)
 * @Date 2016/03/18
 *
 * POIEntity
 *
 * A group of points of interests defined by a group name and
 * a map of geofences (id/geofence object)
 *
 * **************************************************************/
public class POIEntity {
    private String groupName;
    private Map<String, Geofence> poiMap;

    // ///////////////////////////////////////////////////////////////////////////////////////////
    // Getter / setter

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<String, Geofence> getPOIMap() {
        return poiMap;
    }

    public void setPOIMap(Map<String, Geofence> poiMap) {
        this.poiMap = poiMap;
    }
}
