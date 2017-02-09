package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.data;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;


public class HighwayCameraListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    public static final String TAG = HighwayCameraListActivity.class.getName();
    private ListView listView;
    private ArrayList<HighwayCameraListDataModel> dataModels;
    private int side = 100;
    private static HighwayListCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_camera_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.camera_list);
        //listView.setOnItemClickListener(this);


        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, side, side, true);

        dataModels = new ArrayList<>();

        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 1", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 2", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 3", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 4", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 5", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 6", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 7", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 8", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 9", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 10", bMapScaled));
        dataModels.add(new HighwayCameraListDataModel("Temporary Camera Name 11", bMapScaled));

        adapter = new HighwayListCustomAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HighwayCameraListDataModel dataModel = dataModels.get(position);
                Snackbar.make(view, dataModel.getCameraName(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
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
