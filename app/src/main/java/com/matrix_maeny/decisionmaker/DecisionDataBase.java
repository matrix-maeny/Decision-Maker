package com.matrix_maeny.decisionmaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DecisionDataBase extends SQLiteOpenHelper {

    public DecisionDataBase(@Nullable Context context) {
        super(context, "Decision.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table Decision(decision TEXT primary key)");
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table if exists Decision");
    }


    public boolean insertData(String decision){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("decision",decision);

        long result = db.insert("Decision",null,cv);

        return result != -1;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("Select * from Decision",null);
    }

    public boolean deleteData(String decision){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete("Decision","decision=?",new String[]{decision});

        return result != -1;
    }
}
