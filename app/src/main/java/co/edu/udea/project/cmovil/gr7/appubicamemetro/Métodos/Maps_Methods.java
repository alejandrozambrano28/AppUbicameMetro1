package co.edu.udea.project.cmovil.gr7.appubicamemetro.Métodos;

import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import co.edu.udea.project.cmovil.gr7.appubicamemetro.R;

/**
 * Created by Yulian
 */
public class Maps_Methods {
    private HashMap<String, List<LatLng>> MarkerList;

    public Maps_Methods() {
        List<LatLng> markerList = new ArrayList<>();
        MarkerList = new HashMap<String, List<LatLng>>();
        markerList.add(new LatLng(6.245691, -75.566994));
        markerList.add(new LatLng(6.245343, -75.565836));
        markerList.add(new LatLng(6.242805, -75.561373));
        markerList.add(new LatLng(6.241940, -75.559968));
        markerList.add(new LatLng(6.240302, -75.558702));
        markerList.add(new LatLng(6.239302, -75.557174));
        markerList.add(new LatLng(6.238001, -75.554679));
        markerList.add(new LatLng(6.236730, -75.553328));
        markerList.add(new LatLng(6.235105, -75.553492));
        markerList.add(new LatLng(6.231340, -75.549851));
        markerList.add(new LatLng(6.228975, -75.548078));
        markerList.add(new LatLng(6.226078, -75.546392));
        MarkerList.put("01", markerList);
    }

    /**
     * Metodo mediante el cual se asignan los marcadores de los
     *
     * @param Map1 mapa
     * @param latLng localizacion
     */

    public void Markers(GoogleMap Map1, LatLng latLng) {
        Map1.addMarker(((new MarkerOptions().position((latLng)).title("find me here").snippet("Esta es mi posición Actual").alpha(0.8f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))));
        int cnt = MarkerList.size() + 1;
        for (int j = 1; j < cnt; j++) {
            String aux = "0" + j;
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
            String aux = "0" + j;
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
            String aux = "0" + j;
            for (double cont : distance_Array.get(aux)) {
                cnt_dist++;
                if (cont < result) {
                    result = cont; /** Distancia mas corta hallada */
                    control = aux; /** Ruta del arreglo del rutas a la que pertence la distancia mas corta */
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

    public void draw_Circle(GoogleMap map, Vector vector, int color ) {
        double init = Double.parseDouble(String.valueOf(vector.get(3)));
        double fin = Double.parseDouble(String.valueOf(vector.get(4)));
        CircleOptions circle= new CircleOptions().center(new LatLng(init,fin)).radius(30).strokeColor(color).strokeWidth(10);
        map.addCircle(circle);
    }

    public void draw_Routes(GoogleMap map){
        int cnt = MarkerList.size()+1;
        for(int j = 1; j < cnt; j++){
            String aux = "0" + j;
            PolylineOptions polyline = new PolylineOptions().addAll(MarkerList.get(aux)).geodesic(true).color(Color.RED);
            map.addPolyline(polyline);
        }
    }

}