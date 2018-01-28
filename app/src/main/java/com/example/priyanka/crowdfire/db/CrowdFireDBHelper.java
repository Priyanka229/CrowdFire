package com.example.priyanka.crowdfire.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CrowdFireDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CROWD_FIRE_IMAGE_DB";

    private static final String SHIRT_TABLE_NAME = "shirt_table_info";
    private static final String SHIRT_COLUMN_PRIMARY_KEY = "shirt_primary_id";
    private static final String SHIRT_COLUMN_FILE_PATH = "shirt_file_path";

    private static final String TROUSER_TABLE_NAME = "trouser_table_info";
    private static final String TROUSER_COLUMN_PRIMARY_KEY = "trouser_primary_id";
    private static final String TROUSER_COLUMN_FILE_PATH = "trouser_file_path";

    private static final String FAVOURITE_TABLE_NAME = "favourite_table_info";
    private static final String FAVOURITE_COLUMN_PRIMARY_KEY = "favourite_primary_id";
    private static final String FAVOURITE_COLUMN_KEY = "favourite_key";
    private static final String FAVOURITE_COLUMN_IS_FAVOURITE = "favourite_is_favourite";

    private static CrowdFireDBHelper mDBHelper;
    public static CrowdFireDBHelper getInstance(Context context) {
        if (mDBHelper == null) {
            mDBHelper = new CrowdFireDBHelper(context);
        }

        return mDBHelper;
    }

    private CrowdFireDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        tableCreateStatements(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SHIRT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TROUSER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAVOURITE_TABLE_NAME);

        onCreate(db);
    }

    private void tableCreateStatements(SQLiteDatabase db) {
        createShirtTable(db);
        createTrouserTable(db);
        createFavouriteTable(db);
    }

    private void createShirtTable(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + SHIRT_TABLE_NAME + "("
                            + SHIRT_COLUMN_PRIMARY_KEY + " INTEGER PRIMARY KEY, "
                            + SHIRT_COLUMN_FILE_PATH + " VARCHAR(20) UNIQUE"
                            + ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTrouserTable(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + TROUSER_TABLE_NAME + "("
                            + TROUSER_COLUMN_PRIMARY_KEY + " INTEGER PRIMARY KEY, "
                            + TROUSER_COLUMN_FILE_PATH + " VARCHAR(20) UNIQUE"
                            + ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createFavouriteTable(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "CREATE TABLE IF NOT EXISTS "
                            + FAVOURITE_TABLE_NAME + "("
                            + FAVOURITE_COLUMN_PRIMARY_KEY + " INTEGER PRIMARY KEY, "
                            + FAVOURITE_COLUMN_KEY + " VARCHAR(20), "
                            + FAVOURITE_COLUMN_IS_FAVOURITE + " VARCHAR(20) "
                            + ")"
            );

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getShirtImagePathListFromDB() throws Resources.NotFoundException, NullPointerException {
        List<String> returnValue = new ArrayList<>();

        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM "
                            + SHIRT_TABLE_NAME,
                    new String[]{});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String imagePath = cursor.getString(cursor.getColumnIndex(SHIRT_COLUMN_FILE_PATH));
                    if (new File(imagePath).exists()) {
                        returnValue.add(imagePath);
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return returnValue;
    }

    public void insertShirtImagePathIntoDB(String filePath) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SHIRT_COLUMN_FILE_PATH, filePath);
            db.insert(SHIRT_TABLE_NAME, null, contentValues);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getTrouserImagePathListFromDB() throws Resources.NotFoundException, NullPointerException {
        List<String> returnValue = new ArrayList<>();

        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM "
                            + TROUSER_TABLE_NAME,
                    new String[]{});

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String imagePath = cursor.getString(cursor.getColumnIndex(TROUSER_COLUMN_FILE_PATH));
                    if (new File(imagePath).exists()) {
                        returnValue.add(imagePath);
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return returnValue;
    }

    public void insertTrouserImagePathIntoDB(String filePath) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TROUSER_COLUMN_FILE_PATH, filePath);
            db.insert(TROUSER_TABLE_NAME, null, contentValues);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertFavouriteCombination(String key, boolean isFavourite) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAVOURITE_COLUMN_KEY, key);
            contentValues.put(FAVOURITE_COLUMN_IS_FAVOURITE, isFavourite ? "yes" : "no");

            db.insert(FAVOURITE_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFavouriteCombination(String key, boolean isFavourite) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAVOURITE_COLUMN_KEY, key);
            contentValues.put(FAVOURITE_COLUMN_IS_FAVOURITE, isFavourite ? "yes" : "no");

            long x = db.update(FAVOURITE_TABLE_NAME,  contentValues, FAVOURITE_COLUMN_KEY + "=?", new String[]{key});
            System.out.println(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isFavouriteRecordExists(String key) {
        boolean returnValue = false;

        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM "
                            + FAVOURITE_TABLE_NAME
                            + " WHERE "
                            + FAVOURITE_COLUMN_KEY
                            + "= ?",
                    new String[]{key + ""});

            returnValue = cursor.getCount() > 0;

        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return returnValue;
    }

    public boolean isFavourite(String key) {
        boolean returnValue = false;

        Cursor cursor = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM "
                            + FAVOURITE_TABLE_NAME
                            + " WHERE "
                            + FAVOURITE_COLUMN_KEY
                            + "= ?",
                    new String[]{key + ""});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                returnValue = "yes".equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(FAVOURITE_COLUMN_IS_FAVOURITE)));
                System.out.println(returnValue);

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return returnValue;
    }
}
