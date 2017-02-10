package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import java.util.ArrayList;
import java.util.HashMap;

import utils.Constants;

/**
 * Created by Miss Metal on 27/11/2016.
 */

public class DBController extends SQLiteOpenHelper {
    public DBController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query;
        query = "CREATE TABLE "+ Constants.TABLE_ANTENAS_NAME +"( Sector varchar(200)," +
                "  Estado varchar(200)," +
                "  Fecha_Estado date," +
                "  Tipo_Instalacion varchar(200)," +
                "  Azimut int(30)," +
                "  Modelo_Antena varchar(200)," +
                "  Altura int(30)," +
                "  Tilt_Mecanico int(30)," +
                "  Til_Electrico int(30)," +
                "  Tilt_Electrico_Remoto varchar(200)," +
                "  Observaciones varchar(200)" +
                "ID int(30)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query;
        query = "DROP TABLE IF EXISTS " + Constants.TABLE_ANTENAS_NAME;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }

    public void insertRow(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", queryValues.get("id"));
        values.put("estado", queryValues.get("estado"));
        values.put("fecha", queryValues.get("fecha"));
        values.put("tipo", queryValues.get("tipo"));
        values.put("azimut", queryValues.get("azimut"));
        values.put("modelo", queryValues.get("modelo"));
        values.put("altura", queryValues.get("altura"));
        values.put("tiltmecanico", queryValues.get("tiltmecanico"));
        values.put("tiltelectrico", queryValues.get("tiltelectrico"));
        values.put("tiltelectricoremoto", queryValues.get("tiltelectricoremoto"));
        values.put("observaciones", queryValues.get("observaciones"));
        database.insert(Constants.TABLE_ANTENAS_NAME, null, values);
        database.close();
    }

    public ArrayList<HashMap<String, String>> getAllRows() {
        ArrayList<HashMap<String, String>> antenasLista;
        antenasLista = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Constants.TABLE_ANTENAS_NAME;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("estado", cursor.getString(1));
                antenasLista.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return antenasLista;
    }

}
