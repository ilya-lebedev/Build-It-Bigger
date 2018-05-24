package io.github.ilya_lebedev.displayjoke;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * JokeActivity
 */
public class JokeActivity extends AppCompatActivity {

    public static final String EXTRA_JOKE = "joke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        Intent intent = getIntent();
        String joke;
        if (intent.hasExtra(EXTRA_JOKE)) {
            joke = intent.getStringExtra(EXTRA_JOKE);
            if (TextUtils.isEmpty(joke)) {
                throw new IllegalArgumentException("Joke string cannot be null or empty");
            }
        } else {
            throw new IllegalArgumentException("Intent must contain extra with joke string");
        }

        // Find joke view
        TextView jokeTv = findViewById(R.id.tv_joke);

        // Set joke to the joke view
        jokeTv.setText(joke);
    }

    public static Intent generateIntent(Context context, String joke) {
        Intent intent = new Intent(context, JokeActivity.class);
        intent.putExtra(EXTRA_JOKE, joke);

        return intent;
    }

}
