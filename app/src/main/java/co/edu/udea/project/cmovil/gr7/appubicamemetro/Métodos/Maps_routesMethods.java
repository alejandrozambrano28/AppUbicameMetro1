package co.edu.udea.project.cmovil.gr7.appubicamemetro.Métodos;

import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import co.edu.udea.project.cmovil.gr7.appubicamemetro.GoogleRoutes.LlenarBD;


/**
 * Created by Yulian on 24/07/2015.
 */
public class Maps_routesMethods {

    private int []Routes_Colors;
    private HashMap<String,List<LatLng>> Transfers;
    private Cursor cursor;

    public Maps_routesMethods(){
        Transfers = new HashMap<String, List<LatLng>>();
        List<LatLng> list = new ArrayList<LatLng>();
        LatLng pos;
        //Ruta2-1
        pos = new LatLng(6.263863, -75.563419);
        list.add(pos);
        pos = new LatLng(6.263944, -75.563265);
        list.add(pos);
        pos = new LatLng(6.263087, -75.563596);
        list.add(pos);
        Transfers.put("2-1",list);

        //Ruta2-2
        list = new ArrayList();
        pos = new LatLng(6.230616, -75.576628);
        list.add(pos);
        pos = new LatLng(6.229983, -75.575660);
        list.add(pos);
        pos = new LatLng(6.231178, -75.576590);
        list.add(pos);
        Transfers.put("2-2",list);

        //Ruta3
        list = new ArrayList();
        pos = new LatLng(6.229851, -75.575803);
        list.add(pos);
        Transfers.put("2",list);

        //Ruta4
        list = new ArrayList();
        pos = new LatLng(6.229851, -75.575803);
        list.add(pos);
        pos = new LatLng(6.263851, -75.563357);
        list.add(pos);
        Transfers.put("4",list);

        //Ruta5
        list = new ArrayList();
        pos = new LatLng(6.229851, -75.575803);
        list.add(pos);
        pos = new LatLng(6.263851, -75.563357);
        list.add(pos);
        Transfers.put("5",list);

        //Ruta6
        list = new ArrayList();
        pos = new LatLng(6.263851, -75.563357);
        list.add(pos);
        Transfers.put("6",list);


    }

    public String final_route (Vector arrive, Vector start, LlenarBD BD){

        if(arrive.get(0).toString().equalsIgnoreCase(start.get(0).toString())){
            Log.d("Ruta", "Posicion de unicio y fin sobre la misma ruta");
            return start.get(0).toString();
        }else{
            String[] from = new String[]{BD.CN_RUTA, BD.CN_LAT, BD.CN_LONG, BD.CN_PARADA};
            String[] where = new String[]{arrive.get(0).toString()};
            cursor =  BD.retornaBD().getReadableDatabase().query(BD.TABLE_NAME,from,"ruta=?",where,null,null,null);
            cursor.moveToFirst();
            LatLng coordinit = new LatLng(cursor.getDouble(cursor.getColumnIndex(BD.CN_LAT)),cursor.getDouble(cursor.getColumnIndex(BD.CN_LONG)));
            cursor.moveToLast();
            LatLng coordFin = new LatLng(cursor.getDouble(cursor.getColumnIndex(BD.CN_LAT)),cursor.getDouble(cursor.getColumnIndex(BD.CN_LONG)));
            String aux;
            int j = Transfers.size() + 1;
            int p = 1;
               for(int i = 1; i < j; i++) {
                   if (i == 1) {
                       aux = "2-1";
                       List<LatLng> list = Transfers.get(aux);
                       if (list.contains(coordinit) || list.contains(coordFin)) {
                           return "2";
                       }
                   } else if (i == 2) {
                       aux = "2-2";
                       List<LatLng> list = Transfers.get(aux);
                       if (list.contains(coordinit) || list.contains(coordFin)) {
                           return "2";
                       }
                   } else {
                       aux = String.valueOf(i);
                       List<LatLng> list = Transfers.get(aux);
                       if (list.contains(coordinit) || list.contains(coordFin)) {
                           return aux;
                       }
                   }
               }
        }
        return null;
    }
}
