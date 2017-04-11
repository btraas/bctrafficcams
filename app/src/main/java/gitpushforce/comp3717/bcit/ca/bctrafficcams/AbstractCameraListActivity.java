package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.CustomLoaderCallbacks;
import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;


public abstract class AbstractCameraListActivity extends RootActivity implements FilterQueryProvider {

    public static final String TAG = AbstractCameraListActivity.class.getName();
    private ListView listView;
    private ArrayList<HighwayCameraListDataModel> dataModels;
    private int side = 100;
    private CameraCursorAdapter adapter;

    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.layoutId = R.layout.activity_highway_camera_list;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_highway_camera_list);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init drawer with this item selected


        inputSearch = (EditText)findViewById(R.id.inputSearch);

        listView = (ListView)findViewById(R.id.camera_list);
        //listView.setOnItemClickListener(this);


        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, side, side, true);

        dataModels = new ArrayList<>();



        //final OpenHelper helper = new CamerasOpenHelper(getApplicationContext());
        //Cursor dbData = helper.getRows();
        //dbData.moveToFirst();

        //Log.d(TAG, "rows: " + helper.getNumberOfRows());


        adapter = new CameraCursorAdapter(this,
                R.layout.highway_camera_list_row_item,
                null, //helper.getRows(), // dont do this
                new String[]
                        {
                                CamerasOpenHelper.NAME_COLUMN,
                        },
                new int[]
                        {
                                android.R.id.text1,
                        }
        );


        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

        adapter.setFilterQueryProvider(this);

        // use content provider to load data from db
        // now done in subclass
        final LoaderManager manager = getLoaderManager();
        //manager.initLoader(0, null, new CustomLoaderCallbacks(AbstractCameraListActivity.this, adapter, Uri.parse(OpenHelper.URI_BASE + CamerasOpenHelper.TABLE_NAME)));
        this.initLoader(manager, AbstractCameraListActivity.this, adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor c = ((SimpleCursorAdapter)listView.getAdapter()).getCursor();
                c.moveToPosition(position);

                //HighwayCameraListDataModel dataModel = dataModels.get(position);
                //HighwayCameraListDataModel dataModel = new HighwayCameraListDataModel(c.getString(c.getColumnIndex("camera_name")), )
                Snackbar.make(view, ((TextView)view.findViewById(R.id.camera_name)).getText().toString(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                final Intent intent;
                intent = new Intent(getApplicationContext(), HighwayCameraViewActivity.class);
                //intent.putExtra("name", dataModel.getCameraName());
                //intent.putExtra("link", dataModel.getImageLink());
                intent.putExtra("cam_id", c.getInt(c.getColumnIndex("_id"))); // TODO should be replaced with finding by ID rather than hoping the order is the same as this cursor...
                startActivity(intent);
            }
        });
        initSearch();
        Log.d(TAG, "onCreate end");
    }

    public abstract void initLoader(LoaderManager manager, Activity a, CursorAdapter c);

    private void initSearch() {

        adapter.getFilter().filter("");
        adapter.notifyDataSetChanged();

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                adapter.getFilter().filter(cs.toString());
                adapter.notifyDataSetChanged();

                /*
                HighwayListCustomAdapter adapter = HighwayCameraListActivity.this.adapter;
                adapter.getFilter().filter(cs);
                listView.setAdapter(adapter);
                listView.invalidateViews();
                */

                //listView.setAdapter();
                Log.d(TAG, "text changed!");
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
