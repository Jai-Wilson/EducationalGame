package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class SocialNetworkingActivity extends AppCompatActivity {

    TextView userScore;
    private Twitter twitter = TwitterFactory.getSingleton();
    public int score;
    private User user;
    Button sendTweetbutton;
    Button declineTweetButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_networking);

        sendTweetbutton = findViewById(R.id.tweetButton);
        declineTweetButton = findViewById(R.id.noTweet);

        Intent intent = getIntent();
        score = intent.getIntExtra("Score", 0);

        userScore = findViewById(R.id.userScoreDisplay);
        userScore.setText(String.format("Just scored %d on the chemystery quiz! #learningisfun", score));
    }


    public void sendTweetClicked(View view) {
        Background.run(new Runnable() {
            @Override
            public void run() {
                if (isAuthorised()) {

                    try {
                        twitter.updateStatus(String.format("Just scored %d on the chemystery quiz! #learningisfun", score));
                    } catch (TwitterException ignored) {

                    }
                }

            }
        });

        userScore.setText("Success!");


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
        super.onBackPressed();
    }
}