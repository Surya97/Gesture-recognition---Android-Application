package com.example.gesturerecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;


public class Screen2 extends AppCompatActivity {


    private static VideoView gestureLearnVideo;
    private static MediaController gestureVideoController;
    private static String serverAddress = "http://192.168.0.23:8888/SignSavvyVideos";
    DownloadManager downloadManager;

    private long downloadID;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our
            // enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(Screen2.this,
                        "Download Completed", Toast.LENGTH_SHORT).show();
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

        try {
            handleVideoView(gesture_name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void handleVideoView(String gesture_name) throws InterruptedException {

        gestureLearnVideo = findViewById(R.id.gesture_tutorial_video);

        if (gestureVideoController == null) {
            gestureVideoController = new MediaController(Screen2.this);
            gestureVideoController.setAnchorView(gestureLearnVideo);
        }

        gestureLearnVideo.setMediaController(gestureVideoController);
        String[] gesture_name_split = gesture_name.toLowerCase().split(" ");

        String gesture_name_final = "";
        for (String str : gesture_name_split) {
            gesture_name_final += str;
            gesture_name_final += "_";
        }

        gesture_name_final = gesture_name_final.substring(0, gesture_name_final.length() - 1);

        final String GESTURE = gesture_name_final;

//        File gesture_video_file = new File("android.resource://" +
//        getPackageName() + "/raw/" + gesture_name_final);
        File gesture_video_file = new File(getExternalFilesDir(null),
                gesture_name_final + ".mp4");

        if (!gesture_video_file.exists()) {
            String downloadString = serverAddress + "/" + gesture_name_final + ".mp4";
            beginDownload(gesture_name_final);
        }
        gestureLearnVideo.setVideoURI(Uri.parse(gesture_video_file.toString()));
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

    private void beginDownload(String gestureName) {

        DownloadFile downloadFile = new DownloadFile();
        downloadFile.execute(gestureName);
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(Screen2.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... gestureName) {
            int count;
            try{
                URL gesture_video_url = new URL(serverAddress + "/"+ gestureName[0] + ".mp4");
                URLConnection connection = gesture_video_url.openConnection();
                connection.connect();

                //get file length
                int file_length = connection.getContentLength();
                Log.d("Content_length", String.valueOf(file_length));

                //start downloading the file

                InputStream input = new BufferedInputStream(gesture_video_url.openStream(),
                        8192);
                OutputStream output = new FileOutputStream(
                        getExternalFilesDir(null)
                        +"/"+gestureName[0]+".mp4");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / file_length));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            }catch(Exception e){
                Log.e("Download Error", e.getMessage());
            }
            return "Downloaded at: " + getExternalFilesDir(null) + "/"+gestureName[0]+".mp4";
        }
    }
}
