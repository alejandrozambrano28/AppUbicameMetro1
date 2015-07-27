package co.edu.udea.project.cmovil.gr7.appubicamemetro.Métodos;

import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.provider.CalendarContract;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import co.edu.udea.project.cmovil.gr7.appubicamemetro.GoogleRoutes.LlenarBD;
import co.edu.udea.project.cmovil.gr7.appubicamemetro.R;

/**
 * Created by Yulian on 19/06/2015.
 */
public class Maps_Methods {
    private HashMap<String, List<LatLng>> MarkerList;
    private HashMap<String, List<LatLng>> MarkerListRoutes;
    private Cursor cursor;
    private Cursor cursor2;



    public Maps_Methods(LlenarBD prueba) {
        String[] from = new String[]{prueba.CN_RUTA, prueba.CN_LAT, prueba.CN_LONG, prueba.CN_PARADA};
        try {
            cursor =  prueba.retornaBD().getReadableDatabase().query(prueba.TABLE_NAME, from, null, null, null, null, null);
            int  ilat,ilong;
            ilat=cursor.getColumnIndex(prueba.CN_LAT);
            ilong=cursor.getColumnIndex(prueba.CN_LONG);
            String control = "1";
            String flag = "1";
            List<LatLng> markerList = new ArrayList<>();
            MarkerList = new HashMap<String, List<LatLng>>();
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                if (control.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(prueba.CN_RUTA))) && flag.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(prueba.CN_PARADA)))) {
                    markerList.add(new LatLng(cursor.getDouble(ilat), cursor.getDouble(ilong)));
                } else if (flag.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(prueba.CN_PARADA)))) {
                    MarkerList.put(control, markerList);
                    markerList = new ArrayList<>();
                    control = cursor.getString(cursor.getColumnIndex(prueba.CN_RUTA));
                }

            }
            Maps_Routes(prueba);
        }catch (java.lang.NullPointerException e){
            System.out.print("el error " + e);
        }

    }

    public void Maps_Routes(LlenarBD prueba) {
        String[] from = new String[]{prueba.CN_RUTA, prueba.CN_LAT, prueba.CN_LONG};
        try {
            cursor2 =  prueba.retornaBD().getReadableDatabase().query(prueba.TABLE_NAME, from, null, null, null, null, null);
            String control = "1";
             List<LatLng> markerListroutes = new ArrayList<>();
             MarkerListRoutes = new HashMap<String, List<LatLng>>();

             for (cursor2.moveToFirst(); !cursor2.isAfterLast(); cursor2.moveToNext()) {
                 if (control.equalsIgnoreCase(cursor2.getString(cursor2.getColumnIndex(prueba.CN_RUTA)))){
                    markerListroutes.add(new LatLng(cursor2.getDouble(cursor2.getColumnIndex(prueba.CN_LAT)),cursor2.getDouble(cursor2.getColumnIndex(prueba.CN_LONG))));
                 } else{
                 MarkerListRoutes.put(control, markerListroutes);
                 markerListroutes = new ArrayList<>();
                 control = cursor2.getString(cursor2.getColumnIndex(prueba.CN_RUTA));
                 }
             }
        }catch (java.lang.NullPointerException e){
            System.out.print("el error " + e);
        }
        System.out.println("Espera");
    }

    /**
     * Metodo mediante el cual se asignan los marcadores de los
     *
     * @param Map1
     * @param latLng
     */

    public void Markers(GoogleMap Map1, LatLng latLng) {
        Map1.addMarker(((new MarkerOptions().position((latLng)).title("find me here").snippet("Esta es mi posición Actual").alpha(0.8f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))));
        int cnt = MarkerList.size() + 1;
        for (int j = 1; j < cnt; j++) {
            String aux = String.valueOf(j);
            for (LatLng coor : MarkerList.get(aux)) {
                Map1.addMarker((new MarkerOptions().position(coor).icon(BitmapDescriptorFactory.fromResource(R.drawable.descarga))));
            }
        }
    }

    /**
     * Metodo mediante el cual se calcula la distancia entre entre la locacion y los alimentadores
     *
     * @param location posiscion a la cual se le desea saber la distancia
     * @return retorna un vector con ruta, posicion(latitud,longitud), distancia.
     */

    public Vector Distance(LatLng location) {
        float[] mts = new float[1];
        int cnt = MarkerList.size() + 1;
        int i = 0;
        HashMap<String, List<Double>> array_Distances = new HashMap<>();
        for (int j = 1; j < cnt; j++) {
            String aux = String.valueOf(j);
            List<Double> distances = new ArrayList<>();
            for (LatLng coord : MarkerList.get(aux)) {
                Location.distanceBetween(location.latitude, location.longitude, coord.latitude, coord.longitude, mts);
                distances.add(Double.parseDouble(String.valueOf(mts[0])));
            }
            array_Distances.put(aux, distances);
        }

        Vector result = small_Distance(array_Distances);
        cnt = Integer.parseInt(String.valueOf(result.get(1)));
        for (LatLng coord : MarkerList.get(result.get(0))) {
            i++;
            if (i == cnt) {
                result.add(coord.latitude);
                result.add(coord.longitude);
                break;
            }
        }
        return result;
    }


    /**
     * Metodo que halla el alimentador mas cercano a la posicion de llegada
     *
     * @param distance_Array array de distancias de todos los alimentadores separados por rutas
     * @return retorna un vector con ruta, posicion(latitud,longitud), distancia.
     */
    public Vector small_Distance(HashMap<String, List<Double>> distance_Array) {
        double result = 9999999999999999999999999999999999999999.9;
        int cnt = distance_Array.size() + 1;
        int cnt_dist = 0;
        int cnt_result = 0;
        String control = null;
        Vector vectro_result = new Vector();
        for (int j = 1; j < cnt; j++) {
            String aux = String.valueOf(j);
            for (double cont : distance_Array.get(aux)) {
                cnt_dist++;
                if (cont < result) {
                    result = cont; /** Distancia mas corta hallada */
                    control = aux; /** Ruta del arreglo de rutas a la que pertence la distancia mas corta */
                    cnt_result = cnt_dist; /** posiscion en la cual se halla la posicion mas cercana dentro del arreglo de distancias */
                }
            }
            cnt_dist = 0;
        }
        vectro_result.add(control);
        vectro_result.add(cnt_result);
        vectro_result.add(result);
        return vectro_result;
    }

    /**
     * Metodo para dibujar circulos en google maps
     * @param map referencia al mapa
     * @param vector vector con pa posicion
     * @param color entero con el color
     */

    public Circle draw_Circle(GoogleMap map, Vector vector, int color ) {
        double init = Double.parseDouble(String.valueOf(vector.get(3)));
        double fin = Double.parseDouble(String.valueOf(vector.get(4)));
        CircleOptions circle= new CircleOptions().center(new LatLng(init,fin)).radius(60).strokeColor(color).strokeWidth(10);
        return map.addCircle(circle);

    }

    public int[][] draw_Routes(GoogleMap map){
        int cnt = MarkerListRoutes.size()+1;
        boolean flag = false;
        Random color = new Random();
        int [][]Colors = new int[cnt][3];
        for(int j = 1; j < cnt; j++){
            int a = color.nextInt(256);
            int b = color.nextInt(256);
            int c = color.nextInt(256);
            Colors[j][0] = a;
            Colors[j][1] = b;
            Colors[j][2] = c;
            int g = Color.argb(255,Colors[j][0],Colors[j][1],Colors[j][2]);
            String kk = String.format("#%08X",g);
            String aux = String.valueOf(j);
            PolylineOptions polyline = new PolylineOptions().addAll(MarkerListRoutes.get(aux)).geodesic(true).color(Color.argb(255,Colors[j][0],Colors[j][1],Colors[j][2]));
            map.addPolyline(polyline);
        }
        return Colors;
    }

}