package gitpushforce.comp3717.bcit.ca.bctrafficcams;

/**
 * Created by arroy on 2017-02-08.
 */

public class HighwayCameraListDataModel {

    private String cameraName;

    public HighwayCameraListDataModel (String cameraName)
    {
        this.cameraName = cameraName;
    }

    public String getCameraName()
    {
        return cameraName;
    }
}
