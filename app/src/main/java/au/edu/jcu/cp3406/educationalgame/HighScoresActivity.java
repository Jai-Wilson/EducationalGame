package au.edu.jcu.cp3406.educationalgame;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HighScoresActivity extends AppCompatActivity {
    LinearLayout highScoresLayout;
    TextView highScoresLabel;
    ListView highScoresList;
    public Boolean lightMode;
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        Intent intent = getIntent();
        lightMode = intent.getBooleanExtra("lightMode", true);
        highScoresLayout = (LinearLayout) findViewById(R.id.highScoresLayout);
        highScoresLabel = findViewById(R.id.highScoresLabel);
        highScoresList = (ListView) findViewById(R.id.highScoresList);
        isLightorDark();

        SQLiteOpenHelper highScoresDatabaseHelper = new HighScoresDatabaseHelper(this);
        try {
            db = highScoresDatabaseHelper.getReadableDatabase();
            cursor = db.query("HIGHSCORE", null, null, null, null, null, "SCORE DESC", null);
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(HighScoresActivity.this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"SCORE"},
                    new int[]{android.R.id.text1},
                    0);
            highScoresList.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void isLightorDark() {
        if (lightMode) {
            highScoresLayout.setBackgroundColor(Color.WHITE);
            //set color of list?
            highScoresLabel.setTextColor(Color.BLACK);
        } else {
            highScoresLayout.setBackgroundColor(Color.BLACK);
            //set color of list?
            highScoresLabel.setTextColor(Color.WHITE);
        }
    }
}