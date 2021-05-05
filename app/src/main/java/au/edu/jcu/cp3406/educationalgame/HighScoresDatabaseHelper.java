package au.edu.jcu.cp3406.educationalgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighScoresDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "HighScores"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    HighScoresDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void insertHighScore(SQLiteDatabase db, String score) {
        ContentValues highScoreValues = new ContentValues();
        highScoreValues.put("SCORE", score);
        db.insert("HIGHSCORE", null, highScoreValues);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE HIGHSCORE (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "SCORE TEXT);");
            insertHighScore(db, "10");
        }
//        if (oldVersion < 2) {
//            db.execSQL("ALTER TABLE HIGHSCORE ADD COLUMN FAVORITE NUMERIC;");
//        }
    }
}
