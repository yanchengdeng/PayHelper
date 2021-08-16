package com.xq.payhelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xq.payhelper.common.Constants;
import com.xq.payhelper.entity.Bill;


public class HelperDbHelper extends SQLiteOpenHelper {

    public HelperDbHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + Bill.TABLE_NAME + " ("
                + Bill._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Bill.DATE + " TEXT NOT NULL, "
                + Bill.TITLE + " TEXT NOT NULL, "
                + Bill.CONTENT + " TEXT NOT NULL, "
                + Bill.MONEY + " TEXT NOT NULL, "
                + Bill.SYNC + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
