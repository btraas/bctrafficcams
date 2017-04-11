package gitpushforce.comp3717.bcit.ca.bctrafficcams.databases;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;


/**
 * Created by Brayden on 2/6/2017.
 */

public class CustomLoaderCallbacks
        implements LoaderManager.LoaderCallbacks<Cursor>
{
    private Activity activity;
    private CursorAdapter adapter;
    private Uri contentUri;
    private String[] projection;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;
   // private ContentProvider provider;

    private static final String TAG = CustomLoaderCallbacks.class.getName();

    public CustomLoaderCallbacks(Activity activity, CursorAdapter adapter,
                                 Uri contentUri, String[] projection, String selection,
                                 String[] selectionArgs, String sortOrder) {
        this.activity = activity;
        this.adapter = adapter;
        this.contentUri = contentUri;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;

        Log.d(TAG, "Created custom loader");
    }

    public CustomLoaderCallbacks(Activity activity, CursorAdapter adapter, Uri contentUri) {
        this(activity, adapter, contentUri, null, null, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(final int    id,
                                         final Bundle args)
    {
        final Uri uri;
        final CursorLoader loader;

        // activity, uri, projecton, selection, args, sortorder
        loader = new CursorLoader(activity, contentUri, projection, selection, selectionArgs, sortOrder);
        Log.d(TAG, "onCreateLoader");
        return (loader);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader,
                               final Cursor         data)
    {
        Log.d(TAG, "onLoadFinished");
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader)
    {
        Log.d(TAG, "onLoaderReset");
        adapter.swapCursor(null);
    }
}