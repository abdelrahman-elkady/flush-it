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
import android.widget.Toast;

import com.hideme.hideme.R;

import java.util.ArrayList;

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

                //TODO Add the hideMe functionality here
                ArrayList<String> dataList = Utilities.getStringArrayPreferences(mSharedPreferences,Constants.CHECKED_ITEMS);

                if(dataList.isEmpty()) {
                    Toast.makeText(MainActivity.this,"All selected apps uninstalled", Toast.LENGTH_SHORT).show();
                }

                for(String app : dataList) {
                    uninstallApp(app);
                }
                // Clearing selected apps
                dataList.clear();
                Utilities.putStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS,dataList);
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
