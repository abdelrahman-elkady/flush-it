package adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener {

    private ArrayList<ResolveInfo> mData;
    private ArrayList<String> mSelectedAppsList ;

    private PackageManager mPackageManager ;

    //private SharedPreferences mSharedPreferences ;


    // Constructor !
    public ApplicationsAdapter(Context context , ArrayList<ResolveInfo> mData) {
        this.mData = mData;


        this.mPackageManager = context.getPackageManager();
        //this.mSharedPreferences = context.getSharedPreferences(Constants.PREFERENCE_KEY,Context.MODE_PRIVATE);
        Prefs.initPrefs(context);

        mSelectedAppsList = new ArrayList<>();

        if(Prefs.getStringSet(Constants.CHECKED_ITEMS,null) != null) {
            mSelectedAppsList.addAll(Prefs.getStringSet(Constants.CHECKED_ITEMS,null));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {


        final String packageName = getItem(i).activityInfo.packageName;

        viewHolder.mAppName.setText(getItem(i).loadLabel(mPackageManager).toString());
        viewHolder.mPackageName.setText(packageName);
        viewHolder.mAppIcon.setImageDrawable(getItem(i).loadIcon(mPackageManager));

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

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Getting the item at the given index
     */
    private ResolveInfo getItem(int i) {
        return mData.get(i);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mAppName ;
        TextView mPackageName ;
        ImageView mAppIcon;
        Switch mSelectedSwitch;

        public ViewHolder(View itemView) {
            super(itemView);

            mAppName = (TextView) itemView.findViewById(R.id.settings_item_txt_app_name);
            mPackageName = (TextView) itemView.findViewById(R.id.settings_item_txt_package_name);
            mAppIcon = (ImageView) itemView.findViewById(R.id.settings_item_img_icon);
            mSelectedSwitch = (Switch) itemView.findViewById(R.id.settings_item_switch_selected);

        }
    }
}
