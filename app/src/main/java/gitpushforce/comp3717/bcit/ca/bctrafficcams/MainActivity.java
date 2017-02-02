package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Log.d(TAG, "onCreate end");

    }

    public void viewCamerasList(final View view)
    {
        Log.d(TAG, "viewCamerasList begin");
        final Intent intent;

        intent = new Intent(this, HighwayCameraListActivity.class);
        startActivity(intent);
        //finish();
        Log.d(TAG, "viewCameraList end");
    }

    public void goOwnRoutesCreate(final View view)
    {
        Log.d(TAG, "goOwnRoutesCreate begin");
        final Intent intent;

        intent = new Intent(this, OwnRoutesCreationActivity.class);
        startActivity(intent);
        Log.d(TAG, "goOwnRoutesCreate end");
    }


    public void processCaches(final View view) {
        Log.d(TAG, "processCaches begin");
        //NewWestDataset data = new NewWestDataset("webcam-links", getApplicationContext());
        //data.execute("WEBCAM_LINKS.csv");

        String name = "WEBCAM_LINKS.csv";

        AssetManager assetManager = getResources().getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(name);
            if ( inputStream != null)
                Log.d(TAG, "It worked!");

            InputStreamReader read = new InputStreamReader(inputStream);

            StringBuilder firstChars = new StringBuilder();

            int ch = read.read();

            int i = 0;
            while (ch != -1 && firstChars.length() < 30) {
                i++;
                Log.d(TAG, "got "+(char)ch);
                firstChars.append((char)ch);
                ch = read.read();

            }

            Log.d(TAG, "got "+i + "chars: "+firstChars);

            read.close();
            inputStream.close();

            Toast.makeText(this.getApplicationContext(), name + ": " + firstChars + "...", Toast.LENGTH_LONG).show();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng start = new LatLng(49.2189, -122.9177);
        mMap.addMarker(new MarkerOptions().position(start).title("New West"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));

    }



}
