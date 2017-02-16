package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.NoChangeException;
import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    public static final String TAG = MainActivity.class.getName();
    private static String S_TAG = MainActivity.class.getSimpleName();

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView listView;
    private OpenHelper     openHelper;
    private SimpleCursorAdapter adapter;


    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize drawer menu
        menuAdapter();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Log.d(TAG, "onCreate end");

    }

    public void viewCamerasList()
    {
        Log.d(TAG, "viewCamerasList begin");
        final Intent intent;

        intent = new Intent(this, HighwayCameraListActivity.class);
        startActivity(intent);
        //finish();
        Log.d(TAG, "viewCameraList end");
    }

    public void goOwnRoutesCreate()
    {
        Log.d(TAG, "goOwnRoutesCreate begin");
        final Intent intent;

        intent = new Intent(this, OwnRoutesCreationActivity.class);
        startActivity(intent);
        Log.d(TAG, "goOwnRoutesCreate end");
    }


    private void init() {
        final SQLiteDatabase db;
        final long numEntries;
        final LoaderManager manager;

        OpenHelper openHelper = new CamerasOpenHelper(getApplicationContext());

        adapter = new CustomAdapter(getBaseContext(),
                R.layout.highway_camera_list_row_item,
                openHelper.getRows(getApplicationContext()),
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

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng start = new LatLng(49.2189, -122.9177);
        mMap.addMarker(new MarkerOptions().position(start).title("New West"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));

    }

    //HAMBURGER MENU STUFF
    //
    public void menuAdapter(){

        //add menu items here
        mNavItems.add(new NavItem("Highway Cameras", "Check out the different highway cameras", R.drawable.ic_expand));
        mNavItems.add(new NavItem("Create Route", "Create your own routes and see traffic adjusted travel time", R.drawable.ic_expand));
        mNavItems.add(new NavItem("test cache", "This does something", R.drawable.ic_expand));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
    }

    //controls what happens when you click a menu item
    private void selectItemFromDrawer(int position) {

        switch(position){
            case 0:
                viewCamerasList();
                break;
            case 1:
                goOwnRoutesCreate();
                break;
            case 2:
                (new SyncJob()).execute();
        }

    }

    //inner class for individual menu items. Defines a menu item
    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon){
            mTitle = title;
            mSubtitle = subtitle;
            int mIcon = icon;
        }
    }



    private class SyncJob extends AsyncTask<String, Void, Bundle> {

        @Override
        protected Bundle doInBackground(String[] params) {


            Bundle b = new Bundle();
            b.putInt("result", 0);


            OpenHelper helper = new CamerasOpenHelper(getApplicationContext());

            int result, updated = 0;
            int datasets = (int)helper.getNumberOfRows();

            try {
                (new DatabaseBuilder(getApplicationContext())).sync();
                updated = (int)helper.getNumberOfRows() - datasets;

                if(updated == 0) b.putString("msg", "Already up-to-date");
                else b.putString("msg", "Success: Updated "+updated+" cameras");
            } catch(NoChangeException e) {
                b.putString("msg", e.getMessage());
            } catch (IOException e) {
                b.putString("msg", "Error: " + e.getMessage());
            }



            b.putInt("updated", updated);
            return b;
        }

        @Override
        protected void onPostExecute(Bundle b) {
            String message = b.getString("msg");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            //findViewById(R.id.sync_button).clearAnimation();

            Log.d(TAG, "updated = "+b.getInt("updated"));

            if(b.getInt("updated") > 0)
            {
                finish();
                startActivity(getIntent()); // show changes
            }

        }
    }


    //Adapter inner class with modified methods
    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems){
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mNavItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;

            if (view == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.drawer_item, null);
            } else {
                v = view;
            }

            TextView titleView = (TextView) v.findViewById(R.id.title);
            TextView subTitleView = (TextView) v.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) v.findViewById(R.id.icon);

            titleView.setText(mNavItems.get(i).mTitle);
            subTitleView.setText(mNavItems.get(i).mSubtitle);
            iconView.setImageResource(mNavItems.get(i).mIcon);

            return v;
        }
    }
    //
    //END OF MENU STUFF


}
