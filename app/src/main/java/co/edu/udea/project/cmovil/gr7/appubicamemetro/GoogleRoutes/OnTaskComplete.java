package co.edu.udea.project.cmovil.gr7.appubicamemetro.GoogleRoutes;

/**
 * Created by Yulian on 15/05/2015.
 */
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

public interface OnTaskComplete {

    void onTaskCompleted(List<List<HashMap<String, String>>> jsonObj);
}
