package com.example.gesturerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Screen2 extends AppCompatActivity {


    private static VideoView gestureLearnVideo;
    private static MediaController gestureVideoController;
    private static String serverAddress = "localhost:8888";

    private long downloadID;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(Screen2.this, "Download Completed", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);

        Intent gesture_intent = getIntent();
        String gesture_name = gesture_intent.getStringExtra(Screen1.ACTION_GESTURE_NAME);

        Toast.makeText(this, "Selected: " + gesture_name, Toast.LENGTH_LONG).show();

        handleVideoView(gesture_name);
    }


    public void handleVideoView(String gesture_name){

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

        File gesture_video_file = new File("android.resource://" + getPackageName() + "/raw/" + gesture_name_final);
        if(gesture_video_file.exists()){
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
        }else{
            String downloadString = serverAddress + "/" + gesture_name_final + ".mp4";
            beginDownload(gesture_name_final);
        }

    }

    private void beginDownload(String gestureName){

        File file = new File(getApplicationContext().getAssets(), gestureName + ".mp4");
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(serverAddress + "/" + gestureName+".mp4"))
                .setTitle(gestureName)
                .setDescription("Downloading " + gestureName +".mp4")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file));


    }
}
