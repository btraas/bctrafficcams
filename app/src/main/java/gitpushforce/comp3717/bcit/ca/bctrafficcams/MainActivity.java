package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;


public class MainActivity extends RootActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Activity thisActivity;
    private MapView mapView;

    public static final String TAG = MainActivity.class.getName();
    private static String S_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.layoutId = R.layout.content_main;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_root);

        initDrawer(R.id.nav_map);
        thisActivity = this;


        mapView = (MapView) content.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        Log.d(TAG, "onCreate end");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        //loadPins(null);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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

        //(new LoadPinsJob(mMap)).execute();

        /*
            TODO TOO SLOW

        Cursor cameras = openHelper.getRows(getApplicationContext());

        cameras.moveToFirst();
        int failed = 0;
        while(cameras.moveToNext())
        {
            try {
                String name = cameras.getString(cameras.getColumnIndex("camera_name"));
                LatLng location = getLocationFromAddress(name);
                mMap.addMarker(new MarkerOptions().position(location).title(name));
            } catch(IOException e) {
                // do nothing
                failed++;
            }
        }
        Log.e(TAG,"failed cameras: "+failed);
        */

        //mMap.addMarker(new MarkerOptions().position(start).title("New West"));



        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                //Toast.makeText(getApplicationContext(), arg0.getId(), Toast.LENGTH_LONG).show();

                OpenHelper openHelper = new CamerasOpenHelper(thisActivity);
                Cursor c = openHelper.getRow(Integer.parseInt(arg0.getSnippet()));
                c.moveToFirst();
                String link = c.getString(c.getColumnIndex("camera_link"));


                final Intent intent;
                intent = new Intent(getApplicationContext(), HighwayCameraViewActivity.class);
                //intent.putExtra("name", arg0.getTitle());
                //intent.putExtra("link", link);
                intent.putExtra("cam_json", OpenHelper.cursorJSONObject(c).toString()); // TODO BUG FOR REMOVED CAMERAS ?? _id won't equal cam no...

                c.close();
                openHelper.close();
                startActivity(intent);

            }
        });

        OpenHelper openHelper = new CamerasOpenHelper(getApplicationContext());
        Cursor cameras = openHelper.getRows();

        cameras.moveToFirst();
        int failed = 0;
        while(cameras.moveToNext())
        {
            try {
                String name     = cameras.getString(cameras.getColumnIndex("camera_name"));
                int id = cameras.getInt(cameras.getColumnIndex("_id"));
                LatLng location = new LatLng(
                        Double.parseDouble(cameras.getString(cameras.getColumnIndex("latitude"))),
                        Double.parseDouble(cameras.getString(cameras.getColumnIndex("longitude")))
                );
                Log.d(TAG, "adding camera " + name + " to map");

                mMap.addMarker(new MarkerOptions().position(location).title(name).snippet(""+id));
            } catch(Exception e) {
                // do nothing
                failed++;
            }
        }
        Log.e(TAG,"failed cameras: "+failed);
        cameras.close();
        openHelper.close();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 9));


        LoadPinsJob loader = new LoadPinsJob(mMap);
        loader.execute();

    }

    private class LoadPinsJob extends AsyncTask<String, Void, Bundle> {

        private GoogleMap map;
        public LoadPinsJob(GoogleMap map) {
            this.map = map;
        }

        @Override
        protected Bundle doInBackground(String[] params) {


            Bundle b = new Bundle();
            b.putInt("result", 0);

            OpenHelper openHelper = new CamerasOpenHelper(getApplicationContext());
            Cursor cameras = openHelper.getRows();

            cameras.moveToFirst();
            int failed = 0;
            while(cameras.moveToNext())
            {
                try {
                    String name     = cameras.getString(cameras.getColumnIndex("camera_name"));
                    int id = cameras.getInt(cameras.getColumnIndex("_id"));
                    LatLng location = new LatLng(
                                    Double.parseDouble(cameras.getString(cameras.getColumnIndex("latitude"))),
                                    Double.parseDouble(cameras.getString(cameras.getColumnIndex("longitude")))
                    );
                    map.addMarker(new MarkerOptions().position(location).title(name).snippet(""+id));
                } catch(Exception e) {
                    // do nothing
                    failed++;
                }
            }
            Log.e(TAG,"failed cameras: "+failed);
            cameras.close();
            openHelper.close();

            return b;

        }

        @Override
        protected void onPostExecute(Bundle b) {
            String message = b.getString("msg");
           // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            //findViewById(R.id.sync_button).clearAnimation();

            Log.d(TAG, "updated = "+b.getInt("updated"));

            // if(b.getInt("updated") > 0)
            // {
            //finish();
            //startActivity(getIntent()); // show changes
            //  }

        }
    }





}
