package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

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
        query = "CREATE TABLE antenas ( Sector varchar(200)," +
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
        query = "DROP TABLE IF EXISTS antenas";
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
        database.insert("antenas", null, values);
        database.close();
    }

    public ArrayList<HashMap<String, String>> getAllUsers() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM users";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", cursor.getString(0));
                map.put("userName", cursor.getString(1));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return usersList;
    }

}
