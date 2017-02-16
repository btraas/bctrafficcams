package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class OwnRoutesCreationActivity extends AppCompatActivity {

    public static final String TAG = OwnRoutesCreationActivity.class.getName();
   // private ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_routes_creation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // returnButton = (ImageButton)findViewById(R.id.returnButton);
        Log.d(TAG, "onCreate end");
    }

    @Override
    protected void onStart()
    {
        Log.d(TAG, "onStart begin");
        super.onStart();
       // returnButton.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View v) {
       //         finish();
       //     }
       // });
        Log.d(TAG, "onStart end");
    }
}
