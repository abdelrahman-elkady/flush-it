package activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.kady.flushit.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kady on 12/06/15.
 *
 * @author kady
 */
public class AboutActivity extends ActionBarActivity {
    @InjectView(R.id.txt_attribution)
    TextView mAttributionTextView ;

    @InjectView(R.id.txt_version_number)
    TextView mVersionNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mVersionNumberTextView.setText("v"+versionName);
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

        switch(id) {
            case android.R.id.home:
                this.finish(); // Using it as back for now
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
