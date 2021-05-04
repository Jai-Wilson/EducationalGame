package au.edu.jcu.cp3406.educationalgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HighScoresActivity extends AppCompatActivity {
    LinearLayout highScoresLayout;
    TextView highScoresLabel;
    ListView highScoresList;

    public Boolean lightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        Intent intent = getIntent();
        lightMode = intent.getBooleanExtra("lightMode", true);
        highScoresLayout = (LinearLayout) findViewById(R.id.highScoresLayout);
        highScoresLabel = findViewById(R.id.highScoresLabel);
        highScoresList = findViewById(R.id.highScoresList);
        isLightorDark();
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