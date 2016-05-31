package edu.calpoly.ndietz.brewbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by ndietz on 4/17/16.
 */
public class BrewDbHelper extends SQLiteOpenHelper {
    private static BrewDbHelper sInstance;
    private static SQLiteDatabase sWritableDb;

    private static final String DB_HELPER_TAG = "BrewDbHelper";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "BrewHistory.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BrewHistoryContract.BrewEntry.TABLE_NAME + " (" +
                    BrewHistoryContract.BrewEntry._ID + " INTEGER PRIMARY KEY," +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_COFFEE + TEXT_TYPE + COMMA_SEP +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_GRIND + TEXT_TYPE + COMMA_SEP +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_WATER + TEXT_TYPE + COMMA_SEP +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_TOTAL_SECONDS + TEXT_TYPE + COMMA_SEP +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_BREW_METHOD + TEXT_TYPE + COMMA_SEP +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_ACIDITY + TEXT_TYPE + COMMA_SEP +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_BODY + TEXT_TYPE + COMMA_SEP +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_FLAVOR + TEXT_TYPE + COMMA_SEP +
                    BrewHistoryContract.BrewEntry.COLUMN_NAME_OVERALL + TEXT_TYPE +

            " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BrewHistoryContract.BrewEntry.TABLE_NAME;

    /**
     * Singleton pattern for SQLiteOpenHelper
     * @param context calling activity's context
     * @return the singleton instance of our BrewDbHelper
     */
    public static BrewDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new BrewDbHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    // docs mentioned creation/getting tables should not be in main thread
    // TODO investigate necessity of initializing DB/writable tables in a thread
    private BrewDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void close() {
        super.close();
        if (sWritableDb != null) {
            sWritableDb.close();
            sWritableDb = null;
        }
    }

    public void addBrewEntry(Recipe recipe, Review rev) {
        if (sWritableDb == null) {
            sWritableDb = sInstance.getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_COFFEE, recipe.getM_gramsCoffee());
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_GRIND, recipe.getM_coarseness());
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_WATER, recipe.getM_gramsWater());
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_TOTAL_SECONDS, recipe.getM_totalTimeSeconds());
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_BREW_METHOD, recipe.getM_brewMethod().getM_method_name());
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_ACIDITY, rev.getM_acidity());
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_BODY, rev.getM_body());
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_FLAVOR, rev.getM_flavor());
        values.put(BrewHistoryContract.BrewEntry.COLUMN_NAME_OVERALL, rev.getM_overall());

        sWritableDb.insert(BrewHistoryContract.BrewEntry.TABLE_NAME, "null", values);

        dumpContentsToLog();
    }

    public ArrayList<BrewHistoryEntry> getHistory() {
        if (sWritableDb == null) {
            sWritableDb = sInstance.getWritableDatabase();
        }

        ArrayList<BrewHistoryEntry> list = new ArrayList<>();

        Cursor cursor = sWritableDb.rawQuery("SELECT * FROM " +
                BrewHistoryContract.BrewEntry.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                list.add(buildEntryObject(cursor));

            } while(cursor.moveToNext());
        }

        Log.d(DB_HELPER_TAG, "len before return: "+list.size());
        return list;
    }

    private BrewHistoryEntry buildEntryObject(Cursor cursor) {
        Recipe recipe = new Recipe();
        Review review = new Review();

        recipe.setM_coarseness(1);
        recipe.setM_gramsWater(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BrewHistoryContract.BrewEntry.COLUMN_NAME_WATER))));
        recipe.setM_gramsCoffee(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BrewHistoryContract.BrewEntry.COLUMN_NAME_COFFEE))));
        recipe.setM_totalTimeSeconds(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BrewHistoryContract.BrewEntry.COLUMN_NAME_TOTAL_SECONDS))));
        recipe.setTimesFromTotal();
        recipe.setM_brewMethod(new BrewMethod(cursor.getString(cursor.getColumnIndex(BrewHistoryContract.BrewEntry.COLUMN_NAME_BREW_METHOD))));

        review.setM_flavor(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BrewHistoryContract.BrewEntry.COLUMN_NAME_FLAVOR))));
        review.setM_body(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BrewHistoryContract.BrewEntry.COLUMN_NAME_OVERALL))));
        review.setM_acidity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BrewHistoryContract.BrewEntry.COLUMN_NAME_ACIDITY))));
        review.setM_overall(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BrewHistoryContract.BrewEntry.COLUMN_NAME_OVERALL))));

        return new BrewHistoryEntry(recipe, review);
    }

    public void dumpContentsToLog() {
        Log.d(this.DB_HELPER_TAG, "==== Dump DB contents ====");

        if (sWritableDb == null) {
            sWritableDb = sInstance.getWritableDatabase();
        }

        // sourced from http://stackoverflow.com/questions/27003486/printing-all-rows-of-a-sqlite-database-in-android
        // for demoing database contents
        Cursor cursor = sWritableDb.rawQuery("SELECT * FROM " +
                BrewHistoryContract.BrewEntry.TABLE_NAME, null);
        String cursorString = "";
        if (cursor.moveToFirst() ){
            String[] columnNames = cursor.getColumnNames();
            for (String name: columnNames)
                cursorString += String.format("%s ][ ", name);
            cursorString += "\n";
            do {
                for (String name: columnNames) {
                    cursorString += String.format("%s ][ ",
                            cursor.getString(cursor.getColumnIndex(name)));
                }
                cursorString += "\n";
            } while (cursor.moveToNext());
        }

        Log.d(this.DB_HELPER_TAG, cursorString);
    }
}
