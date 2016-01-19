package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.kady.flushit.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import util.Constants;
import util.Utilities;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_flush_it)
    FancyButton mFlushItButton;

    SharedPreferences mSharedPreferences;

    @Bind(R.id.fab_select_apps)
    FloatingActionButton mSelectAppsFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        mSharedPreferences = this.getSharedPreferences(Constants.PREFERENCE_KEY, MODE_PRIVATE);

        PreferenceManager.setDefaultValues(this, Constants.PREFERENCE_KEY, MODE_PRIVATE, R.xml.preferences, false); // Setting default values for first run

        Utilities.logSharedPreferences(mSharedPreferences);

        mFlushItButton.setCustomIconFont("MaterialIcons-Regular.ttf");

        initializeListeners();

    }

    /**
     * Adding listeners to the views
     */
    private void initializeListeners() {

        mFlushItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("SELECTED APPS", Utilities.getStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS).toString());

                ArrayList<String> dataList = Utilities.getStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS);

                if (dataList == null || dataList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All selected apps uninstalled", Toast.LENGTH_SHORT).show();
                }

                for (String app : dataList) {
                    boolean backupApks = mSharedPreferences.getBoolean(SettingsActivity.KEY_BACKUP_APK, false);

                    if (backupApks) {
                        try {

                            backupApk(app);

                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e("APK BACKUP", "package " + app + " can't be found for backup");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    uninstallApp(app);
                }
                // Clearing selected apps
                dataList.clear();
                Utilities.putStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS, dataList);

            }
        });


        // To simulate button pressing color change with hollow button
        mFlushItButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mFlushItButton.setBorderColor(ContextCompat.getColor(MainActivity.this, R.color.fancy_main_pressed));
                    mFlushItButton.setIconColor(ContextCompat.getColor(MainActivity.this, R.color.fancy_main_pressed));
                    mFlushItButton.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.fancy_main_pressed));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mFlushItButton.setBorderColor(ContextCompat.getColor(MainActivity.this, R.color.fancy_main_default));
                    mFlushItButton.setIconColor(ContextCompat.getColor(MainActivity.this, R.color.fancy_main_default));
                    mFlushItButton.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.fancy_main_default));
                }
                return false;
            }
        });

        mSelectAppsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppSelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Makes a copy of the uninstalled application in a backup folder
     *
     * @param packageName
     * @throws PackageManager.NameNotFoundException
     * @throws IOException
     */
    private void backupApk(String packageName) throws PackageManager.NameNotFoundException, IOException {
        ApplicationInfo appInfo = MainActivity.this.getPackageManager().getApplicationInfo(packageName, 0);
        String path = appInfo.sourceDir;

        if (Utilities.isExternalStorageWritable()) {
            File apkFile = new File(path);

            File backupDir = Environment.getExternalStoragePublicDirectory(Constants.APKS_BACKUP_DIR);

            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }

            String backupName = appInfo.packageName + "_v" + MainActivity.this.getPackageManager().getPackageInfo(packageName, 0).versionName + ".apk";
            Utilities.copyFile(apkFile, new File(backupDir, backupName));
        }
    }

    /**
     * Uninstalling an app using ACTION_DELETE
     *
     * @param packageName the package full name without <em>package:</em>
     */
    private void uninstallApp(String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                packageUri);
        startActivity(uninstallIntent);
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

        switch (id) {
            case android.R.id.home:
                this.finish(); // Using it as back for now
                return true;

            case (R.id.action_settings):
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case (R.id.action_about):
                startActivity(new Intent(this, AboutActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
