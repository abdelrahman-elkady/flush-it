package util;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by kady on 06/01/15.
 *
 * @author kady
 */
public class Utilities {

    /**
     * Put ArrayList<String> in the sharedPreferences
     * @param sharedPreferences sharedPreferences instance
     * @param key the key value of the data
     * @param values the ArrayList<String>
     */
    public static void putStringArrayPreferences(SharedPreferences sharedPreferences, String key, ArrayList<String> values) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        JSONArray array = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            array.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, array.toString());
        } else {
            editor.putString(key, "");
        }
        editor.commit();
    }

    /**
     * Get an ArrayList<String> from sharedPreferences
     * @param sharedPreferences sharedPreferences instance
     * @param key the key value of the data
     * @return the ArrayList<String> that matches the key
     */
    public static ArrayList<String> getStringArrayPreferences(SharedPreferences sharedPreferences, String key) {

        String json = sharedPreferences.getString(key, "");

        ArrayList<String> data = new ArrayList<String>();
        if (json != null) {
            if(json.equals("")) {
                return new ArrayList<String>();
            }
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    String string = array.optString(i);
                    data.add(string);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            return null;
        }
        return data;
    }
}
