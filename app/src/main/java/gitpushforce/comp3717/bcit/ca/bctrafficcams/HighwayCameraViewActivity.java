package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;

public class HighwayCameraViewActivity extends AppCompatActivity {

    public static final String TAG = HighwayCameraViewActivity.class.getName();
    // private ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highway_camera_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //returnButton = (ImageButton)findViewById(R.id.returnButton);

        String link = getIntent().getStringExtra("link");

        new DownloadImageTask((ImageView) findViewById(R.id.camera_image))
                .execute(link);


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

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
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
            if(bmImage != null) bmImage.setImageBitmap(result);
        }
    }

}
