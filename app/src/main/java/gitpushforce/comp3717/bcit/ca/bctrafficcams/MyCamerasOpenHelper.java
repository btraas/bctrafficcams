package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Context;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

/**
 * Created by darcy on 2016-10-16.
 */

public final class MyCamerasOpenHelper
    extends OpenHelper
{
    public static final String NAME_COLUMN = "camera_name";

    private static final String TAG = MyCamerasOpenHelper.class.getName();
    private static final String DB_NAME = "mycams.db";
    private static final String TABLE_NAME = "mycams";
    private static final String ID_COLUMN_NAME = "_id";
    private static final String[] COLUMNS = {
            "camera_name TEXT NOT NULL",
            "camera_link TEXT NOT NULL",
            "latitude TEXT NOT NULL",
            "longitude TEXT NOT NULL"
    };

    public MyCamerasOpenHelper(final Context ctx)
    {
        super(ctx, DB_NAME, TABLE_NAME, ID_COLUMN_NAME, COLUMNS, NAME_COLUMN );
    }
}
