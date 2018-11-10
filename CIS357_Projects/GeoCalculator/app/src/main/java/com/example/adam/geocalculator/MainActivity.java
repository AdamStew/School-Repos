package com.example.adam.geocalculator;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @Authors : Adam Stewart & Kristian Trevino
 */
public class MainActivity extends AppCompatActivity {

    public static final int SETTINGS_SELECTION = 1;
    private TextView distanceResult;
    private TextView bearingResult;
    private Button calcButton;
    private Button clearButton;
    private EditText latp1;
    private EditText longp1;
    private EditText latp2;
    private EditText longp2;
    private String distUnits = "Kilometers";
    private String bearUnits = "Degrees";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent,SETTINGS_SELECTION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latp1 = (EditText) findViewById(R.id.latp1);
        longp1 = (EditText) findViewById(R.id.longp1);
        latp2 = (EditText) findViewById(R.id.latp2);
        longp2 = (EditText) findViewById(R.id.longp2);

        calcButton = (Button) findViewById(R.id.calcButton);
        clearButton = (Button) findViewById(R.id.clearButton);

        distanceResult = (TextView) findViewById(R.id.distResult);
        bearingResult = (TextView) findViewById(R.id.bearResult);



        calcButton.setOnClickListener(v -> {
            Double latPoint1 = Double.parseDouble(latp1.getText().toString());
            Double longPoint1 = Double.parseDouble(longp1.getText().toString());
            Double latPoint2 =  Double.parseDouble(latp2.getText().toString());
            Double longPoint2 = Double.parseDouble(longp2.getText().toString());
            Location pair1 = new Location("pair1");
            pair1.setLatitude(latPoint1);
            pair1.setLongitude(longPoint1);
            Location pair2 = new Location("pair2");
            pair2.setLatitude(latPoint2);
            pair2.setLongitude(longPoint2);
            double distance = Math.round((pair1.distanceTo(pair2)/1000.00)*100.00)/100.00;
            if(distUnits.equals("Kilometers")) {
                distanceResult.setText(distance + " Kilometers");
            }else if(distUnits.equals("Miles")){
                distance = distance * 0.621371;
                distance = Math.round((distance*100.00)/100.00);
                distanceResult.setText(distance + " Miles");
            }
            double bearing = Math.round(pair1.bearingTo(pair2)*100.00/100.00);
            if(bearUnits.equals("Degrees")) {
                bearingResult.setText(bearing + " Degrees");
            }else if(bearUnits.equals("Mils")) {
                bearing = bearing * 17.777777777778;
                bearing = Math.round((bearing * 100.00) / 100.00);
                bearingResult.setText(bearing + " Mils");
            }
        });

        clearButton.setOnClickListener(v -> {
            latp1.setText("");
            longp1.setText("");
            latp2.setText("");
            longp2.setText("");
            distanceResult.setText("");
            bearingResult.setText("");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SETTINGS_SELECTION) {
            distUnits = data.getStringExtra("distance");
            bearUnits = data.getStringExtra("bearing");
            calcButton.performClick();
        }
    }
}
