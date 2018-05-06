package com.d.net_brains_test.model;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Алексей on 06.05.2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "FAVORITES";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ DATABASE_NAME +" ("
                + "_id integer primary key autoincrement,"
                + "id integer,"
                + "score float,"
                + "target_id integer UNIQUE,"
                + "target_type text,"
                + "course integer,"
                + "course_owner integer,"
                + "course_title text,"
                + "course_slug text,"
                + "course_cover text,"
                + "course_autors text"+ ");");
        Log.e("MY", "CREATE DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_NAME);
        onCreate(db);
    }
}
