package com.shopping.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// https://www.geeksforgeeks.org/how-to-create-and-add-data-to-sqlite-database-in-android/
public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "db_s_list";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "tbl_barang";

    private static final String ID_COL = "id";
    private static final String KETERANGAN_COL = "keterangan";
    private static final String JUDUL_COL = "judul";
    private static final String TANGGAL_COL = "tanggal";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

//  Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KETERANGAN_COL + " TEXT,"
                + JUDUL_COL + " TEXT,"
                + TANGGAL_COL + " DATE)";

        db.execSQL(query);
    }

//  Insert Data
    public void addNewData(String keterangan, String judul, String tanggal) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KETERANGAN_COL, keterangan);
        values.put(JUDUL_COL, judul);
        values.put(TANGGAL_COL, tanggal);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return cursor;
    }

    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = ?", new String[]{String.valueOf(id)});
        return cursor;
    }

    public void updateDataById(int id, String judulBaru, String catatanBaru, String tanggalBaru) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("judul", judulBaru);
        values.put("keterangan", catatanBaru);
        values.put("tanggal", tanggalBaru);
        db.update(TABLE_NAME, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean deleteDataById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
