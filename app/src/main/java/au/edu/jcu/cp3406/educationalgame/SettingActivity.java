package au.edu.jcu.cp3406.educationalgame;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    public static int SETTINGS_RESULT = 1;

    RadioGroup radioGroup;
    RadioButton radioButtonOne;
    RadioButton radioButtonTwo;
    LinearLayout settingsLayout;
    TextView settingsLabel;

    public Boolean lightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();
        lightMode = intent.getBooleanExtra("lightMode", true);
        Log.i("Content view: ", String.valueOf(lightMode));

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonOne = findViewById(R.id.radioOne);
        radioButtonTwo = findViewById(R.id.radioTwo);
        settingsLabel = findViewById(R.id.settingsLabel);
        settingsLayout = findViewById(R.id.settingsLayout);

        radioButtonOne.setChecked(!lightMode);
        radioButtonTwo.setChecked(lightMode);

        isLightOrDark();
    }


    public void radioApply(View view) {
        isLightOrDark();
        Intent intent = new Intent();
        intent.putExtra("lightMode", lightMode);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("lightMode", lightMode);
        setResult(SETTINGS_RESULT, intent);
        finish();
    }

    public void isLightOrDark() {
        if (radioButtonOne.isChecked()) {
            settingsLayout.setBackgroundColor(Color.BLACK);
            radioButtonOne.setTextColor(Color.WHITE);
            radioButtonTwo.setTextColor(Color.WHITE);
            settingsLabel.setTextColor(Color.WHITE);
            lightMode = false;
        } else {
            settingsLayout.setBackgroundColor(Color.WHITE);
            radioButtonOne.setTextColor(Color.BLACK);
            radioButtonTwo.setTextColor(Color.BLACK);
            settingsLabel.setTextColor(Color.BLACK);
            lightMode = true;
        }
    }
}