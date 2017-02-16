package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.graphics.Bitmap;

/**
 * Created by arroy on 2017-02-08.
 */

public class HighwayCameraListDataModel {

    private String cameraName;
    private Bitmap thumbnail;

    public HighwayCameraListDataModel (String cameraName, Bitmap thumbnail)
    {
        this.cameraName = cameraName;
        this.thumbnail = thumbnail;

    }

    public String getCameraName()
    {
        return cameraName;
    }

    public Bitmap getThumbnail()
    {
        return thumbnail;
    }

}
