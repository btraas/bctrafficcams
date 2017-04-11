package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;

public class HighwayCameraViewActivity extends AppCompatActivity {

    private CamsPagerAdapter pagerAdapter;
    private ViewPager pager;
    //private static FloatingActionButton saveButton;

    private static final int DEFAULT_EXTRA_CAM_NO = -1;
    private static final String TAG = HighwayCameraViewActivity.class.getName();
    // private ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_camera_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //NavUtils.navigateUpFromSameTask(this);





        CamerasOpenHelper helper = new CamerasOpenHelper(this);
        Cursor c = helper.getRows();
        pagerAdapter = new CamsPagerAdapter(getSupportFragmentManager(), c);
        c.close();
        helper.close();


        // Set up the ViewPager with the sections adapter.
        pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(pagerAdapter);

        int camNo = DEFAULT_EXTRA_CAM_NO;
        if(getIntent().hasExtra("cam_id")) {
            camNo = pagerAdapter.findId(getIntent().getIntExtra("cam_id", DEFAULT_EXTRA_CAM_NO));
        }

        /*
        if(camNo == DEFAULT_EXTRA_CAM_NO && getIntent().hasExtra("cam_json")) {
            try {
                JSONObject cam = new JSONObject(getIntent().getStringExtra("cam_json"));
                camNo = pagerAdapter.findId(cam.getInt("_id"));
            } catch (JSONException e) {
            }
        }
        */



        pager.setCurrentItem(camNo);


        Log.d(TAG, "onCreate end");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }



        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);

        }
    }



    public void toggleCamera(View v) {

        CamerasOpenHelper helper = new CamerasOpenHelper(this);
        //int id = pagerAdapter.getId(pager.getCurrentItem());

        /*
        pagerAdapter.getItemId(pos)
                pager.getCurrentItem();
        */
        //int id = pager.getCurrentItem();
        JSONObject o = pagerAdapter.getFromPosition(pager.getCurrentItem());

        //Toast.makeText(v.getContext(), "toggling number "+pager.getCurrentItem() + " "+o, Toast.LENGTH_LONG).show();

        int id;
        try {
            id = o.getInt("_id");
        } catch (JSONException e) {
            return;
        }


        boolean prevSaved = false;
        try {
            prevSaved = (o.getInt("saved") == 1);
        } catch (JSONException e) {
            return;
        }


        if(prevSaved)   ((FloatingActionButton)v).setImageDrawable(getResources().getDrawable(R.drawable.add_camera_black ));
        else        ((FloatingActionButton)v).setImageDrawable(getResources().getDrawable(R.drawable.remove_camera_black));

        try {
            o.put("saved", prevSaved ? 1 : 0);
            //Toast.makeText(v.getContext(), "saving number "+pager.getCurrentItem(), Toast.LENGTH_LONG).show();
            pagerAdapter.cams.put(pager.getCurrentItem(), o);
        } catch (JSONException e) {

        }

        helper.setCamera(id, !prevSaved);
        helper.close();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        //private static final String ARG_STOP_NUMBER = "stop_number";

        private FloatingActionButton saveButton;


        public PlaceholderFragment() {



        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(JSONObject object) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            String json = object.toString();

            args.putString("json_string", json);


            fragment.setArguments(args);
            return fragment;
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {



            //int stopNum = getArguments().getInt(ARG_STOP_NUMBER);
            //Stop stop = LoginBarcodeActivity.stops.get(stopNum-1);

            View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

            saveButton = (FloatingActionButton) rootView.findViewById(R.id.save_button);

            ImageView bmImage = (ImageView)rootView.findViewById(R.id.imageView);


            JSONObject cam;
            try {
                cam = new JSONObject(getArguments().getString("json_string"));

                String link = cam.getString("camera_link");
                boolean saved = cam.getInt("saved") != 0;
                if(saved)   saveButton.setImageDrawable(getResources().getDrawable(R.drawable.remove_camera_black ));
                else        saveButton.setImageDrawable(getResources().getDrawable(R.drawable.add_camera_black));

                (new DownloadImageTask(bmImage))
                        .execute(link);

            } catch (Exception e) {

                e.printStackTrace();
                //Messaging.showError((Activity)container.getContext(), e.getMessage());


            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class CamsPagerAdapter extends FragmentPagerAdapter {

        JSONArray cams;

        public CamsPagerAdapter(FragmentManager fm, Cursor c) {
            super(fm);
            this.cams = OpenHelper.cursorJSONArray(c);
            c.close();
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            try {
                return PlaceholderFragment.newInstance(cams.getJSONObject(position));
            } catch (JSONException e) {
                return null;
            }
        }

        public int findId(int id) {
            for(int i = 0; i<cams.length(); ++i) {
                try {
                    JSONObject thisItem = cams.getJSONObject(i);
                    if (thisItem.getInt("_id") == id) return i;
                } catch (JSONException e) {

                }

            }
            return 0;
        }

        public JSONObject getFromPosition(int pos) {
            try {
                return cams.getJSONObject(pos);
            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        public int getCount() {
            return cams.length();

        }

        @Override
        public CharSequence getPageTitle(int position) {

            try {
                JSONObject cam = cams.getJSONObject(position);

                return cam.getString("camera_name");

            } catch (JSONException e) {
                return "Camera unknown";
            }
        }
    }

}
