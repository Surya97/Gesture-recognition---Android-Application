package com.example.gesturerecognition;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Arrays;

public class Screen1 extends Activity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        Spinner gesture_spinner = (Spinner) findViewById(R.id.gesture_spinner);
        gesture_spinner.setOnItemSelectedListener(this);

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
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
