package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.hideme.hideme.R;

import java.util.ArrayList;

import adapters.ApplicationsAdapter;

/**
 * Created by kady on 23/12/14.
 *
 * @author kady
 */
public class SettingsActivity extends ActionBarActivity {

    private SharedPreferences mSharedPreferences ;
    private ListView mAppsListView;
    private ApplicationsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAppsListView = (ListView) findViewById(R.id.settings_lst_application_list);

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<ResolveInfo> dataArrayList = new ArrayList<>();
        dataArrayList.addAll(getPackageManager().queryIntentActivities(mainIntent, 0));

        Log.d("INSTALLED_APPS",dataArrayList.toString());
        mAdapter = new ApplicationsAdapter(this,dataArrayList);
        mAppsListView.setAdapter(mAdapter);


    }
}
