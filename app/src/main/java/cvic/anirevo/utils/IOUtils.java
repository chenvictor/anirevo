package cvic.anirevo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtils {

    public static Bitmap getBitmap(Context context, String path) throws FileNotFoundException {
        FileInputStream inputStream;
        File file = getFile(context, path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        inputStream = new FileInputStream(file);
        return BitmapFactory.decodeStream(inputStream);
    }

    public static String readFile(Context context, String path) throws FileNotFoundException {
        FileInputStream inputStream;
        File file = getFile(context, path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        try {
            inputStream = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            inputStream.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(Context context, String path, String contents) {
        writeFile(context, path, contents.getBytes());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void writeFile(Context context, String path, byte[] contents) {
        FileOutputStream outputStream;
        try {
            File file = getFile(context, path);
            file.getParentFile().mkdirs();
            if (file.exists()) {
                file.delete();
            }
            outputStream = new FileOutputStream(file);
            outputStream.write(contents);
            outputStream.close();
        } catch (FileNotFoundException e) {
            Log.i("temp", "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("temp", "IOException");
            e.printStackTrace();
        }
    }

    private static File getFile(Context context, String path) {
        File dir = context.getExternalFilesDir(null);
        return new File(dir, path);
    }

}
