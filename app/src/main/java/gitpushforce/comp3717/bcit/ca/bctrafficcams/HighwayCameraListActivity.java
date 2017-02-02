package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;


public class HighwayCameraListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public static final String TAG = HighwayCameraListActivity.class.getName();
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_camera_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView)findViewById(R.id.camera_list);
        listView.setOnItemClickListener(this);
        Log.d(TAG, "onCreate end");
    }

    protected  void onStart()
    {   Log.d(TAG, "onStart begin");
        super.onStart();
        Log.d(TAG, "onStart end");
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, final View view, int position, long id)
    {
        Log.d(TAG, "onItemClick begin");
        Toast.makeText(getApplicationContext(),
                ((TextView)view).getText(),
                Toast.LENGTH_SHORT).show();
        final Intent intent;
        intent = new Intent(this, HighwayCameraViewActivity.class);
        startActivity(intent);
        Log.d(TAG, "onItemClick end");
    }
}
