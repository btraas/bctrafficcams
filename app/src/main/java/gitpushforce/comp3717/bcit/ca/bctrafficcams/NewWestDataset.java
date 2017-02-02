package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Brayden on 2/1/2017.
 */

public class NewWestDataset {

    private static final String PROTOCOL            = "http://";
    private static final String DOMAIN              = "opendata.newwestcity.ca";
    private static final String PAGE_FOLDER         = "datasets";
    private static final String DOWNLOADS_FOLDER    = "downloads";

    private static final int BUFFER_SIZE = 4096;
    private static final String CACHE_DIR = "cache";

    private static final String HTML_EXT = ".html";

    private String id;
    /**
     * @param id        the identifier for this New West Dataset. Used for building a URL
     */
    public NewWestDataset(String id) {
        this.id = id;

        cache(PROTOCOL + DOMAIN + "/" + PAGE_FOLDER + "/" + this.id, this.id + HTML_EXT);

    }

    public void cacheFile(String fileName) {
        cache(PROTOCOL + DOMAIN + "/" + DOWNLOADS_FOLDER + "/" + this.id + fileName, fileName);
    }

    /**
     * cache the html of this DataSet
     */
    private static void cache(String urlString, String fileName) {

        try {
            URL url = new URL(urlString);


            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            if(http.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // opens input stream from the HTTP connection
                InputStream inputStream = http.getInputStream();
                String saveFilePath = CACHE_DIR + File.separator + fileName;

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();

                http.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
