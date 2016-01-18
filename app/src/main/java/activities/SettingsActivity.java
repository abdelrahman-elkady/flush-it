package activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kady.flushit.R;

import fragments.SettingsFragment;

/**
 * Created by kady on 12/06/15.
 *
 * @author kady
 */
public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_BACKUP_APK = "pref_backup_apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Back to action bar
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
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

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
