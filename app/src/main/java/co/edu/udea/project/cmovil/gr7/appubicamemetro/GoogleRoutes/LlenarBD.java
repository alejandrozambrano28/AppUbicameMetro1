package co.edu.udea.project.cmovil.gr7.appubicamemetro.GoogleRoutes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by root on 7/07/15.
 */
public class LlenarBD {
  public static  final String TABLE_NAME= "Proyect";
    public static final  String CN_ID="id";
    public static final  String CN_RUTA="ruta";
    public static final  String CN_LAT="latitud";
    public static final  String CN_LONG="longitud";
    public static final  String CN_PARADA="parada";

    public static final String CREATE_TABLE="create table " +TABLE_NAME+" ("
            + CN_ID + " integer primary key autoincrement,"
            +CN_RUTA + " string not null,"
            +CN_LAT + " Double not null,"
            +CN_LONG + " Double not null,"
            +CN_PARADA + " boolean not null);";

    private BaseD admin;
    private SQLiteDatabase bd;

    public LlenarBD(Context context) {
       admin = new BaseD(context);
         bd = admin.getWritableDatabase();



    }

    public ContentValues generarContentValues (String ruta, Double latitud, Double longitud, boolean parada) {
        ContentValues valores = new ContentValues();
        valores.put(CN_RUTA, ruta);
        valores.put(CN_LAT, latitud);
        valores.put(CN_LONG, longitud);
        valores.put(CN_PARADA,parada);
        return valores;

    }

    public void insertar(String ruta, Double latitud, Double longitud,boolean parada){
        bd.insert(TABLE_NAME, null, generarContentValues(ruta, latitud, longitud, parada));


    }
   public Cursor cargarRutas() {
       String[] columnas = new String[]{CN_ID, CN_RUTA, CN_LAT, CN_LONG, CN_PARADA};
       return bd.query(TABLE_NAME, columnas, null, null, null, null, null);
   }

    public BaseD retornaBD(){
        return admin;
    }
}
