package co.edu.udea.project.cmovil.gr7.appubicamemetro.GoogleRoutes;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 7/07/15.
 */
public class BaseD extends SQLiteOpenHelper {


    public static String DB_NAME = "Proyec";

    public static int v_db = 1;

    public BaseD(Context contexto) {
        super(contexto,DB_NAME,null,v_db);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LlenarBD.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion) {
        if (newVersion > oldVersion) {

        }
    }
}




