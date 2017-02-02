package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Brayden on 2/1/2017.
 */

public class NewWestDataset extends AsyncTask<String, Void, String> {

    private static final String PROTOCOL            = "http://";
    private static final String DOMAIN              = "opendata.newwestcity.ca";
    private static final String PAGE_FOLDER         = "datasets";
    private static final String DOWNLOADS_FOLDER    = "downloads";

    private static final int BUFFER_SIZE = 4096;
    private static final String CACHE_DIR = "cache";

    private static final String HTML_EXT = ".html";

    private static final String CACHE_FINISHED = "Dataset Cached";

    public static final String TAG = NewWestDataset.class.getName();

    private String id;
    private Context context;
    /**
     * @param id        the identifier for this New West Dataset. Used for building a URL
     * @param context   the context to return messages to.
     */
    public NewWestDataset(String id, Context context) {
        this.id = id;
        this.context = context;
    }

    // params[0] = filename
    @Override
    protected String doInBackground(String... params)
    {

        cache(PROTOCOL + DOMAIN + "/" + PAGE_FOLDER + "/" + this.id, this.id + HTML_EXT);
        cache(PROTOCOL + DOMAIN + "/" + DOWNLOADS_FOLDER + "/" + this.id + "/" + params[0], params[0]);
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
       Toast.makeText(this.context, CACHE_FINISHED, Toast.LENGTH_LONG).show();
        ///("Downloaded " + result + " bytes");


    }


    /**
     * cache the html of this DataSet
     */
    private static void cache(String urlString, String absolutefileName) {

        String saveFilePath = CACHE_DIR + File.separator + absolutefileName;

        Log.d(TAG, "");
        Log.d(TAG, " URL: "+urlString+" TO:"+saveFilePath);

        try {
            URL url = new URL(urlString);


            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            Log.d(TAG, "HTTP code: " + http.getResponseCode());

            if(http.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new Exception("Failed to download from "+urlString+" with HTTP code: "+http.getResponseCode());

            // opens input stream from the HTTP connection
            InputStream inputStream = http.getInputStream();


            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            File file = new File(saveFilePath);
            int file_size = Integer.parseInt(String.valueOf(file.length()/1024));

            Log.d(TAG, "SAVED FILE SIZE: "+file_size);

            outputStream.close();
            inputStream.close();

            http.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
