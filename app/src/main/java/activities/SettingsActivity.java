package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;

import com.hideme.hideme.R;

import java.util.ArrayList;
import java.util.Iterator;

import adapters.ApplicationsAdapter;

/**
 * Created by kady on 23/12/14.
 *
 * @author kady
 */
public class SettingsActivity extends ActionBarActivity {

    private ListView mAppsListView;
    private ApplicationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAppsListView = (ListView) findViewById(R.id.settings_lst_application_list);

        final Intent appQueryIntent = new Intent(Intent.ACTION_MAIN, null);
        appQueryIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<ResolveInfo> dataArrayList = new ArrayList<>();
        dataArrayList.addAll(getPackageManager().queryIntentActivities(appQueryIntent, PackageManager.GET_META_DATA));


        // Filtering : Removing non-uninstallable apps !
        Iterator<ResolveInfo> iterator = dataArrayList.iterator();
        while (iterator.hasNext()) {
            ResolveInfo info = iterator.next();
            if (isSystemPackage(info))
                iterator.remove();
        }


        Log.d("INSTALLED_APPS",dataArrayList.toString());
        mAdapter = new ApplicationsAdapter(this,dataArrayList);
        mAppsListView.setAdapter(mAdapter);


    }

    private boolean isSystemPackage(ResolveInfo info) {
        return (info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
