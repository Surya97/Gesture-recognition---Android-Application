package com.example.gesturerecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Arrays;

public class Screen1 extends Activity implements AdapterView.OnItemSelectedListener {

    static final String ACTION_GESTURE_NAME = "Gesture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        Spinner gesture_spinner = (Spinner) findViewById(R.id.gesture_spinner);
        gesture_spinner.setOnItemSelectedListener(Screen1.this);

        ArrayAdapter<CharSequence> gestureDataAdapter = ArrayAdapter.createFromResource(
                this, R.array.gestures_list, android.R.layout.simple_spinner_item);

        gestureDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gesture_spinner.setAdapter(gestureDataAdapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        if(!item.equals("Select")) {
            Intent gestureVideoIntent = new Intent(Screen1.this, Screen2.class);
            gestureVideoIntent.putExtra(this.ACTION_GESTURE_NAME, item);
            gestureVideoIntent.setType("text/plain");
            if (gestureVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(gestureVideoIntent);
            } else {
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
