package adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
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
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashSet;

import util.Constants;

/**
 * Created by kady on 25/12/14.
 *
 * @author kady
 */
public class ApplicationsAdapter extends BaseAdapter {

    private ArrayList<ResolveInfo> mData;
    private ArrayList<String> mSelectedAppsList ;

    private PackageManager mPackageManager ;
    private Context mContext;

    //private SharedPreferences mSharedPreferences ;


    // Constructor !
    public ApplicationsAdapter(Context context , ArrayList<ResolveInfo> mData) {
        this.mData = mData;
        this.mContext = context;

        this.mPackageManager = context.getPackageManager();
        //this.mSharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_KEY,Context.MODE_PRIVATE);
        Prefs.initPrefs(context);

        mSelectedAppsList = new ArrayList<>();

        if(Prefs.getStringSet(Constants.CHECKED_ITEMS,null) != null) {
            mSelectedAppsList.addAll(Prefs.getStringSet(Constants.CHECKED_ITEMS,null));
        }
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

        viewHolder.mSelectedSwitch.setOnCheckedChangeListener(null); //detaching listener before changing check
        if(mSelectedAppsList.contains(packageName))
            viewHolder.mSelectedSwitch.setChecked(true);
        else
            viewHolder.mSelectedSwitch.setChecked(false);


        viewHolder.mSelectedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("ApplicationsAdapter","Selection Check changed");

                //TODO Clean multiple calls to sharedPreferences
                if(Prefs.getStringSet(Constants.CHECKED_ITEMS , null) == null) {
                    HashSet<String> set = new HashSet<String>();
                    if(isChecked) {
                        mSelectedAppsList.add(packageName);

                        set.add(packageName);
                    }
                    Prefs.putStringSet(Constants.CHECKED_ITEMS,set );
                }
                else {
                    HashSet<String> set = (HashSet<String>) Prefs.getStringSet(Constants.CHECKED_ITEMS,null);
                    if(isChecked) {
                        set.add(packageName);
                        mSelectedAppsList.add(packageName);
                    }else {
                        Log.d("REMOVE!","Item Removed !");
                        set.remove(packageName);
                        mSelectedAppsList.remove(packageName);
                    }
                    Prefs.putStringSet(Constants.CHECKED_ITEMS,set);
                }
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
