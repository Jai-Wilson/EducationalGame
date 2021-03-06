package au.edu.jcu.cp3406.educationalgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button openQuizButton;
    Button highScoresButton;
    ImageButton settingsButton;
    public Boolean lightMode;
    LinearLayout mainLayout;
    TextView subtitle;
    TextView title;
    EditText userNameBox;
    public String userName;
    public Spinner difficultySpinner;

    public ArrayAdapter adapter;
    public int difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lightMode = true;
        openQuizButton = findViewById(R.id.openQuizButton);
        highScoresButton = findViewById(R.id.highScoresButton);
        settingsButton = findViewById(R.id.settingsButton);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        subtitle = findViewById(R.id.subtitle);
        title = findViewById(R.id.title);
        userNameBox = findViewById(R.id.userNameBox);

        //initialise the spinner
        difficultySpinner = findViewById(R.id.spinner1);
        //use array adapter for spinner selections
        adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, R.layout.spinner_color_light_mode);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_light_mode);
        difficultySpinner.setAdapter(adapter);
        difficultySpinner.setBackgroundColor(Color.WHITE);
        difficultySpinner.setOnItemSelectedListener(this);

        //initially, set the application in light mode
        mainLayout.setBackgroundColor(Color.WHITE);
        subtitle.setTextColor(Color.BLACK);
        title.setTextColor(Color.BLACK);
        userNameBox.setHintTextColor(Color.BLACK);
        userNameBox.setTextColor(Color.BLACK);

        if (userNameBox.getText().toString().matches("")) {
            // if no text is entered into the username box, disable the start quiz button
            openQuizButton.setEnabled(false);
        }

        userNameBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userNameBox.getText().toString().matches("")) {
                    // if no text is entered into the username box, disable the start quiz button
                    openQuizButton.setEnabled(false);
                } else {
                    // once text is added, the user can start the game
                    userName = userNameBox.getText().toString();
                    openQuizButton.setEnabled(true);
                }
            }
        });
    }

    public void openQuizClicked(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("lightMode", lightMode);
        intent.putExtra("userName", userName);
        intent.putExtra("difficulty", difficulty);
        // send all relevant values to the GameActivity, being the liughtmode, the username and the
        // selected difficulty
        startActivityForResult(intent, SettingActivity.SETTINGS_RESULT);
    }

    public void settingsButtonClicked(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra("lightMode", lightMode);
        startActivityForResult(intent, SettingActivity.SETTINGS_RESULT);
    }

    public void highSCoresButtonClicked(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        intent.putExtra("lightMode", lightMode);
        startActivityForResult(intent, SettingActivity.SETTINGS_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // when returning from the settings screen, retrieve the result
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SettingActivity.SETTINGS_RESULT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    lightMode = data.getBooleanExtra("lightMode", false);
                    if (lightMode) {
                        // set background white, text black if light mode
                        mainLayout.setBackgroundColor(Color.WHITE);
                        subtitle.setTextColor(Color.BLACK);
                        title.setTextColor(Color.BLACK);
                        userNameBox.setHintTextColor(Color.BLACK);
                        userNameBox.setTextColor(Color.BLACK);
                        adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, R.layout.spinner_color_light_mode);
                        //change view of the spinner respectively
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_light_mode);

                    } else {
                        //set background black, colours white if dark mode
                        mainLayout.setBackgroundColor(Color.BLACK);
                        subtitle.setTextColor(Color.WHITE);
                        title.setTextColor(Color.WHITE);
                        userNameBox.setHintTextColor(Color.WHITE);
                        userNameBox.setTextColor(Color.WHITE);
                        adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, R.layout.spinner_color_dark_mode);
                        //change view of the spinner respectively
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_dark_mode);
                    }
                    difficultySpinner.setAdapter(adapter);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // when a new spinner selection is made, display selection with toast message
        String choice = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(), choice, Toast.LENGTH_SHORT).show();
        difficulty = difficultySpinner.getSelectedItemPosition();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}