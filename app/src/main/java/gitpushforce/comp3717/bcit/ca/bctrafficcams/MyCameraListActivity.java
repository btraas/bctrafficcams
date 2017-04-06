package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;


public class MyCameraListActivity extends RootActivity {

    public static final String TAG = MyCameraListActivity.class.getName();
    private ListView listView;
    private ArrayList<HighwayCameraListDataModel> dataModels;
    private int side = 100;
    private HighwayListCustomAdapter adapter;

    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.layoutId = R.layout.activity_highway_camera_list;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_highway_camera_list);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init drawer with this item selected
        initDrawer(R.id.nav_camera);

        inputSearch = (EditText)findViewById(R.id.inputSearch);

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


        OpenHelper helper = new MyCamerasOpenHelper(getApplicationContext());
        Cursor dbData = helper.getRows();
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
        adapter = new HighwayListCustomAdapter(dataModels, this);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HighwayCameraListDataModel dataModel = dataModels.get(position);
                Snackbar.make(view, dataModel.getCameraName(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                final Intent intent;
                intent = new Intent(getApplicationContext(), HighwayCameraViewActivity.class);
                //intent.putExtra("name", dataModel.getCameraName());
                //intent.putExtra("link", dataModel.getImageLink());
                intent.putExtra("cam_no", position+1); // TODO should be replaced with finding by ID rather than hoping the order is the same as this cursor...
                startActivity(intent);
            }
        });
        initSearch();
        Log.d(TAG, "onCreate end");
    }

    private void initSearch() {
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                HighwayListCustomAdapter adapter = MyCameraListActivity.this.adapter;
                adapter.getFilter().filter(cs);
                listView.setAdapter(adapter);
                listView.invalidateViews();
                //listView.setAdapter();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    protected  void onStart()
    {   Log.d(TAG, "onStart begin");
        super.onStart();
        Log.d(TAG, "onStart end");
    }
}
