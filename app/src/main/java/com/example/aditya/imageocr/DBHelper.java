package com.example.aditya.imageocr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by andi on 10/18/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="RECORDS";
    public static final String TABLE_NAME="ladeeda";
    public static final String COLUMN_1="ID";
    public static final String COLUMN_2="TITLE";
    public static final String COLUMN_3="DATA";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String q = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" ("+COLUMN_1+" INTEGER PRIMARY KEY, "+COLUMN_2 +" TEXT,"+COLUMN_3+" TEXT);" ;
        sqLiteDatabase.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addData(String title, String data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_2,title);
        cv.put(COLUMN_3,data);
        db.insert(TABLE_NAME,null,cv);
    }


    //I created this just for testing purposes.
    //If you wish to use it too, replace text at line 64 in MainActivity with "db.getData()".
   /* public String getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs=db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        cs.moveToFirst();
        int id=cs.getInt(0);
        String t=cs.getString(1);
        String d=cs.getString(2);
        String f=id+" "+t+"\n"+d;
        return f;
    }*/
}
