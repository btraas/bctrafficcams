package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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

    private static final int DEFAULT_EXTRA_CAM_NO = -1;
    private static final String TAG = HighwayCameraViewActivity.class.getName();
    // private ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_camera_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        /*
        String title = getIntent().getStringExtra("name");

        getSupportActionBar().setTitle(title);

        //returnButton = (ImageButton)findViewById(R.id.returnButton);

        String link = getIntent().getStringExtra("link");
        Log.d(TAG, "recieved image link: "+link);

        new DownloadImageTask((ImageView) findViewById(R.id.imageView))
                .execute(link);

        */


        CamerasOpenHelper helper = new CamerasOpenHelper(this);
        Cursor c = helper.getRows();
        pagerAdapter = new CamsPagerAdapter(getSupportFragmentManager(), c);
        c.close();
        helper.close();


        // Set up the ViewPager with the sections adapter.
        pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(pagerAdapter);

        int camNo = DEFAULT_EXTRA_CAM_NO;
        if(getIntent().hasExtra("cam_no")) {
            camNo = getIntent().getIntExtra("cam_no", DEFAULT_EXTRA_CAM_NO);
        }

        if(camNo == DEFAULT_EXTRA_CAM_NO && getIntent().hasExtra("cam_json")) {
            try {
                JSONObject cam = new JSONObject(getIntent().getStringExtra("cam_json"));
                camNo = pagerAdapter.findId(cam.getInt("_id"));
            } catch (JSONException e) {
            }
        }

        pager.setCurrentItem(camNo);


        Log.d(TAG, "onCreate end");
    }

    @Override
    protected void onStart()
    {
        Log.d(TAG, "onStart begin");
        super.onStart();
        // returnButton.setOnClickListener(new View.OnClickListener() {
         //   public void onClick(View v) {
         //       finish();
         //   }
        //})


        Log.d(TAG, "onStart end");
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

    public void addCamera(View v) {
        CamerasOpenHelper helper = new CamerasOpenHelper(this);
        MyCamerasOpenHelper myHelper = new MyCamerasOpenHelper(this);
        int id = (int)pagerAdapter.getItemId(pager.getCurrentItem());
        try {
            
            myHelper.upsertJSON("_id = ?", new String[]{id+""}, OpenHelper.cursorJSONObject(helper.getRow(id)));
            Toast.makeText(this, "Saved camera", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Unable to save camera", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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


            ImageView bmImage = (ImageView)rootView.findViewById(R.id.imageView);


            JSONObject cam;
            try {
                cam = new JSONObject(getArguments().getString("json_string"));

                String link = cam.getString("camera_link");

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
