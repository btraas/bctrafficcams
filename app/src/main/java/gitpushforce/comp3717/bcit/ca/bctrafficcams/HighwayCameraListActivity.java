package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.CustomLoaderCallbacks;
import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;


public class HighwayCameraListActivity extends AbstractCameraListActivity {

    public static final String TAG = HighwayCameraListActivity.class.getName();

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        initDrawer(R.id.nav_camera);
        getSupportActionBar().setTitle("All Cameras");
    }

    @Override
    public void initLoader(LoaderManager manager, Activity a, CursorAdapter ca) {
            manager.initLoader(0, null, new CustomLoaderCallbacks(a, ca, Uri.parse(OpenHelper.URI_BASE + CamerasOpenHelper.TABLE_NAME)));
    }


    @Override
    public Cursor runQuery(CharSequence constraint) {

        String like = "%" + constraint.toString().toLowerCase() + "%";

        OpenHelper cams = new CamerasOpenHelper(this);
        Cursor c = cams.getRows("LOWER(camera_name) LIKE ?", new String[]{like});

        return c;

    }



}
