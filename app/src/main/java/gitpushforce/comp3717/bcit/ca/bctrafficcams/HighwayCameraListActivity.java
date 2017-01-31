package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;


public class HighwayCameraListActivity extends AppCompatActivity {
    public static final String TAG = HighwayCameraListActivity.class.getName();

    private ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_camera_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        returnButton = (ImageButton)findViewById(R.id.returnButton);
        Log.d(TAG, "onCreate end");
    }

    protected  void onStart()
    {   Log.d(TAG, "onStart begin");
        super.onStart();
        returnButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Log.d(TAG, "onStart end");
    }
    public void viewCamera(final View view)
    {
        Log.d(TAG, "viewCamera begin");
        final Intent intent;

        intent = new Intent(this, HighwayCameraViewActivity.class);
        startActivity(intent);
        Log.d(TAG, "viewCamera end");
    }
}
