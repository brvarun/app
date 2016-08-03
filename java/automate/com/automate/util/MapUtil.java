package automate.com.automate.util;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by varun on 8/3/2016.
 */
public class MapUtil {

    private MarkerOptions markerOptions = new MarkerOptions();
    private Marker currLocationMarker = null;
    private GoogleMap mGoogleMap;


    public void goToLocation(LatLng latLng){
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        if (currLocationMarker != null){
            currLocationMarker.setPosition(latLng);
        }else{
            currLocationMarker = mGoogleMap.addMarker(markerOptions);
        }
        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        mGoogleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }
}
