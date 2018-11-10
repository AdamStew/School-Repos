package com.example.adam.geocalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * @Authors : Adam Stewart & Kristian Trevino
 */
public class SettingsActivity extends AppCompatActivity {

    private String distanceSelection = "Kilometers";
    private String bearingSelection = "Degrees";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner distanceUnitsSpinner = (Spinner) findViewById(R.id.distanceUnitsSpinner);
        ArrayAdapter<CharSequence> distanceAdapter = ArrayAdapter.createFromResource(this,
                R.array.distanceUnits, android.R.layout.simple_spinner_item);

        distanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distanceUnitsSpinner.setAdapter(distanceAdapter);
        distanceUnitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                distanceSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Spinner bearingUnitsSpinner = (Spinner) findViewById(R.id.bearingUnitsSpinner);
        ArrayAdapter<CharSequence> bearingAdapter = ArrayAdapter.createFromResource(this,
                R.array.bearingUnits, android.R.layout.simple_spinner_item);

        bearingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bearingUnitsSpinner.setAdapter(bearingAdapter);
        bearingUnitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bearingSelection = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent();
                intent.putExtra("distance", distanceSelection);
                intent.putExtra("bearing", bearingSelection);
                setResult(MainActivity.SETTINGS_SELECTION,intent);
                finish();
            }
        });
    }

}
