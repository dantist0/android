package com.d.net_brains_test.controller.search;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.d.net_brains_test.model.DBHelper;
import com.d.net_brains_test.model.SearchResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Алексей on 06.05.2018.
 */

public class CourseSaver {
    private static Runnable changeListener;
    public static void setChangeListener(Runnable listener){
        changeListener = listener;
        changeListener.run();
    }
    public static void save(Context context, SearchResponse.Result result){

        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            StringBuilder autors = new StringBuilder("");
            String prefix = "";
            if(result.course_authors != null)
                for (int autor : result.course_authors) {
                    autors.append(prefix);
                    prefix = "|";
                    autors.append(autor);
                }

            String[] toInsert = {
                    result.id + "",
                    result.course + "",
                    result.target_id + "",
                    result.target_type + "",
                    result.course + "",
                    result.course_owner + "",
                    result.course_title,
                    result.course_slug,
                    result.course_cover,
                    autors.toString()
            };
            db.execSQL("INSERT INTO " + DBHelper.DATABASE_NAME + " (id, score, target_id, target_type, course, " +
                    "course_owner, course_title, course_slug, course_cover, course_autors) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", toInsert);
        }catch (SQLiteConstraintException e){

        }
        finally {
            db.close();
        }
        if(changeListener != null)
            changeListener.run();
//        try {
//            FileOutputStream os = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
//            List<SearchResponse.Result> cources = load();
//            cources.add(result);
//            objectOutputStream.writeObject(result);
//            objectOutputStream.flush();
//            objectOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(GlobalAdapter.currentContext,R.string.imput_output_error, Toast.LENGTH_LONG).show();
//        }
    }
    public static List<SearchResponse.Result> load(Context context){
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+DBHelper.DATABASE_NAME, null);

        List<SearchResponse.Result> results = new ArrayList<>();
        try {
            cursor.moveToPosition(0);
            if (cursor.getCount() != 0)
                do {
                    SearchResponse.Result result = new SearchResponse().new Result();
                    result.id = cursor.getInt(cursor.getColumnIndex("id"));
                    result.score = cursor.getFloat(cursor.getColumnIndex("score"));
                    result.target_id = cursor.getInt(cursor.getColumnIndex("target_id"));
                    result.target_type = cursor.getString(cursor.getColumnIndex("target_type"));
                    result.course = cursor.getInt(cursor.getColumnIndex("course"));
                    result.course_owner = cursor.getInt(cursor.getColumnIndex("course_owner"));
                    result.course_title = cursor.getString(cursor.getColumnIndex("course_title"));
                    result.course_slug = cursor.getString(cursor.getColumnIndex("course_slug"));
                    result.course_cover = cursor.getString(cursor.getColumnIndex("course_cover"));
                    String autorsRaw = cursor.getString(cursor.getColumnIndex("course_autors"));
                    result.course_authors = new ArrayList<>();
                    Log.e("MY", autorsRaw);
                    try {
                        for (String str : autorsRaw.split("[|]")) {
                            result.course_authors.add(Integer.parseInt(str));
                        }
                    }catch (NumberFormatException e){}
                    results.add(result);
                } while (cursor.moveToNext());
        }finally {
            cursor.close();
            db.close();
        }
        return results;
    }
    public static void delete(Context context, SearchResponse.Result result){
        DBHelper helper = new DBHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM "+ DBHelper.DATABASE_NAME + " WHERE target_id = " + result.target_id);
            if(changeListener != null)
                changeListener.run();
        }finally {
            db.close();
        }
    }
}
