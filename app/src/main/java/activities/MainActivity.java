package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.kady.flushit.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mehdi.sakout.fancybuttons.FancyButton;
import util.Constants;
import util.Utilities;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.btn_flush_it)
    FancyButton mFlushItButton;

    SharedPreferences mSharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mSharedPreferences = this.getSharedPreferences(Constants.PREFERENCE_KEY,MODE_PRIVATE);

        mFlushItButton.setCustomIconFont("Material-Design-Iconic-Font.ttf");

        mFlushItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("SELECTED APPS", Utilities.getStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS).toString());

                ArrayList<String> dataList = Utilities.getStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS);

                if (dataList.isEmpty()) {
                    Toast.makeText(MainActivity.this, "All selected apps uninstalled", Toast.LENGTH_SHORT).show();
                }

                for (String app : dataList) {
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
                    mFlushItButton.setBorderColor(getResources().getColor(R.color.fancy_main_pressed));
                    mFlushItButton.setTextColor(getResources().getColor(R.color.fancy_main_pressed));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    mFlushItButton.setBorderColor(getResources().getColor(R.color.fancy_main_default));
                    mFlushItButton.setTextColor(getResources().getColor(R.color.fancy_main_default));
                }
                return false;
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
