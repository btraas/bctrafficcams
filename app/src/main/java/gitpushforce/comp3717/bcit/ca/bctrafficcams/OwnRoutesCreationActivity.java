package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class OwnRoutesCreationActivity extends RootActivity {

    public static final String TAG = OwnRoutesCreationActivity.class.getName();
   // private ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.layoutId = R.layout.activity_own_routes_creation;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_own_routes_creation);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // returnButton = (ImageButton)findViewById(R.id.returnButton);

        // initialize drawer with this item selected
        initDrawer(R.id.nav_newroutes);

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
