package au.edu.jcu.cp3406.educationalgame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class SocialNetworkingActivity extends AppCompatActivity {
    public static int SOCIAL_RESULT = 2;

    public boolean finished;
    TextView userScore;
    private Twitter twitter = TwitterFactory.getSingleton();
    public int score;
    private User user;
    Button sendTweetbutton;
    Button declineTweetButton;
    public String userName;
    public Boolean passed;
    public String level;
    public String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_networking);

        finished = false;

        sendTweetbutton = findViewById(R.id.tweetButton);
        declineTweetButton = findViewById(R.id.noTweet);

        Intent intent = getIntent();
        score = intent.getIntExtra("Score", 0);
        userName = intent.getStringExtra("userName");
        passed = intent.getBooleanExtra("passed", true);
        level = intent.getStringExtra("level");

        userScore = findViewById(R.id.userScoreDisplay);
        if (passed) {
            //add difficulty
            message = String.format(Locale.getDefault(), "%s just scored %d on the chemystery quiz on the %s difficulty mode! Well done %s! #learningisfun", userName, score, level, userName);
            userScore.setText(message);
        } else {
            message = String.format(Locale.getDefault(), "%s just scored %d on the chemystery quiz on the %s difficulty mode! Nearly there %s! #keepgoing", userName, score, level, userName);
            userScore.setText(message);
        }
    }

    public void sendTweetClicked(View view) {
        Background.run(new Runnable() {
            @Override
            public void run() {
                if (isAuthorised()) {
                    try {
                        twitter.updateStatus(message);
                    } catch (TwitterException ignored) {

                    }
                }

            }
        });
        userScore.setText("Success!");
        sendTweetbutton.setEnabled(false);
        declineTweetButton.setText("Return to main menu");
    }

    private boolean isAuthorised() {
        try {
            user = twitter.verifyCredentials();
            Log.i("MainActivity", "verified");
            return true;
        } catch (Exception e) {
            Log.i("MainActivity", "not verified");
            return false;
        }
    }


    public void declineTweet(View view) {
        finished = true;
        Intent intent = new Intent();
        intent.putExtra("finished", finished);
        setResult(RESULT_OK, intent);
        finish();
    }
}