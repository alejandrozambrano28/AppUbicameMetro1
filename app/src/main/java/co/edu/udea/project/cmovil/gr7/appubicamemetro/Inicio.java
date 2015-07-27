package co.edu.udea.project.cmovil.gr7.appubicamemetro;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
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

import co.edu.udea.project.cmovil.gr7.appubicamemetro.GoogleRoutes.LlenarBD;
import co.edu.udea.project.cmovil.gr7.appubicamemetro.GoogleRoutes.OnTaskComplete;
import co.edu.udea.project.cmovil.gr7.appubicamemetro.Métodos.Maps_Methods;
import co.edu.udea.project.cmovil.gr7.appubicamemetro.Métodos.Maps_routesMethods;


public class Inicio extends ActionBarActivity implements OnTaskComplete, LocationListener{
    private final LatLng LOCATION_BURNABY = new LatLng(6.267847, -75.333332);
    private final LatLng LOCATION_SURREY = new LatLng(6.267847, -75.568533);
    LatLng latLng; //Coordenadas actuales del usuario
    Marker markers;//Coordenadas a las cuales el usuario quiere llegar
    Circle circle;
    GoogleMap map1;
    Maps_Methods methods;
    int[][]Colors;
    Vector vector;
    Vector vector2;
    LocationManager locationManager;
    Button rutas;
    Button sugerencia;
    TextView transfer;
    ImageView imagen;
    Fragment ap;
    private LlenarBD manager;
    //List<List<HashMap<String, String>>> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        ap = new Prueba();
        imagen = (ImageView)findViewById(R.id.dos);
        Toast.makeText(getApplicationContext(),"Activar GPS",Toast.LENGTH_LONG).show();
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        Toast.makeText(getApplicationContext(),"GPS Activo",Toast.LENGTH_LONG).show();
        manager =new LlenarBD(this);

        //Caucasia
        manager.insertar("01",6.245691,-75.566994,true); // Coordenadas de Transferencia a
        manager.insertar("01",6.245343,-75.565836,true);
        manager.insertar("01",6.242805,-75.561373,true);
        manager.insertar("01",6.241940,-75.559968,true);
        manager.insertar("01",6.240302,-75.558702,true);
        manager.insertar("01",6.239302,-75.557174,true);
        manager.insertar("01",6.238001, -75.554679,true);
        manager.insertar("01",6.236730, -75.553328,true);
        manager.insertar("01",6.236437, -75.553462,false);
        manager.insertar("01",6.235786, -75.553280,false);
        manager.insertar("01",6.235903, -75.553612,false);
        manager.insertar("01",6.235338, -75.553956,false);
        manager.insertar("01",6.235105, -75.553492,true);
        manager.insertar("01",6.231340, -75.549851,true);
        manager.insertar("01",6.228975, -75.548078,true);
        manager.insertar("01",6.226078, -75.546392,true);

        //Metro
        manager.insertar("02",6.337702, -75.544424,true);//Estacion niquia
        manager.insertar("02",6.330137, -75.553676,true);
        manager.insertar("02",6.315978, -75.555418,true);
        manager.insertar("02",6.300238, -75.558336,true);
        manager.insertar("02",6.290427, -75.564645,true);
        manager.insertar("02",6.278227, -75.569451,true);
        manager.insertar("02",6.269312, -75.565846,true);
        manager.insertar("02",6.263851, -75.563357,true);// Hospital Coordenadas de Tranferencia a 6.263863, -75.563419 (4) y a 6.263944, -75.563265(6) y a 6.263087, -75.563596(5)
        manager.insertar("02",6.256898, -75.566061,true);
        manager.insertar("02",6.250456, -75.568207,true);
        manager.insertar("02",6.247086, -75.569623,true);
        manager.insertar("02",6.242991, -75.571511,true);
        manager.insertar("02",6.238597, -75.573271,true);
        manager.insertar("02",6.229851, -75.575803,true);// Industriales Coordenadas de Tranferencia a 6.230616, -75.576628(3) y a 6.229983, -75.575660(4) y 6.231178, -75.576590(5)
        manager.insertar("02",6.212317, -75.578206,true);
        manager.insertar("02",6.193203, -75.582583,true);
        manager.insertar("02",6.185907, -75.585587,true);
        manager.insertar("02",6.174004, -75.597175,true);
        manager.insertar("02",6.153825, -75.623295,true);
        manager.insertar("02",6.152689, -75.625861,true);

        //metro plus
        manager.insertar("03",6.230959, -75.609318,true); // Inicio
        manager.insertar("03",6.231043, -75.605316,true);
        manager.insertar("03",6.231099, -75.601192,true);
        manager.insertar("03",6.231341, -75.596665,true);
        manager.insertar("03",6.231543, -75.590907,true);
        manager.insertar("03",6.231632, -75.586575,true);
        manager.insertar("03",6.231788, -75.581969,true);
        manager.insertar("03",6.230616, -75.576628,true); // Fin - Transferencia a 6.229851, -75.575803

