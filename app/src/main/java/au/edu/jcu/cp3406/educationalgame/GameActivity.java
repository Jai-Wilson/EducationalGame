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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private static final int shakeCalibrate = 800;

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
    public String userName;
    //.initialise the sensor
    private SensorManager sensorManager;
    private Sensor shakeSensor;
    private float acelVal;
    private float acelLast;
    private float shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        lightMode = intent.getBooleanExtra("lightMode", true);
        userName = intent.getStringExtra("userName");

        game = new Game();
        startButton = findViewById(R.id.startButton);
        checkButton = findViewById(R.id.checkButton);
        questionsBox = findViewById(R.id.questionBox);
        userInputBox = findViewById(R.id.userInputBox);
        bottomImage = findViewById(R.id.bottomImage);
        correctBox = findViewById(R.id.correctBox);
        incorrectBox = findViewById(R.id.incorrectBox);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

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
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt(x * x + y * y + z * z);
            float changeInAccel = acelVal - acelLast;
            shake = shake * 0.9f + changeInAccel;

            if (shake > 7) {
                //shake detected
                Toast toast = Toast.makeText(getApplicationContext(), "Shake Detected", Toast.LENGTH_SHORT);
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
            Intent intent = new Intent(this, SocialNetworkingActivity.class);
            intent.putExtra("Score", correctCounter);
            intent.putExtra("userName", userName);
            startActivity(intent);
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