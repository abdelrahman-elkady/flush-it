package activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.kady.flushit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kady on 12/06/15.
 *
 * @author kady
 */
public class AboutActivity extends AppCompatActivity {
    @Bind(R.id.txt_attribution)
    TextView mAttributionTextView;

    @Bind(R.id.txt_version_number)
    TextView mVersionNumberTextView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mVersionNumberTextView.setText("v" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mAttributionTextView.setMovementMethod(LinkMovementMethod.getInstance()); // activating anchor links
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

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
