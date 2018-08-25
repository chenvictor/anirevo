package cvic.anirevo.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class JSONUtils {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getString(Context context, String path) {
        String json;
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static JSONObject getObject(Context context, String path) throws JSONException{
        return new JSONObject(JSONUtils.getString(context, path));
    }

    public static JSONArray getArray(Context context, String path) throws JSONException {
        return new JSONArray(JSONUtils.getString(context, path));
    }

}
