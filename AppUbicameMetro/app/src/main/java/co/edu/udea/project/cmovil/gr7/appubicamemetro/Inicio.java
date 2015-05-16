package co.edu.udea.project.cmovil.gr7.appubicamemetro;

import android.content.Context;

import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Inicio extends ActionBarActivity implements OnTaskComplete{
    private final LatLng LOCATION_BURNABY = new LatLng(6.267847, -75.333332);
    private final LatLng LOCATION_SURREY = new LatLng(6.267847, -75.568533);
    GoogleMap map1;
    LatLng latLng;
    LatLng arg01 ;
    String posActual;
    String posLlegada;
    List<List<HashMap<String, String>>> routes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setUpMapIfNeeded()
    {
        if(map1 == null)
        {
            map1 = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        if(map1 != null)
        {
            setUpMap();
        }
    }

    public LatLng getCurrentLocation(Context context)
    {
        try
        {
            LocationManager locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String locProvider = locMgr.getBestProvider(criteria, false);
            Location location = locMgr.getLastKnownLocation(locProvider);

            // getting GPS status
            boolean isGPSEnabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNWEnabled = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNWEnabled)
            {
                // no network provider is enabled
                return null;
            }
            else
            {
                // First get location from Network Provider
                if (isNWEnabled)
                    if (locMgr != null)
                        location = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled)
                    if (location == null)
                        if (locMgr != null)
                            location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            return new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (NullPointerException ne)
        {
            Log.e("Current Location", "Current Lat Lng is Null");
            return new LatLng(0,0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new LatLng(0, 0);
        }
    }

    public void setUpMap()
    {

        map1.setMyLocationEnabled(true);
        //mLocMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        map1.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        latLng = getCurrentLocation(getApplicationContext());
        map1.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 2));

        map1.animateCamera(CameraUpdateFactory.zoomTo(18));
        SystemClock.sleep(2000);

        map1.addMarker(((new MarkerOptions().position((latLng)).title("find me here").snippet("Esta es mi posición Actual"))));

        map1.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng arg0){
                map1.addMarker(new MarkerOptions().position(arg0).title(String.valueOf(arg0.latitude) + "," + String.valueOf(arg0.longitude)));
                arg01 = arg0;
            }

        });
    }

    public void onClicks(View v){
       setUpMapIfNeeded();
    }

    public void onClicker(View v){
        setLocation(arg01);
        new ManageGoogleRoutes(Inicio.this).execute("Universidad de Antioquia", "Universidad EAFIT");
        System.out.println("Final");
    }

    public void setLocation(LatLng loc){
        List<Address> list;
        if(loc.latitude != 0.0 && loc.longitude != 0.0){
            try{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                list = geocoder.getFromLocation(loc.latitude,loc.longitude,1);
                if(!list.isEmpty()){
                    Address address = list.get(0);
                    posLlegada = address.getAddressLine(0);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        if(latLng.longitude != 0.0 && latLng.latitude != 0.0){
            try{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                list = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                if(!list.isEmpty()){
                    Address address = list.get(0);
                    posActual = address.getAddressLine(0);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTaskCompleted(List<List<HashMap<String, String>>> listLatLong) {
        if (listLatLong != null && listLatLong.size() > 0) {
            routes = listLatLong;
            setUpRoutes();
        } else {
            Toast.makeText(this, "Oops!, No se logro determinar ruta ;(", Toast.LENGTH_LONG).show();
        }
    }

    private void drawRoutes(List<List<HashMap<String, String>>> result) {
        LatLng center = null;
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        setUpMapIfNeeded();

        // Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                if (center == null) {
                    //Obtengo la primera coordenada para centrar el mapa en la misma.
                    center = new LatLng(lat, lng);
                }

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(2);
            lineOptions.color(Color.RED);
        }

        // Drawing polyline in the Google Map for the i-th route
        map1.addPolyline(lineOptions);
        map1.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 13));
    }

    private void setUpRoutes() {
                drawRoutes(this.routes);

    }
}
