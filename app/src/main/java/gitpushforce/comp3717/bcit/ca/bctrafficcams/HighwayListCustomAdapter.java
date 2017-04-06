package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arroy on 2017-02-08.
 */

public class HighwayListCustomAdapter extends ArrayAdapter<HighwayCameraListDataModel>
        implements View.OnClickListener, Filterable {

    private static final String TAG = HighwayListCustomAdapter.class.getName();

    private ArrayList<HighwayCameraListDataModel> cameraList;
    private ArrayList<HighwayCameraListDataModel> originalCameraList;
    private final Context mContext;
    private final Activity activity;
    private HighwayListCustomAdapter self;

    //View lookup cache
    private static class ViewHolder {
        TextView txtCameraName;
        ImageView cameraImage;
    }

    public HighwayListCustomAdapter(ArrayList<HighwayCameraListDataModel> list, Activity activity)
    {
        super(activity.getApplicationContext(), R.layout.highway_camera_list_row_item, list);

        this.self = this;
        this.cameraList = list;
        this.originalCameraList = list;
        this.activity = activity;
        this.mContext = activity.getApplicationContext();
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

    @NonNull
    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String in = charSequence.toString().toLowerCase();
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = originalCameraList;
                    results.count = originalCameraList.size();
                }
                else
                {
                    ArrayList<HighwayCameraListDataModel> filterResultsData = new ArrayList<>();

                    for(HighwayCameraListDataModel data : originalCameraList)
                    {
                        if(data.getCameraName().toLowerCase().contains(in))
                            filterResultsData.add(data);
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                Log.d(TAG, "filtered to "+filterResults.count + "values!");
                self.cameraList = (ArrayList<HighwayCameraListDataModel>) filterResults.values;
                Log.d(TAG, "real: "+self.cameraList.size());
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "notifying change ");
                        self.notifyDataSetChanged();
                    }
                });
            }
        };
    }


            int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
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
