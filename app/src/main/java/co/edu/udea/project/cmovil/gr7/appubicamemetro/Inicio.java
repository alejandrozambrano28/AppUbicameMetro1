package co.edu.udea.project.cmovil.gr7.appubicamemetro;

import android.content.Context;

import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import co.edu.udea.project.cmovil.gr7.appubicamemetro.GoogleRoutes.OnTaskComplete;
import co.edu.udea.project.cmovil.gr7.appubicamemetro.Métodos.Maps_Methods;


public class Inicio extends ActionBarActivity implements OnTaskComplete, LocationListener{
    private final LatLng LOCATION_BURNABY = new LatLng(6.267847, -75.333332);
    private final LatLng LOCATION_SURREY = new LatLng(6.267847, -75.568533);
    GoogleMap map1;
    LatLng latLng; //Coordenadas actuales del usuario
    Marker markers;//Coordenadas a las cuales el usuario quiere llegar
    Button rutas;
    Button sugerencia;
    Maps_Methods methods;
    LocationManager locationManager;
    List<List<HashMap<String, String>>> routes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        rutas = (Button)findViewById(R.id.button);
        sugerencia = (Button)findViewById(R.id.button2);
        methods = new Maps_Methods();
        setUpMapIfNeeded();

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

    @Override
    public void onTaskCompleted(List<List<HashMap<String, String>>> listLatLong) {
        if (listLatLong != null && listLatLong.size() > 0) {
            routes = listLatLong;
            setUpRoutes();
        } else {
            Toast.makeText(this, "Oops!, No se logro determinar ruta ;(", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Métodos con los cuales cargaremos los valores predeterminados que definamos en nuestra aplicacion y metodos
     * para la localizacion actual del usuario
     *
     */

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

    public void setUpMap()
    {

        map1.setMyLocationEnabled(true);
        //mLocMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        map1.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        latLng = getCurrentLocation(getApplicationContext());
        map1.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 2));

        map1.animateCamera(CameraUpdateFactory.zoomTo(16));
        SystemClock.sleep(2000);

        methods.Markers(map1,latLng);
        methods.draw_Routes(map1);

        map1.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng arg0){
                if(markers == null){
                    rutas.setEnabled(true);
                    sugerencia.setEnabled(true);
                    markers = map1.addMarker(new MarkerOptions().position(arg0).title(String.valueOf(arg0.latitude)
                            + "," + String.valueOf(arg0.longitude)).alpha(0.8f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }else{
                    markers.remove();
                    markers = map1.addMarker(new MarkerOptions().position(arg0).title(String.valueOf(arg0.latitude)
                            + "," + String.valueOf(arg0.longitude)).alpha(0.8f)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
            }

        });

    }

    public LatLng getCurrentLocation(Context context)
    {
        try
        {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String locProvider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(locProvider);
            locationManager.requestLocationUpdates(locProvider,1000,2,this);

            // getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            boolean isNWEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNWEnabled)
            {
                Toast.makeText(this,"NO está activo el GPS o NO hay conexión WIFI",Toast.LENGTH_SHORT);
                return null;
            }
            else
            {
                // First get location from Network Provider
                if (isNWEnabled)
                    if (locationManager != null)
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled)
                    if (location == null)
                        if (locationManager != null)
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

    public String setLocation(LatLng loc){
        List<Address> list;
        String result= "";
        if(loc.latitude != 0.0 && loc.longitude != 0.0){
            try{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                list = geocoder.getFromLocation(loc.latitude,loc.longitude,1);
                if(!list.isEmpty()){
                    Address address = list.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    sb.append(address.getCountryName());
                    result = sb.toString();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * Métodos con  los cuales se pintaran las rutas de los alimentadores
     *
     * @param result
     *
     */
    private void drawRoutes(List<List<HashMap<String, String>>> result) {
        LatLng center = null;
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        //setUpMapIfNeeded();

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


    /**
     * Métodos onClicks para los diferentes botones con los que cuenta muesra aplicacion
     *
     * @param v
     *
     */

    public void onClick_Routes(View v){

    }

    public void onClick_Near(View v) {
        LatLng arrive_position = new LatLng(markers.getPosition().latitude,markers.getPosition().longitude);
        Vector vector = methods.Distance(arrive_position);
        Vector vector2 = methods.Distance(latLng);
        int color = Color.GREEN;
        methods.draw_Circle(map1,vector, color);
        color = Color.BLUE;
        methods.draw_Circle(map1,vector2, color);
        System.out.println("Final");

    }

    /**
     * Métodos de la interfaz LocationListiner que se encargan de estar escuchando los cambios en la posicion del
     * usuario y hacer las respectivas operaciones
     *
     * @param location
     *
     */

    @Override
    public void onLocationChanged(Location location) {
        //alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        Vibrator vibrator =(Vibrator) getSystemService(VIBRATOR_SERVICE);
        float []dist = new float[1];
        Location.distanceBetween(location.getLatitude(),location.getLongitude(),markers.getPosition().latitude,markers.getPosition().longitude,dist);
        if( dist[0] < 200){
            vibrator.vibrate(200000/*AudioAttributes.USAGE_ALARM*/);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
/**
    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
    }
    */
}
