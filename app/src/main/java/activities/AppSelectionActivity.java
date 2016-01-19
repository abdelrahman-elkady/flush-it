package activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.kady.flushit.R;

import java.util.ArrayList;
import java.util.Iterator;

import adapters.ApplicationsAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kady on 23/12/14.
 *
 * @author kady
 */
public class AppSelectionActivity extends AppCompatActivity {

    @g(R.id.lst_app_selection)
    ListView mAppsListView;

    private ApplicationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_selection);


        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Back to action bar
        }

        ButterKnife.bind(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                this.finish(); // Using it as back for now
                return true;

            case(R.id.action_settings):
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case(R.id.action_about):
                startActivity(new Intent(this,AboutActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