        //linea1
        manager.insertar("04",6.229983, -75.575660,false); // Fin - Transferencia a 6.229851, -75.575803(2)
        manager.insertar("04",6.236740, -75.576650,false);
        manager.insertar("04",6.243352, -75.575376,true);
        manager.insertar("04",6.250739, -75.575074,true);
        manager.insertar("04",6.254457, -75.574703,false);
        manager.insertar("04",6.256606, -75.572756,true);
        manager.insertar("04",6.260847, -75.569116,true);
        manager.insertar("04",6.264392, -75.567515,true);
        manager.insertar("04",6.263863, -75.563419,true); // Inicio - Transferencia a 6.263851, -75.563357(2)

        //linea2
        manager.insertar("05",6.231178, -75.576590,true); // Inicio - Transferencia a 6.229851, -75.575803(2)
        manager.insertar("05",6.228656, -75.571080,true);
        manager.insertar("05",6.227554, -75.569431,false);
        manager.insertar("05",6.233662, -75.570034,true);
        manager.insertar("05",6.238774, -75.570368,false);
        manager.insertar("05",6.240842, -75.569736,true);
        manager.insertar("05",6.240842, -75.569736,false);
        manager.insertar("05",6.246566, -75.566460,true);
        manager.insertar("05",6.249340, -75.564570,true);
        manager.insertar("05",6.253069, -75.562409,true);
        manager.insertar("05",6.254474, -75.562241,false);
        manager.insertar("05",6.257134, -75.565903,false);
        manager.insertar("05",6.257788, -75.565720,true);
        manager.insertar("05",6.263087, -75.563596,true); // Fin - Tranferencia a 6.263851, -75.563357(2)

        //metro plus
        manager.insertar("06",6.263944, -75.563265,true);// Inicio transferencia a 6.263851, -75.563357(2)
        manager.insertar("06",6.261971, -75.555922,true);
        manager.insertar("06",6.267676, -75.555029,true);
        manager.insertar("06",6.273280, -75.554050,true);
        manager.insertar("06",6.278318, -75.553150,true);
        manager.insertar("06",6.281512, -75.552627,false);
        manager.insertar("06",6.282747, -75.552932,true);
        manager.insertar("06",6.284111, -75.552944,false);
        manager.insertar("06",6.284552, -75.556622, false);
        manager.insertar("06",6.285169, -75.55663, true);// Fin

        rutas = (Button)findViewById(R.id.button);
        transfer = (TextView)findViewById(R.id.Transferencia);
        sugerencia = (Button)findViewById(R.id.button2);
        methods = new Maps_Methods(manager);
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
            //routes = listLatLong;
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
        Colors = methods.draw_Routes(map1);

        map1.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            public void onMapClick(LatLng arg0){
                if(markers == null){
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
               // drawRoutes(this.routes);

    }


    /**
     * Métodos onClicks para los diferentes botones con los que cuenta muesra aplicacion
     *
     * @param v
     *
     */

    public void onClick_Routes(View v){
        Maps_routesMethods p = new Maps_routesMethods();
        String Traser1 = p.final_route(vector,vector2,manager);
        String Transer2 = null;
        //imagen.setBackgroundColor(Color.parseColor("#FF9494D9"));
        //image1.setBackgroundColor(Color.argb(255, Colors[Integer.parseInt(vector2.get(0).toString())][0], Colors[Integer.parseInt(vector2.get(0).toString())][1], Colors[Integer.parseInt(vector2.get(0).toString())][2]));
        //image2.setBackgroundColor(Color.argb(255,Colors[Integer.parseInt(Traser1)][0],Colors[Integer.parseInt(Traser1)][1],Colors[Integer.parseInt(Traser1)][2]));
        //Intent i = new Intent(this,Prueba.class);
        //startActivity(i);
    }

    public void onClick_Near(View v) {
        LatLng arrive_position = new LatLng(markers.getPosition().latitude,markers.getPosition().longitude);
        vector = methods.Distance(arrive_position);
        vector2 = methods.Distance(latLng);
        rutas.setEnabled(true);
        int color = Color.GREEN;
        if (circle == null){
            LatLng x = new LatLng(Double.parseDouble(vector.get(3).toString()),Double.parseDouble(vector.get(4).toString()));
            circle = methods.draw_Circle(map1,vector, color);
            //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(x, 2));
            //map1.animateCamera(CameraUpdateFactory.zoomTo(16));

        }else{
            circle.remove();
            circle = methods.draw_Circle(map1,vector, color);
            //LatLng x = new LatLng(Double.parseDouble(vector.get(3).toString()),Double.parseDouble(vector.get(4).toString()));
            //map1.moveCamera(CameraUpdateFactory.newLatLngZoom(x, 2));
            //map1.animateCamera(CameraUpdateFactory.zoomTo(16));

        }
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
