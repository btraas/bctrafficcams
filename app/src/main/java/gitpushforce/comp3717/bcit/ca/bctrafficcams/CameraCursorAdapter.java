package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arroy on 2017-02-08.
 */

public class CameraCursorAdapter extends SimpleCursorAdapter
        implements View.OnClickListener {

    private static final String TAG = CameraCursorAdapter.class.getName();

    private ArrayList<HighwayCameraListDataModel> cameraList;
    private ArrayList<HighwayCameraListDataModel> originalCameraList;
    //private final Context mContext;
    //private final Activity activity;

    private Activity activity;
    private int layout;

    private CameraCursorAdapter self;

    //View lookup cache
    private static class ViewHolder {
        TextView txtCameraName;
        ImageView cameraImage;
    }

    public CameraCursorAdapter(Activity activity, int layout, Cursor c, String[] from, int[] to)
    {
        //super(activity.getApplicationContext(), R.layout.highway_camera_list_row_item, list);
        super(activity, layout, c, from, to);
        this.activity = activity;
        this.layout = layout;

        this.self = this;
        //this.cameraList = list;
        //this.originalCameraList = list;
        //this.activity = activity;
        //this.mContext = activity.getApplicationContext();
    }


    @Override
    public void onClick(View v)
    {
        /*
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        HighwayCameraListDataModel dataModel = (HighwayCameraListDataModel)object;

        switch (v.getId())
        {
            case R.id.
        }
        */
    }


    @Override
    public View newView(Context context, Cursor c, ViewGroup parent) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);


        bindView(v, context, c);


        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c)
    {
        //HighwayCameraListDataModel dataModel = getItem(position);
        //ViewHolder viewHolder;
        final View result;

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtCameraName    = (TextView) v.findViewById(R.id.camera_name);
        viewHolder.cameraImage      = (ImageView) v.findViewById(R.id.camera_image);

        v.setTag(viewHolder);

        String name = c.getString(c.getColumnIndex("camera_name"));
        viewHolder.txtCameraName.setText(name);
        byte[] imageData = c.getBlob(c.getColumnIndex("camera_thumbnail"));
        if(imageData != null && imageData.length > 0) {
            Bitmap data = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            Log.d(TAG, "setting "+name+" image bitmap from image ("+imageData.length+" bytes).");
            if(data == null) Log.d(TAG, "IMAGE NULL~");
            viewHolder.cameraImage.setImageBitmap(data);
        }
        viewHolder.cameraImage.setOnClickListener(this);
        viewHolder.cameraImage.setVisibility(View.VISIBLE);

        //viewHolder.cameraImage.setTag(position);

        //return convertView;
    }
}
