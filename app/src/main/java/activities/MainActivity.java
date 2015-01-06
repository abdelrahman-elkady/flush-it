package activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hideme.hideme.R;

import at.markushi.ui.CircleButton;
import util.Constants;
import util.Utilities;


public class MainActivity extends ActionBarActivity {

    private CircleButton mHideMeButton ;
    private SharedPreferences mSharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = this.getSharedPreferences(Constants.PREFERENCE_KEY,MODE_PRIVATE);
        mHideMeButton = (CircleButton) findViewById(R.id.btn_hide_me);
        mHideMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("SELECTED APPS", Utilities.getStringArrayPreferences(mSharedPreferences,Constants.CHECKED_ITEMS).toString());

                /*//TODO Add the hideMe functionality here
                Set<String> data = mSharedPreferences.getStringSet(Constants.CHECKED_ITEMS,null) ;
                List<String> dataList = new ArrayList<String>(Arrays.asList(data.toArray(new String[data.size()])));

                for(String app : dataList) {
                    uninstallApp(app);
                }
                // Clearing selected apps
                dataList.clear();
                mSharedPreferences.edit().putStringSet(Constants.CHECKED_ITEMS,new HashSet<String>()).apply();*/
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void uninstallApp(String packageName) {
        Uri packageUri = Uri.parse("package:"+ packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                packageUri);
        startActivity(uninstallIntent);
    }
}
