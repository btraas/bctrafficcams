package gitpushforce.comp3717.bcit.ca.bctrafficcams;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.NoChangeException;
import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;


/**
 * Created by Brayden on 2/6/2017.
 */

public final class DatabaseBuilder {


    private static final String TAG = DatabaseBuilder.class.getName();

    private static final String OPENDATA_DOMAIN = "opendata.newwestcity.ca";
    private static final String DRIVEBC_DOMAIN  = "images.drivebc.ca";
    private static final String TRANSLINK_DOMAIN = "www.translink.ca";

    private static final String DRIVEBC_CAMERAS_BASEURL = "http://" + DRIVEBC_DOMAIN + "/bchighwaycam/pub/html/www/";
    private static final String DRIVEBC_CAMERAS_URL = DRIVEBC_CAMERAS_BASEURL + "index.html";
    private static final String DRIVEBC_CAMERAS_SELECT = "#contentx div table tr td a img";


    private static final int MAX_ERROR_TOAST = 3; // max error pages to show on Toast.




    private OpenHelper helper;

    private SQLiteDatabase camDB;

    public DatabaseBuilder(Context context) {
        helper = new CamerasOpenHelper(context);
        //datasetsHelper = new DatasetsOpenHelper(context);

        camDB = helper.getWritableDatabase();

    }


    private int getNumberOfDatasets(String category_name) {

        Cursor cursor = helper.getRow(null, "category_name", category_name);

        cursor.moveToFirst();
        int number = cursor.getInt(cursor.getColumnIndex("dataset_count"));
        cursor.close();
        return number;
    }

// So we can end db transactions.
    public int sync() throws IOException, NoChangeException {

        int result = -1;

        helper.getWritableDatabase().beginTransactionNonExclusive();

        try {

            //cleanup(); // delete current data
            //categoriesHelper.rebuildTable(); // delete current data
            result = syncHandler();
            helper.getWritableDatabase().setTransactionSuccessful();

        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            helper.getWritableDatabase().endTransaction();
        }
        return result;
    }

    private int syncHandler() throws IOException, NoChangeException {
        //InputStream categories = httpInputStream(CATEGORIES_URL);

        int originalCount = (int)helper.getNumberOfRows();


        Log.d(TAG, "count: "+originalCount);

        int cameraCount = 0;
        //int datasetCount = 0;


        ArrayList<String> failedCams = new ArrayList<String>();
        ArrayList<String> failedDatasets = new ArrayList<String>();

        try {
            Elements cameras = Jsoup.connect(DRIVEBC_CAMERAS_URL).get().select(DRIVEBC_CAMERAS_SELECT);
            for (Element camera : cameras) {
                Log.d(TAG, "Camera "+(cameraCount+1));

                String link = DRIVEBC_CAMERAS_BASEURL + camera.parent().attr("href");
                String name = camera.attr("title").replace("Webcam Image: ", "");

                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("_id", ""+(++cameraCount));
                hm.put("camera_name", name);
                hm.put("camera_link", ""+link);
                helper.insert(camDB, hm);
                //helper.insert(helper.getWritableDatabase(), )

            }
        } catch (IOException e) {
            throw new IOException("Unable to connect to "+DRIVEBC_DOMAIN);
        }


        if(failedCams.size() > 0) {
            String failed = "Failed ot load ("+failedCams.size()+") Cams: ";

            int i;
            for(i = 0; i <= MAX_ERROR_TOAST && i < failedCams.size(); i++) {
                failed += failedCams.get(i);
                if(i+1 != MAX_ERROR_TOAST && i+1 < failedCams.size()) failed += ", ";
            }
            if(i < failedCams.size()) failed += "...";

            throw new IOException(failed);


        }

        Log.d(TAG, "orig dataset: "+originalCount+" now datasets: "+cameraCount);


        int count = (int)helper.getNumberOfRows();

        if(count == originalCount) throw new NoChangeException("Already up-to-date");
        if(count < originalCount) throw new IOException("Failed to update DB...");

        return count - originalCount;
    }






}
