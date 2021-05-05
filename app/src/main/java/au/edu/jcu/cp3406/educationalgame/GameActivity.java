package au.edu.jcu.cp3406.educationalgame;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    public static final String EXTRA_SCOREID = "scoreId";

    Game game;
    Button startButton;
    Button pauseButton;
    Button restartButton;
    Button checkButton;
    TextView questionsBox;
    TextView correctBox;
    TextView incorrectBox;
    EditText userInputBox;
    ImageView bottomImage;
    TableLayout tableLayout;


    public int questionCounter;
    public Boolean isStart;
    public int correctCounter;
    public int incorrectCounter;
    public Boolean lightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        lightMode = intent.getBooleanExtra("lightMode", true);

        game = new Game();
        startButton = findViewById(R.id.startButton);
        checkButton = findViewById(R.id.checkButton);
        questionsBox = findViewById(R.id.questionBox);
        userInputBox = findViewById(R.id.userInputBox);
        bottomImage = findViewById(R.id.bottomImage);
        correctBox = findViewById(R.id.correctBox);
        incorrectBox = findViewById(R.id.incorrectBox);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        checkButton.setVisibility(View.INVISIBLE);
        questionsBox.setVisibility(View.INVISIBLE);

        isStart = true;
        questionCounter = 0;
        correctCounter = 0;
        incorrectCounter = 0;
        //configure the mode
        isLightorDark();
    }


    public void startQuiz(View view) {
        if (isStart) {
            startButton.setText("Pause");
            checkButton.setVisibility(View.VISIBLE);
            questionsBox.setVisibility(View.VISIBLE);
            String currentQuestion = game.getQuestion(questionCounter);
            questionsBox.setText(currentQuestion);
            isStart = false;
        } else {
            startButton.setText("Start");
            questionsBox.setVisibility(View.INVISIBLE);
            checkButton.setVisibility(View.INVISIBLE);
            isStart = true;
        }
    }

    public void checkQuestion(View view) {
        String userAnswer = userInputBox.getText().toString().toLowerCase().trim();
        String currentAnswer = game.getAnswer(questionCounter).toLowerCase();
        if (userAnswer.equals(currentAnswer)) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            ++correctCounter;
            correctBox.setText(String.format("Correct : %d", correctCounter));
        } else {
            Toast.makeText(this, String.format("Incorrect, the correct answer is: %s", currentAnswer), Toast.LENGTH_SHORT).show();
            ++incorrectCounter;
            incorrectBox.setText(String.format("Inorrect : %d", incorrectCounter));
        }
        ++questionCounter;
        if (questionCounter < 10) {
            userInputBox.setText("");
            String currentQuestion = game.getQuestion(questionCounter);
            questionsBox.setText(currentQuestion);
        } else {
            onGameFinished(correctCounter);
            super.onBackPressed();
        }
    }

    public void isLightorDark() {
        if (lightMode) {
            correctBox.setTextColor(Color.BLACK);
            incorrectBox.setTextColor(Color.BLACK);
            questionsBox.setTextColor(Color.BLACK);
            userInputBox.setTextColor(Color.BLACK);
            userInputBox.setHintTextColor(Color.BLACK);
            tableLayout.setBackgroundColor(Color.WHITE);
        } else {
            correctBox.setTextColor(Color.WHITE);
            incorrectBox.setTextColor(Color.WHITE);
            questionsBox.setTextColor(Color.WHITE);
            userInputBox.setTextColor(Color.WHITE);
            userInputBox.setHintTextColor(Color.WHITE);
            tableLayout.setBackgroundColor(Color.BLACK);
        }
    }

    //Update the database when the game is finished
    public void onGameFinished(int correctCounter) {
        new UpdateHighScoresDatabaseTask().execute(correctCounter);
    }

    private class UpdateHighScoresDatabaseTask extends AsyncTask<Integer, Void, Boolean> {
        private ContentValues scoreValues;

        protected void onPreExecute() {
            scoreValues = new ContentValues();
            scoreValues.put("SCORE", correctCounter);
        }

        protected Boolean doInBackground(Integer... scores) {
            int scoreValue = scores[0];
            SQLiteOpenHelper highScoresDatabaseHelper =
                    new HighScoresDatabaseHelper(GameActivity.this);
            try {
                SQLiteDatabase db = highScoresDatabaseHelper.getWritableDatabase();
                db.insert("HIGHSCORE", null, scoreValues);
                db.close();
                return true;
            } catch (SQLiteException e) {
                return false;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (!success) {
                Toast toast = Toast.makeText(GameActivity.this,
                        "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


}