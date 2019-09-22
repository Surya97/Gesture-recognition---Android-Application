package com.example.gesturerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class Screen2 extends AppCompatActivity {


    private static VideoView gestureLearnVideo;
    private static MediaController gestureVideoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        Intent gesture_intent = getIntent();
        String gesture_name = gesture_intent.getStringExtra(Screen1.ACTION_GESTURE_NAME);

        Toast.makeText(this, "Selected: " + gesture_name, Toast.LENGTH_LONG).show();

        gestureLearnVideo = (VideoView)findViewById(R.id.gesture_tutorial_video);

        if(gestureVideoController == null){
            gestureVideoController = new MediaController(Screen2.this);
            gestureVideoController.setAnchorView(gestureLearnVideo);
        }

        gestureLearnVideo.setMediaController(gestureVideoController);
        String[] gesture_name_split = gesture_name.toLowerCase().split(" ");

        String gesture_name_final = "";
        for(String str: gesture_name_split){
            gesture_name_final+=str;
            gesture_name_final+="_";
        }

        gesture_name_final = gesture_name_final.substring(0, gesture_name_final.length()-1);

        final String GESTURE = gesture_name_final;

        gestureLearnVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/" + gesture_name_final));
        // start a video
        gestureLearnVideo.start();

        gestureLearnVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(),
                        GESTURE, Toast.LENGTH_LONG).show();
            }
        });
        gestureLearnVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(),
                        "Oops An Error Occur While Playing Video...!!!",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }
}
