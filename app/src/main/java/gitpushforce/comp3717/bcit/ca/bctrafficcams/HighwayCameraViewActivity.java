package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;

public class HighwayCameraViewActivity extends AppCompatActivity {

    public static final String TAG = HighwayCameraViewActivity.class.getName();
    // private ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_camera_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //returnButton = (ImageButton)findViewById(R.id.returnButton);
        Log.d(TAG, "onCreate end");
    }

    @Override
    protected void onStart()
    {
        Log.d(TAG, "onStart begin");
        super.onStart();
        // returnButton.setOnClickListener(new View.OnClickListener() {
         //   public void onClick(View v) {
         //       finish();
         //   }
        //});
        Log.d(TAG, "onStart end");
    }
}
