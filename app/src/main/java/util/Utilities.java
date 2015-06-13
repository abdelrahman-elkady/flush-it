package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

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


    /**
     * Copies a file into another path
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyFile(File source, File destination) throws IOException {
        InputStream in = new FileInputStream(source);
        OutputStream out = new FileOutputStream(destination);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * Logs the keys of the elements stored in the shared prefs
     * @param prefs
     */
    public static void logSharedPreferences(SharedPreferences prefs) {
        Map<String,?> keys = prefs.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("SHARED PREFS CONTENTS", entry.getKey() + ": " +
                    entry.getValue().toString());
        }
    }
}
