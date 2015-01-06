package adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.hideme.hideme.R;

import java.util.ArrayList;
import java.util.HashSet;

import util.Constants;
import util.Utilities;

/**
 * Created by kady on 25/12/14.
 *
 * @author kady
 */
public class ApplicationsAdapter extends BaseAdapter {

    private ArrayList<ResolveInfo> mData;

    private PackageManager mPackageManager ;
    private Context mContext;

    private SharedPreferences mSharedPreferences ;


    // Constructor !
    public ApplicationsAdapter(Context context , ArrayList<ResolveInfo> mData) {
        this.mData = mData;
        this.mContext = context;

        this.mPackageManager = context.getPackageManager();
        this.mSharedPreferences = mContext.getSharedPreferences(Constants.PREFERENCE_KEY,Context.MODE_PRIVATE);

    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ResolveInfo getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView ;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_settings,null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.mAppName = (TextView) view.findViewById(R.id.settings_item_txt_app_name);
            viewHolder.mPackageName = (TextView) view.findViewById(R.id.settings_item_txt_package_name);
            viewHolder.mAppIcon = (ImageView) view.findViewById(R.id.settings_item_img_icon);
            viewHolder.mSelectedSwitch = (Switch) view.findViewById(R.id.settings_item_switch_selected);
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        ResolveInfo info = getItem(position);

        final String packageName = info.activityInfo.packageName;

        viewHolder.mAppName.setText(info.loadLabel(mPackageManager).toString());
        viewHolder.mPackageName.setText(packageName);
        viewHolder.mAppIcon.setImageDrawable(info.loadIcon(mPackageManager));

        if(Utilities.getStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS) == null) { // Creating a set if it is null
            ArrayList<String> data = new ArrayList<>();
            Utilities.putStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS, data);
        }

        viewHolder.mSelectedSwitch.setOnCheckedChangeListener(null); //detaching listener before changing check
        if(Utilities.getStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS).contains(packageName))
            viewHolder.mSelectedSwitch.setChecked(true);
        else
            viewHolder.mSelectedSwitch.setChecked(false);


        viewHolder.mSelectedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("ApplicationsAdapter","Selection Check changed");

                //TODO Clean multiple calls to sharedPreferences
                ArrayList<String> selectedApps = Utilities.getStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS);
                    if(isChecked) {
                        selectedApps.add(packageName);
                    }else {
                        Log.d("REMOVE!", "Item Removed !");
                        selectedApps.remove(packageName);
                    }
                Utilities.putStringArrayPreferences(mSharedPreferences, Constants.CHECKED_ITEMS, selectedApps);

            }
        });

        return view ;
    }



    static class ViewHolder {
        TextView mAppName ;
        TextView mPackageName ;
        ImageView mAppIcon;
        Switch mSelectedSwitch;
    }
}
