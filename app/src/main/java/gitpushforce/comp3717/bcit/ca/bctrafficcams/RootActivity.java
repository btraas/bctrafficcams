package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.NoChangeException;
import gitpushforce.comp3717.bcit.ca.bctrafficcams.databases.OpenHelper;

public abstract class RootActivity extends AppBarActivity implements NavigationView.OnNavigationItemSelectedListener  {


    protected int layoutId;
    private View appBar;
    private View navView;
    private ViewGroup contentContainer;
    protected View content;


    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView listView;
    private OpenHelper     openHelper;
    private SimpleCursorAdapter adapter;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private static final String TAG = RootActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate begin");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);


        appBar = findViewById(R.id.activity_root_app_bar);
        contentContainer = (ViewGroup)appBar.findViewById(R.id.content_container);
        content = inflate(layoutId); // exception if layout layoutId doesn't have

        Log.d(TAG,"Set content to "+content);

        //setContentView(R.layout.activity_main);



        openHelper = new CamerasOpenHelper(getApplicationContext());

        if(openHelper.getNumberOfRows() < 1) {
            openHelper.deleteTable();
            openHelper.onCreate(openHelper.getWritableDatabase());

        }


        Log.d(TAG, "onCreate end");

    }

    protected final View inflate(int resId) {
        return View.inflate(this,  resId, contentContainer);

    }




    protected final void initDrawer(int selectedId) {

        Log.d(TAG,"initDrawer("+selectedId+")");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize drawer menu
        //menuAdapter();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navView = ((NavigationView)this.findViewById(R.id.nav_view)).getHeaderView(0);

        TextView title = (TextView)navView.findViewById(R.id.nav_title);
        TextView subtitle = (TextView)navView.findViewById(R.id.nav_subtitle);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Init for the given nav id
        if(selectedId > 0) {
            MenuItem item = navigationView.getMenu().findItem(selectedId);
            item.setChecked(true);

        }
    }

    // TODO @javax.annotation.OverridingMethodsMustInvokeSuper
    protected void load(Cursor c) {
        c.moveToFirst();


    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }


    }


    //HAMBURGER MENU STUFF
    //
    /*
    public void menuAdapter(){

        //add menu items here
        mNavItems.add(new NavItem("Highway Cameras", "Check out the different highway cameras", R.drawable.ic_menu_camera));
        mNavItems.add(new NavItem("Routes", "Choose route and see traffic adjusted travel time", R.drawable.ic_menu_manage));
        mNavItems.add(new NavItem("Download Cameras", "Update list of cameras", R.drawable.ic_menu_gallery));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });
    }
    */

    //controls what happens when you click a menu item
    /*
    private void selectItemFromDrawer(int position) {

        switch(position){
            case 0:
                viewCamerasList();
                break;
            case 1:
                viewRoutes();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "Downloading data...", Toast.LENGTH_LONG).show();
                (new SyncJob()).execute();
        }

    }
    */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.nav_map:
                startActivityIfNotThis(MainActivity.class);
                break;
            case R.id.nav_camera  :
                startActivityIfNotThis(HighwayCameraListActivity.class);
                break;
            case R.id.nav_routes  :
                startActivityIfNotThis(RoutesActivity.class);
                break;
            case R.id.nav_newroutes :
                startActivityIfNotThis(OwnRoutesCreationActivity.class);
                break;
            case R.id.nav_download :
                Toast.makeText(getApplicationContext(), "Downloading data...", Toast.LENGTH_LONG).show();
                (new SyncJob()).execute();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //inner class for individual menu items. Defines a menu item
    class NavItem {
        String mTitle;
        String mSubtitle;
        int mIcon;

        public NavItem(String title, String subtitle, int icon){
            mTitle = title;
            mSubtitle = subtitle;
            int mIcon = icon;
        }
    }



    private class SyncJob extends AsyncTask<String, Void, Bundle> {

        @Override
        protected Bundle doInBackground(String[] params) {


            Bundle b = new Bundle();
            b.putInt("result", 0);


            OpenHelper helper = new CamerasOpenHelper(getApplicationContext());

            int result, updated = 0;
            int datasets = (int)helper.getNumberOfRows();

            try {
                (new DatabaseBuilder(getApplicationContext())).sync();
                updated = (int)helper.getNumberOfRows() - datasets;

                if(updated == 0) b.putString("msg", "Already up-to-date");
                else b.putString("msg", "Success: Updated "+updated+" cameras");
            } catch(NoChangeException e) {
                b.putString("msg", e.getMessage());
            } catch (IOException e) {
                b.putString("msg", "Error: " + e.getMessage());
            }



            b.putInt("updated", updated);
            return b;
        }

        @Override
        protected void onPostExecute(Bundle b) {
            String message = b.getString("msg");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            //findViewById(R.id.sync_button).clearAnimation();

            Log.d(TAG, "updated = "+b.getInt("updated"));

            // if(b.getInt("updated") > 0)
            // {
            finish();
            startActivity(getIntent()); // show changes
            //  }

        }
    }


    //Adapter inner class with modified methods
    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems){
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mNavItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v;

            if (view == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.drawer_item, null);
            } else {
                v = view;
            }

            TextView titleView = (TextView) v.findViewById(R.id.title);
            TextView subTitleView = (TextView) v.findViewById(R.id.subTitle);
            ImageView iconView = (ImageView) v.findViewById(R.id.icon);

            titleView.setText(mNavItems.get(i).mTitle);
            subTitleView.setText(mNavItems.get(i).mSubtitle);
            iconView.setImageResource(mNavItems.get(i).mIcon);

            return v;
        }
    }
    //
    //END OF MENU STUFF


}
