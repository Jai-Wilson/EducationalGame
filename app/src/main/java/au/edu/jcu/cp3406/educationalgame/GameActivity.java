package au.edu.jcu.cp3406.educationalgame;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    Game game;
    Button startButton;
    Button checkButton;
    TextView questionsBox;
    TextView correctBox;
    TextView incorrectBox;
    EditText userInputBox;
    LinearLayout linearLayout;

    public int questionCounter;
    public Boolean isStart;
    public int correctCounter;
    public int incorrectCounter;
    public Boolean lightMode;
    public String userName;
    //initialise the sensor
    private SensorManager sensorManager;
    private Sensor shakeSensor;
    private float acelVal;
    private float shake;
    private int passingRate;
    public Boolean passed;
    public String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        lightMode = intent.getBooleanExtra("lightMode", true);
        userName = intent.getStringExtra("userName");
        int difficulty = intent.getIntExtra("difficulty", 0);

        game = new Game();
        startButton = findViewById(R.id.startButton);
        checkButton = findViewById(R.id.checkButton);
        questionsBox = findViewById(R.id.questionBox);
        userInputBox = findViewById(R.id.userInputBox);
        correctBox = findViewById(R.id.correctBox);
        incorrectBox = findViewById(R.id.incorrectBox);
        linearLayout = findViewById(R.id.tableLayout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shakeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);


        checkButton.setVisibility(View.INVISIBLE);
        questionsBox.setVisibility(View.INVISIBLE);

        isStart = true;
        questionCounter = 0;
        correctCounter = 0;
        incorrectCounter = 0;
        //configure the mode
        isLightorDark();
        passingRate = getPassRate(difficulty);
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float acelLast = acelVal;
            //calculate the acceleeation
            acelVal = (float) Math.sqrt(x * x + y * y + z * z);
            //find the change in acceleration
            float changeInAccel = acelVal - acelLast;
            shake = shake * 0.9f + changeInAccel;

            //detect if shake is strong enough
            if (shake > 10) {
                //shake detected
                userInputBox.setText("");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, shakeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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
            Log.i("GameActivity", String.format("Correct Answer is: %s", currentAnswer));
            Log.i("GameActivity", String.format("Raw input is: %s", userInputBox.getText().toString()));
            Log.i("GameActivity", String.format("User's answer: %s", userAnswer));
            Log.i("GameActivity", String.valueOf(userAnswer.equals(currentAnswer)));
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            ++correctCounter;
            correctBox.setText(String.format(Locale.getDefault(), "Correct : %d", correctCounter));
        } else {
            Log.i("GameActivity", String.format("Correct Answer is: %s", currentAnswer));
            Log.i("GameActivity", String.format("Raw input is: %s", userInputBox.getText().toString()));
            Log.i("GameActivity", String.format("User's answer: %s", userAnswer));
            Log.i("GameActivity", String.valueOf(userAnswer.equals(currentAnswer)));
            Toast.makeText(this, String.format("Incorrect, the correct answer is: %s", currentAnswer), Toast.LENGTH_SHORT).show();
            ++incorrectCounter;
            incorrectBox.setText(String.format(Locale.getDefault(), "Inorrect : %d", incorrectCounter));
        }
        ++questionCounter;
        if (questionCounter < 10) {
            userInputBox.setText("");
            String currentQuestion = game.getQuestion(questionCounter);
            questionsBox.setText(currentQuestion);
        } else {
            onGameFinished(correctCounter);
            passed = correctCounter >= passingRate;
            finish();
            Intent intent = new Intent(this, SocialNetworkingActivity.class);
            intent.putExtra("Score", correctCounter);
            intent.putExtra("userName", userName);
            intent.putExtra("passed", passed);
            intent.putExtra("level", level);
            startActivity(intent);
        }
    }

    public int getPassRate(int difficulty) {
        switch (difficulty) {
            case 0:
                passingRate = 5;
                level = "Easy";
                break;
            case 1:
                passingRate = 7;
                level = "Medium";
                break;
            case 2:
                passingRate = 8;
                level = "Hard";
                break;
            case 3:
                passingRate = 10;
                level = "Mastermind";
                break;
        }
        return passingRate;
    }

    public void isLightorDark() {
        if (lightMode) {
            correctBox.setTextColor(Color.BLACK);
            incorrectBox.setTextColor(Color.BLACK);
            questionsBox.setTextColor(Color.BLACK);
            userInputBox.setTextColor(Color.BLACK);
            userInputBox.setHintTextColor(Color.BLACK);
            linearLayout.setBackgroundColor(Color.WHITE);
        } else {
            correctBox.setTextColor(Color.WHITE);
            incorrectBox.setTextColor(Color.WHITE);
            questionsBox.setTextColor(Color.WHITE);
            userInputBox.setTextColor(Color.WHITE);
            userInputBox.setHintTextColor(Color.WHITE);
            linearLayout.setBackgroundColor(Color.BLACK);
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