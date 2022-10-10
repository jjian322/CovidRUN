package com.example.covidrun.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.covidrun.model.DependentModel;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "covidrundb";
    private static final int DB_VERSION = 1;
    private static final String ID_COL = "id";

    private static final String USER_TABLE = "userDB";
    private static final String EMAIL_COL = "email";
    private static final String PASSWORD_COL = "password";
    private static final String PHONENUM_COL = "phonenum";
    private static final String NAME_COL = "name";

    private static final String TRACING_TABLE = "tracingDB";
    private static final String PAYLOADDATA_COL = "payloadData";
    private static final String LASTUPDATEDAT_COL = "lastUpdatedAt";
    private static final String DIDRECEIVE_COL = "didReceive";

    private static final String DEPENDENT_TABLE = "dependentDB";
    private static final String DEPENDENT_NAME_COL = "dependentName";
    private static final String DEPENDENT_PHONENUM_COL = "dependentPhonenum";

    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + USER_TABLE + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + PASSWORD_COL + " TEXT,"
                + PHONENUM_COL + " INTEGER,"
                + EMAIL_COL + " TEXT)");

        db.execSQL("CREATE TABLE " + TRACING_TABLE + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PAYLOADDATA_COL + " TEXT,"
                + DIDRECEIVE_COL + " TEXT,"
                + LASTUPDATEDAT_COL + " TEXT)");

        db.execSQL("CREATE TABLE " + DEPENDENT_TABLE + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DEPENDENT_NAME_COL + " TEXT,"
                + DEPENDENT_PHONENUM_COL + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRACING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DEPENDENT_TABLE);
        onCreate(db);
    }

    public Boolean addNewUser(String name, String password, String phonenum, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(PASSWORD_COL, password);
        values.put(PHONENUM_COL, phonenum);
        values.put(EMAIL_COL, email);

        long result = db.insert(USER_TABLE, null, values);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public void updateUser (String originalName, String name, String password, String phonenum, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(PASSWORD_COL, password);
        values.put(PHONENUM_COL, phonenum);
        values.put(EMAIL_COL, email);

        db.update(USER_TABLE, values, "name=?", new String[]{originalName});
        db.close();
    }

    public Cursor getUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE, null);
        return cursor;
    }

    public Boolean addNewDependent(String name, String phonenum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DEPENDENT_NAME_COL, name);
        values.put(DEPENDENT_PHONENUM_COL, phonenum);

        long result = db.insert(DEPENDENT_TABLE, null, values);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public ArrayList<DependentModel> readDependent(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorDependent = db.rawQuery("SELECT * FROM " + DEPENDENT_TABLE, null);
        ArrayList<DependentModel> dependentModelArrayList = new ArrayList<>();

        if (cursorDependent.moveToFirst()){
            do{
                dependentModelArrayList.add(new DependentModel(cursorDependent.getString(1),
                        cursorDependent.getString(2)));
            }while (cursorDependent.moveToNext());
        }
        cursorDependent.close();
        return dependentModelArrayList;
    }

    public void updateDependent (String originalName, String name, String phonenum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DEPENDENT_NAME_COL, name);
        values.put(DEPENDENT_PHONENUM_COL, phonenum);

        db.update(DEPENDENT_TABLE, values, "dependentName=?", new String[]{originalName});
        db.close();
    }

    public Cursor getDependent(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DEPENDENT_TABLE, null);
        return cursor;
    }

    public Boolean addNewPayload(String payloadData, String didReceive, String lastUpdatedAt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PAYLOADDATA_COL, payloadData);
        values.put(DIDRECEIVE_COL, didReceive);
        values.put(LASTUPDATEDAT_COL, lastUpdatedAt);

        long result = db.insert(TRACING_TABLE, null, values);
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public void updatePayload (String originalPayloadData, String payloadData, String didReceive, String lastUpdatedAt){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PAYLOADDATA_COL, payloadData);
        values.put(DIDRECEIVE_COL, didReceive);
        values.put(LASTUPDATEDAT_COL, lastUpdatedAt);

        db.update(TRACING_TABLE, values, "payloadData=?", new String[]{originalPayloadData});
        db.close();
    }

    public Boolean comparePayload (String payloadData){
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT " + PAYLOADDATA_COL + " FROM " + TRACING_TABLE + " WHERE " + PAYLOADDATA_COL + " =? ";
        Cursor c = db.rawQuery(queryString, new String[] {payloadData});
        Boolean result = c.getCount() > 0;
        c.close();
        db.close();
        return result;
    }
}
