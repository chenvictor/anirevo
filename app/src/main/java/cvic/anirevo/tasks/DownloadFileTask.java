package cvic.anirevo.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import cvic.anirevo.utils.IOUtils;

class DownloadFileTask extends AsyncTask <Void, Void, Void> {

    private static final String TAG = "anirevo.DFT";

    private static final String BASE_PATH_URL = "https://api.github.com/repos/chenvictor/anirevo-data/contents/data/";
    private static final String BASE_DOWNLOAD_URL = "https://raw.githubusercontent.com/chenvictor/anirevo-data/master/data/";

    private DownloadFileTaskListener mListener;
    private final WeakReference<Context> context;

    DownloadFileTask(WeakReference<Context> context, DownloadFileTaskListener listener) {
        this.context = context;
        mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String[] jsonFiles = getFilesInPath("json");
        String[] imageFiles = getFilesInPath("images");
        for (String string : jsonFiles) {
            string = "json/" + string;
            HttpsURLConnection connection;
            BufferedReader reader;
            try {
                URL url = new URL(BASE_DOWNLOAD_URL + string);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder buffer = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                    buffer.append("\n");
                }
                connection.disconnect();
                inputStream.close();
                IOUtils.writeFile(context.get(), string, buffer.toString());
                Log.i(TAG, "Downloaded " + string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String string : imageFiles) {
            string = "images/" + string;
            try {
                URL url = new URL(BASE_DOWNLOAD_URL + string);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                int contentLength = connection.getContentLength();
//                Log.i(TAG, "size: " + contentLength);
                DataInputStream inputStream = new DataInputStream(url.openStream());
                byte[] buffer = new byte[contentLength];
                inputStream.readFully(buffer);
                inputStream.close();
                IOUtils.writeFile(context.get(), string, buffer);
                Log.i(TAG, "Downloaded " + string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Returns the download URL of each file in the given path
     * @param path  path to search
     * @return      an array of URL Strings
     */
    private String[] getFilesInPath(String path) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(BASE_PATH_URL + path).openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder buffer = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            JSONArray array = new JSONArray(buffer.toString());
            String[] files = new String[array.length()];
            for (int i = 0; i < files.length; i++) {
                files[i] = array.getJSONObject(i).getString("name");
            }
            return files;
        } catch (IOException | JSONException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mListener.downloadFileTaskFinished();
    }

    public interface DownloadFileTaskListener {
        void downloadFileTaskFinished();
    }
}
