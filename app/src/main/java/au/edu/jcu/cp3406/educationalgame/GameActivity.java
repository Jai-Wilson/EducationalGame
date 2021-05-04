package au.edu.jcu.cp3406.educationalgame;

import android.content.Intent;
import android.graphics.Color;
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

    public String[] questions =
            {"Chemical formula for Sodium Chloride?",
                    "How many oxygen atoms in a moilecule of water?",
                    "Atomic number of Titanium?",
                    "Chemical responsible 'spiciness' of a chilli?",
                    "What element has the symbol Hg?",
                    "Which atom has the highest atomic number?",
                    "Only letter that does not appear in the periodic table?",
                    "What is the lightest element?",
                    "What colour are copper crystals?",
                    "What colour does Tungsten burn?"};

    public String[] answers =
            {"NaCl",
                    "1",
                    "22",
                    "capsaicin",
                    "Mercury",
                    "Oganesson",
                    "j",
                    "Hydrogen",
                    "Blue",
                    "Green"};

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

        startButton = findViewById(R.id.startButton);
//        pauseButton = findViewById(R.id.pauseButton);
//        restartButton = findViewById(R.id.restartButton);
        checkButton = findViewById(R.id.checkButton);
        questionsBox = findViewById(R.id.questionBox);
        userInputBox = findViewById(R.id.userInputBox);
        bottomImage = findViewById(R.id.bottomImage);
        correctBox = findViewById(R.id.correctBox);
        incorrectBox = findViewById(R.id.incorrectBox);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);

//        restartButton.setVisibility(View.INVISIBLE);
//        pauseButton.setVisibility(View.INVISIBLE);
        checkButton.setVisibility(View.INVISIBLE);
        questionsBox.setVisibility(View.INVISIBLE);


        isStart = true;
        questionCounter = 0;
        correctCounter = 0;
        incorrectCounter = 0;

        isLightorDark();


//        ActionBar actionbar = getSupportActionBar();
//        assert actionbar != null;
//        getActionBar().setDisplayHomeAsUpEnabled(true);


    }


    public void startQuiz(View view) {

        if (isStart) {
            //        restartButton.setVisibility(View.VISIBLE);
            //        pauseButton.setVisibility(View.VISIBLE);
            startButton.setText("Pause");
            checkButton.setVisibility(View.VISIBLE);
            questionsBox.setVisibility(View.VISIBLE);
            String currentQuestion = questions[questionCounter];
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
        String currentAnswer = answers[questionCounter].toLowerCase();
        if (userAnswer.equals(currentAnswer)) {
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
            ++correctCounter;
            correctBox.setText(String.format("Correct :%d", correctCounter));
        } else {
            Toast.makeText(this, String.format("Incorrect, the correct answer is: %s", answers[questionCounter]), Toast.LENGTH_LONG).show();
            ++incorrectCounter;
            incorrectBox.setText(String.format("Inorrect :%d", incorrectCounter));
        }
        ++questionCounter;
        if (questionCounter < 10) {
            userInputBox.setText("");
            String currentQuestion = questions[questionCounter];
            questionsBox.setText(currentQuestion);
        } else {
            onBackPressed();
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

}