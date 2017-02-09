package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by arroy on 2017-02-08.
 */

public class HighwayListCustomAdapter extends ArrayAdapter<HighwayCameraListDataModel>
        implements View.OnClickListener {

    private ArrayList<HighwayCameraListDataModel> cameraList;
    private Context mContext;

    //View lookup cache
    private static class ViewHolder {
        TextView txtCameraName;
        ImageView cameraImage;
    }

    public HighwayListCustomAdapter(ArrayList<HighwayCameraListDataModel> list, Context context)
    {
        super(context, R.layout.highway_camera_list_row_item, list);

        this.cameraList = list;
        this.mContext = context;
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


    int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        HighwayCameraListDataModel dataModel = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.highway_camera_list_row_item, parent, false);
            viewHolder.txtCameraName = (TextView) convertView.findViewById(R.id.camera_name);
            viewHolder.cameraImage = (ImageView) convertView.findViewById(R.id.camera_image);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation (animation);
        lastPosition = position;

        /*
        ImageView iv = (ImageView)findViewById(R.id.camera_image);
        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.picture);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, newWidth, newHeight, true);
        iv.setImageBitmap(bMapScaled);
        */
        viewHolder.txtCameraName.setText(dataModel.getCameraName());
        viewHolder.cameraImage.setImageBitmap(dataModel.getThumbnail());
        viewHolder.cameraImage.setOnClickListener(this);
        viewHolder.cameraImage.setVisibility(View.VISIBLE);

        viewHolder.cameraImage.setTag(position);

        return convertView;
    }
}
