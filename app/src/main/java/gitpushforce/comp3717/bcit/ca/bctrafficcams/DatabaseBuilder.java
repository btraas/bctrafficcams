package gitpushforce.comp3717.bcit.ca.bctrafficcams;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;
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

    private static final String DATABC_DOMAIN = "catalogue.data.gov.bc.ca";
    private static final String DATABC_WEBCAMS_RESOURCE_ID = "ba4453bb-f610-4f8f-af9e-8459aba32415";
    private static final String DATABC_WEBCAMS_CSV = "https://" + DATABC_DOMAIN + "/dataset/6b39a910-6c77-476f-ac96-7b4f18849b1c/resource/"+DATABC_WEBCAMS_RESOURCE_ID+"/download/webcams.csv";
    private static final int    WEBCAMS_COLUMN_PAGE_LINK        = 0;
    private static final int    WEBCAMS_COLUMN_IMAGE_LINK       = 1;
    private static final int    WEBCAMS_COLUMN_THUMBNAIL_LINK   = 2;
    private static final int    WEBCAMS_COLUMN_REPLAY_LINK      = 3;
    private static final int    WEBCAMS_COLUMN_ID               = 4;
    private static final int    WEBCAMS_COLUMN_NAME             = 7;
    private static final int    WEBCAMS_COLUMN_LATITUDE         = 11;
    private static final int    WEBCAMS_COLUMN_LONGITUDE        = 12;

    private static final int MAX_ERROR_TOAST = 3; // max error pages to show on Toast.




    //private OpenHelper helper;

    //private SQLiteDatabase camDB;

    private Context context;

    public DatabaseBuilder(Context context) {
        this.context = context;
        /// /helper = new CamerasOpenHelper(context);
        //datasetsHelper = new DatasetsOpenHelper(context);

        //camDB = helper.getWritableDatabase();

    }


    // So we can end db transactions.
    public int sync() throws IOException, NoChangeException {

        int result = -1;

        CamerasOpenHelper helper = new CamerasOpenHelper(context);
        helper.getWritableDatabase().beginTransactionNonExclusive();

        try {

            //cleanup(); // delete current data
            //categoriesHelper.rebuildTable(); // delete current data
            result = syncHandler(helper);
            helper.getWritableDatabase().setTransactionSuccessful();

        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            helper.getWritableDatabase().endTransaction();
            helper.close();
        }
        return result;
    }

    private int syncHandler(CamerasOpenHelper helper) throws IOException, NoChangeException {
        //InputStream categories = httpInputStream(CATEGORIES_URL);

        int originalCount = (int)helper.getNumberOfRows();


        Log.d(TAG, "count: "+originalCount);

        int cameraCount = 0;
        //int datasetCount = 0;


        ArrayList<String> failedCams = new ArrayList<String>();

        try {

            CSVReader reader = new CSVReader(new InputStreamReader( (new URL(DATABC_WEBCAMS_CSV)).openStream() ));

            String[] firstLine = reader.readNext();

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                Log.d(TAG, "Camera "+(++cameraCount));


                //HashMap<String, String> hm = new HashMap<String, String>();
                JSONObject arr = new JSONObject();

                try {
                    arr.put("_id", nextLine[WEBCAMS_COLUMN_ID]);
                    arr.put("camera_name", nextLine[WEBCAMS_COLUMN_NAME]);
                    arr.put("camera_link", nextLine[WEBCAMS_COLUMN_IMAGE_LINK]);
                    arr.put("latitude", nextLine[WEBCAMS_COLUMN_LATITUDE]);
                    arr.put("longitude", nextLine[WEBCAMS_COLUMN_LONGITUDE]);

                    helper.upsertJSON("_id = ?", new String[] {nextLine[WEBCAMS_COLUMN_ID]}, arr);

                } catch (JSONException e) {
                    Log.e(TAG, "Unable to add camera");
                    e.printStackTrace();
                }


            }

        } catch (IOException e) {
            throw new IOException("Unable to connect to " + DATABC_DOMAIN);
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
