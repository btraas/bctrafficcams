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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.CustomLoaderCallbacks;
import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

import static gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper.*;

//import static gitpushforce.comp3717.bcit.ca.bctrafficcams.R.id.returnButton;


public class SavedCamerasListActivity extends AbstractCameraListActivity {

    public static final String TAG = SavedCamerasListActivity.class.getName();


    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        initDrawer(R.id.nav_my_cameras);
        getSupportActionBar().setTitle("Saved Cameras");
    }

    @Override
    public void initLoader(LoaderManager manager, Activity a, CursorAdapter c) {

        CustomLoaderCallbacks cb = new CustomLoaderCallbacks(a, c, Uri.parse(URI_BASE + CamerasOpenHelper.TABLE_NAME),
                                                                null, "saved = 1", null, null);
        manager.initLoader(0, null, cb);
    }

    @Override
    public Cursor runQuery(CharSequence charSequence) {
        String constraint = "%" + charSequence.toString().toLowerCase() + "%";
        OpenHelper helper = new CamerasOpenHelper(this);
        Cursor c = helper.getRows("LOWER(camera_name) LIKE ? AND saved = 1", new String[]{constraint});
        return c;
    }
}
