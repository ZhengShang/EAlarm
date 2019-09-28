package com.wolfer.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;

public class DatabaseHelper extends SQLiteOpenHelper {
    String sql_create = "create table words (id integer primary key autoincrement, word varchar(15) , mean varchar(100))";
    private Context context;

    public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql_create);
        Toast.makeText(context, "CREATE DATABASE SUCCESS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }

    public void insertwords(SQLiteDatabase db) throws Exception {
        FileReader fileReader = new FileReader("F:\\words.txt");

        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line = null;// ���������ϣ���Ȼ��֪��Ϊʲô

        while ((line = bufferedReader.readLine()) != null) {
            if (line.trim().contains(" ")) {
                String str[] = line.split(" ");
                String word = str[0];
                String mean = null;
                for (int i = 1; i < str.length; i++) {
                    mean += str[i];
                }
                ContentValues cv = new ContentValues();
                cv.put(word, mean);
                db.insert("words", null, cv);
            }
        }
        bufferedReader.close();
        fileReader.close();
        Log.i("ME", "INSERT WORDS SUCCESS");
    }


}
