package com.hoai.lab4v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbManager extends SQLiteOpenHelper {
    private static final String dbname = "StudentDesc.db";

    public DbManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String qry = "create table tb1_student (id interger primary key autoincrement , title text ,content text)";
        db.execSQL(qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tb1_student");
        onCreate(db);
    }

    public String addRecord(String p1, String p2)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("title", p1);
        cv.put("content", p2);
    }
}
