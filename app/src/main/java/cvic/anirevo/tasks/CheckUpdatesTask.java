package cvic.anirevo.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CheckUpdatesTask extends AsyncTask <Void, Void, String> {

    private static final String LAST_UPDATED_DATE_KEY = "cvic.anirevo.last_updated_key";
    private static final String NO_UPDATE = "cvic.anirevo.updates.NONE";

    private static final String UPDATE_CHECK_URL = "https://api.github.com/repos/chenvictor/anirevo-data";

    private static final String TYPE_WIFI = "1";
    private static final String TYPE_ANY = "2";

    private final WeakReference<Context> context;
    private final boolean forced;

    public CheckUpdatesTask(Context context, boolean forced) {
        this.context = new WeakReference<>(context);
        this.forced = forced;
    }

    @Override
    protected void onPreExecute() {
        if (checkNetwork()) {
            //do stuff
            Toast.makeText(context.get(), "Network Ok.", Toast.LENGTH_SHORT).show();
        } else {
            cancel(true);
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (isCancelled()) {
            return null;
        }
        //Check date
        HttpsURLConnection connection = null;
        BufferedReader reader = null;
        try {
            connection = (HttpsURLConnection) new URL(UPDATE_CHECK_URL).openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            cancel(true);
            return null;
        }
    }

    private boolean checkNetwork() {
        ConnectivityManager cm = (ConnectivityManager) context.get().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                if (forced) {
                    return true;
                } else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.get());
                    String downloadUpdateType = prefs.getString("download_update_type", "1");
                    return netInfo.getType() == ConnectivityManager.TYPE_WIFI || downloadUpdateType.equals(TYPE_ANY);
                }
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            JSONObject object = new JSONObject(response);
            String lastPush = object.getString("pushed_at");
            Toast.makeText(context.get(), lastPush, Toast.LENGTH_SHORT).show();
            if (shouldUpdate(lastPush)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context.get());
                builder.setTitle("AniRevo Updates");
                builder.setMessage("A content update is available. Download now?");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            // DownloadUpdates task
                            Toast.makeText(context.get(), "Downloading updates...", Toast.LENGTH_LONG).show();

                        } else if (which == DialogInterface.BUTTON_NEGATIVE){
                            Toast.makeText(context.get(), "Skipping updates...", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No", dialogClickListener);
                builder.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return
     * @param lastPush  fetched last push date
     * @return          true if date is different from stored push date
     */
    private boolean shouldUpdate(String lastPush) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.get());
        String storedPush = prefs.getString(LAST_UPDATED_DATE_KEY,NO_UPDATE);
        return storedPush.equals(NO_UPDATE) || !storedPush.equals(lastPush);
    }

    @Override
    protected void onCancelled(String response) {
        Toast.makeText(context.get(), "Updates Task Cancelled.", Toast.LENGTH_SHORT).show();
    }


}
