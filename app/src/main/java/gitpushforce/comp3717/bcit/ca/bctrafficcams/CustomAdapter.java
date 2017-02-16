package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomAdapter extends SimpleCursorAdapter {

    private int mSelectedPosition;
    Cursor items;
    private Context context;
    private int layout;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        Cursor c = getCursor();

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

        int nameCol = c.getColumnIndex("camera_name");
        String name = c.getString(nameCol);


        TextView name_text = (TextView) v.findViewById(R.id.camera_name);
        if (name_text != null) {
            //int count = c.getInt(countCol);
            //TODO UNCOMMENT name_text.setText(name + " ("+count+")");
        }


        return v;
    }


    public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
        this.layout = layout;
    }


    @Override
    public void bindView(View v, Context context, Cursor c) {

        int nameCol = c.getColumnIndex("camera_name");
        String name = c.getString(nameCol);

        int countCol = c.getColumnIndex("dataset_count");
        int count = c.getInt(countCol);


        TextView name_text = (TextView) v.findViewById(R.id.camera_name);
        if (name_text != null) {
            name_text.setText(name + " (" + count + ")");
        }

        //name_text.setTextColor(Color.GREEN);
/*
        int position = c.getPosition();
        if (mSelectedPosition == position) {
            //v.setBackgroundResource(R.drawable.listviewbackground);
            v.getBackground().setDither(true);
        } else {
            v.setBackgroundColor(Color.BLACK);
        }
*/
    }


    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();

    }
}