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

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        difficultySpinner.setAdapter(adapter);
//        difficultySpinner.setOnItemSelectedListener(this);

        mainLayout.setBackgroundColor(Color.WHITE);
        subtitle.setTextColor(Color.BLACK);
        title.setTextColor(Color.BLACK);
        userNameBox.setHintTextColor(Color.BLACK);
        userNameBox.setTextColor(Color.BLACK);

        if (userNameBox.getText().toString().matches("")) {
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
                    openQuizButton.setEnabled(false);
                } else {
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SettingActivity.SETTINGS_RESULT) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    lightMode = data.getBooleanExtra("lightMode", false);
                    if (lightMode) {
                        // set background white, text black
                        mainLayout.setBackgroundColor(Color.WHITE);
                        subtitle.setTextColor(Color.BLACK);
                        title.setTextColor(Color.BLACK);
                        userNameBox.setHintTextColor(Color.BLACK);
                        userNameBox.setTextColor(Color.BLACK);
                        adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, R.layout.spinner_color_light_mode);
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_light_mode);

                    } else {
                        //set background black, colours white
                        mainLayout.setBackgroundColor(Color.BLACK);
                        subtitle.setTextColor(Color.WHITE);
                        title.setTextColor(Color.WHITE);
                        userNameBox.setHintTextColor(Color.WHITE);
                        userNameBox.setTextColor(Color.WHITE);
                        adapter = ArrayAdapter.createFromResource(this, R.array.difficulties, R.layout.spinner_color_dark_mode);
                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_dark_mode);
                    }
                    difficultySpinner.setAdapter(adapter);
                    Log.i("Content view: ", String.valueOf(lightMode));
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}