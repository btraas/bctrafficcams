package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

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

}
