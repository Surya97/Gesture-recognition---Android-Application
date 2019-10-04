package com.example.gesturerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Screen3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3);

        Intent gesturePracticeIntent = getIntent();
        String gestureName = gesturePracticeIntent.getStringExtra(Screen2.GESTURE_PRACTICE_MESSAGE);

        Toast.makeText(Screen3.this,
                "Practice Gesture " + gestureName, Toast.LENGTH_LONG).show();
    }
}
