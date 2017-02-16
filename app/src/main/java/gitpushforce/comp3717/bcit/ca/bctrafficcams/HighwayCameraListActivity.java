package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;


public class HighwayCameraListActivity extends AppCompatActivity {

    public static final String TAG = HighwayCameraListActivity.class.getName();
    private ListView listView;
    private ArrayList<HighwayCameraListDataModel> dataModels;
    private int side = 100;
    private HighwayListCustomAdapter adapter;

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

        /*
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
        */


        OpenHelper helper = new CamerasOpenHelper(getApplicationContext());
        Cursor dbData = helper.getRows(getApplicationContext());
        dbData.moveToFirst();

        Log.d(TAG, "rows: " + helper.getNumberOfRows());


        while(dbData.moveToNext())
        {
            final String name;
            //name = cursor.get
            name = dbData.getString(dbData.getColumnIndex("camera_name"));
            String id   = dbData.getString(dbData.getColumnIndex("_id"));
            String link = dbData.getString(dbData.getColumnIndex("camera_link"));

            dataModels.add(new HighwayCameraListDataModel(name, bMapScaled, link));


            Log.d(TAG, id + "-" + name);
        }

        dbData.close();
        adapter = new HighwayListCustomAdapter(dataModels, getApplicationContext());


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HighwayCameraListDataModel dataModel = dataModels.get(position);
                Snackbar.make(view, dataModel.getCameraName(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                final Intent intent;
                intent = new Intent(getApplicationContext(), HighwayCameraViewActivity.class);
                intent.putExtra("name", dataModel.getCameraName());
                intent.putExtra("link", dataModel.getImageLink());
                startActivity(intent);
            }
        });
        Log.d(TAG, "onCreate end");
    }

    protected  void onStart()
    {   Log.d(TAG, "onStart begin");
        super.onStart();
        Log.d(TAG, "onStart end");
    }
}
