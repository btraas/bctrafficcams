package gitpushforce.comp3717.bcit.ca.bctrafficcams;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class RoutesActivity extends RootActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = HighwayCameraListActivity.class.getName();

    private ListView listView;

    // Hardcoded routes
    private ArrayList<String> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateBegin");
        super.layoutId = R.layout.activity_routes;
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_routes);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDrawer(R.id.nav_routes);

        listView = (ListView)findViewById(R.id.route_list);
        routes = new ArrayList<>();
        routes.add("Home to BCIT");
        routes.add("Home to Metrotown");
        routes.add("Home to McGill Public Library");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, routes);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        Log.d(TAG, "onCreate end");
    }

    protected  void onStart()
    {   Log.d(TAG, "onStart begin");
        super.onStart();
        Log.d(TAG, "onStart end");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Snackbar.make(view, ((TextView) view).getText(), Snackbar.LENGTH_LONG)
                .setAction("No action", null).show();

    }
}
