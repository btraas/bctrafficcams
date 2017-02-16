package gitpushforce.comp3717.bcit.ca.bctrafficcams.databases;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by darcy on 2016-10-16.
 */

public abstract class OpenHelper
    extends SQLiteOpenHelper
{
    private static final String TAG = OpenHelper.class.getName();
    private static final int SCHEMA_VERSION = 1;
    private static final String URI_BASE = "content://a00968178.comp3717.bcit.ca.opendata/";

    //private static final String DB_NAME = "categories.db";
    //public static final String NAME_TABLE_NAME = "categories";
    //private static final String ID_COLUMN_NAME = "_id";
    //public static final String NAME_COLUMN_NAME = "category";
    //private static CamerasOpenHelper instance;
	
	protected final String dbName;

    protected final String tableName;
	protected final String idName;
	protected final String[] columnNames;
    protected final String[] columnTypes;
	protected final Uri contentUri;

    protected String nameColumn;

    private SQLiteDatabase writeDatabase;
    private SQLiteDatabase readDatabase;

    private static final String NAME_TYPE_MISMATCH = "Error: column names doesn't match column types!";

    protected OpenHelper(final Context ctx, final String dbName,
                         String tableName, String idName,
                         String[] columnNames, String[] columnTypes)
    {
        super(ctx, dbName, null, SCHEMA_VERSION);

        if(columnNames.length != columnTypes.length) {

            String msg = NAME_TYPE_MISMATCH + " ("
                    + columnNames.length + " != " + columnTypes.length + ")";

            Log.e(TAG, msg, new Exception(msg));
        }

        this.dbName = dbName;
        this.tableName = tableName;
        this.idName = idName;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.contentUri = Uri.parse(URI_BASE + tableName);

        this.nameColumn = columnNames[0]; // by default
    }

    @Override
    public void onConfigure(final SQLiteDatabase db)
    {
        super.onConfigure(db);

        setWriteAheadLoggingEnabled(true);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(final SQLiteDatabase db)
    {
        String CREATE_TABLE;

        CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "  + this.tableName + " ( " +
                            this.idName   + " INTEGER PRIMARY KEY AUTOINCREMENT, ";

        for(int i = 0; i < columnNames.length; i++) {
            CREATE_TABLE += columnNames[i] + " " + columnTypes[i];
            if(i+1 != columnNames.length) CREATE_TABLE += ", ";
        }
        CREATE_TABLE += ")";
        Log.d(TAG, "Running SQL: "+CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db,
                          final int oldVersion,
                          final int newVersion)
    {
    }

    public Uri getContentUri() {
        return contentUri;
    }

    public String nameColumn() {
        return this.nameColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDbName() {
        return dbName;
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {

        if(writeDatabase == null) writeDatabase = super.getWritableDatabase();
        return writeDatabase;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {

        //return getWritableDatabase();

        if(readDatabase == null) readDatabase = super.getReadableDatabase();
        return readDatabase;


    }

    public long getNumberOfRows()
    {
        return getNumberOfRows(null, null);
    }

    public long getNumberOfRows(String selection,
                                String[] args)
    {
        final long numEntries;
        final SQLiteDatabase db = getReadableDatabase();

        this.onCreate(db);
        //numEntries = DatabaseUtils.queryNumEntries(db, this.tableName);

        //if(selection == null && args == null) numEntries = DatabaseUtils.queryNumEntries(db, this.tableName);
        //else if(args == null) numEntries = DatabaseUtils.queryNumEntries(db, this.tableName, selection);
        //else
        numEntries = DatabaseUtils.queryNumEntries(db, this.tableName, selection, args);

        return (numEntries);
    }

    public void insert(final SQLiteDatabase db,
                       final HashMap<String, String> data
                       )
    {
        final ContentValues contentValues;

        contentValues = new ContentValues();
        for(Map.Entry<String, String> entry : data.entrySet()) {
            contentValues.put(entry.getKey(), entry.getValue());
        }
        db.insert(this.tableName, null, contentValues);
    }



    public void deleteTable() {
        SQLiteDatabase db = getWritableDatabase();

        String SQL = "DROP TABLE IF EXISTS "+this.tableName;
                Log.d(TAG, "Executing SQL: " + SQL);
        db.execSQL(SQL);
    }

    public void rebuildTable() {
        SQLiteDatabase db = getWritableDatabase();

        this.deleteTable();
        this.onCreate(db);
    }

    public void delete(final int id){
       this.delete(this.idName + " = ?", new String[] {""+id});
    }


    public int delete(final String where, final String[] args){
        return getWritableDatabase().delete(this.tableName, where, args);
    }

    public int getId(final String where, final String[] args) throws Resources.NotFoundException {
        Cursor c = this.getRows(null, where, args);
        c.moveToFirst();
        try {
            if (c.getInt(0) == 0) throw new Resources.NotFoundException("Row not found!");
        } catch(CursorIndexOutOfBoundsException e) {
            throw new Resources.NotFoundException("Row not found!");
        }
        return c.getInt(c.getColumnIndex(this.idName));
    }


    /**
     * Default getRow... for PK
     */
    public Cursor getRow(final Context context, final int id) {
        return this.getRow(context, idName, ""+id);
    }

    /**
     * getRow with a colName and ID
     */
    public Cursor getRow(final Context context, final String colName, final String value) {
        final Cursor cursor;
        boolean valid = false;

        Log.d(TAG, "getRow(context, "+colName + ", " + value + ")");

        if(colName.equals(this.idName)) valid = true;
        else {
            List validCols = Arrays.asList(this.columnNames);
            if(validCols.contains(colName)) valid = true;
        }

        if(!valid) {
            Toast.makeText(context, "Invalid column selection: "+colName, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Invalid column selection: "+colName);
            return null;
        }

        return getRows(context, colName + " = ?", new String[] {""+value});


    }

    /**
     * Get all rows
     */
    public Cursor getRows(final Context context)
    {
        return this.getRows(context, null, null, null, null, null, null, null );

    }

    /**
     * Get rows with selection
     *
     */
    public Cursor getRows(final Context context, String selection) {


        return this.getRows(context, null, selection, null, null, null, null, null );

    }

    /**
     * Get rows with selection & args
     *
     */
    public Cursor getRows(final Context context, String selection, String[] selectionArgs) {


        return this.getRows(context, null, selection, selectionArgs, null, null, null, null );

    }

    /**
     * Get rows with full query parameters
     *
     */
    public Cursor getRows(final Context context,
                            String[] cols,
                            String selection,
                            String[] args,
                            String group,
                            String having,
                            String order,
                            String limit)
    {
        final Cursor cursor;

        Log.d(TAG, "getRows() selection: "+selection);

        cursor = getReadableDatabase().query(this.tableName,
                          cols,
                          selection,     // selection, null = *
                          args,     // selection args (String[])
                          group,     // group by
                          having,     // having
                          order,     // order by
                          limit);    // limit

        //context.getContentResolver().
        //cursor.set
        if(context != null) cursor.setNotificationUri(context.getContentResolver(), this.contentUri);

        return (cursor);
    }
}