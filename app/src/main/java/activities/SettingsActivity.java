package activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import com.kady.hideme.R;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;

import adapters.ApplicationsAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kady on 23/12/14.
 *
 * @author kady
 */
public class SettingsActivity extends ActionBarActivity {

    @InjectView(R.id.settings_lst_application_list)
    private ListView mAppsListView;

    @InjectView(R.id.settings_edit_fab)
    private FloatingActionButton mEditFab;

    private ApplicationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.inject(this);

        mEditFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Launch Edit Activity
            }
        });

        mEditFab.attachToListView(mAppsListView);

        ArrayList<ResolveInfo> dataArrayList = fetchInstalledApps();

        mAdapter = new ApplicationsAdapter(this,dataArrayList);
        mAppsListView.setAdapter(mAdapter);


    }


    /**
     * Fetching the installed applications on the device
     * @param systemApps pass true to include system applications
     * @return <code>ArrayList<ResolveInfo></code> contains info about installed apps
     */
    private ArrayList<ResolveInfo> fetchInstalledApps(boolean systemApps) {
        // Getting all installed apps and adding them to dataArrayList
        final Intent installedAppsQueryIntent = new Intent(Intent.ACTION_MAIN, null);
        installedAppsQueryIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        ArrayList<ResolveInfo> dataArrayList = new ArrayList<>();
        dataArrayList.addAll(getPackageManager().queryIntentActivities(installedAppsQueryIntent, PackageManager.GET_META_DATA));


        if(!systemApps) {
            // Filtering : Removing non-uninstallable apps [System-apps]!
            Iterator<ResolveInfo> iterator = dataArrayList.iterator();
            while (iterator.hasNext()) {
                ResolveInfo info = iterator.next();
                if (isSystemPackage(info))
                    iterator.remove();
            }
        }

        return dataArrayList;
    }

    /**
     * Fetching the installed applications on the device without system apps
     * @return <code>ArrayList<ResolveInfo></code> contains info about installed apps without system apps
     */
    private ArrayList<ResolveInfo> fetchInstalledApps() {
        return fetchInstalledApps(false);
    }

    private boolean isSystemPackage(ResolveInfo info) {
        return (info.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
