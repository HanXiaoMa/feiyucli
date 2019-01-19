package www.kjwx_poject.com.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 本地存储操作
 * <p>
 * created at 2019/1/19 上午11:52
 */
public class SDCard {


    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    public static String getBasePath(Context context) {
        String path = context.getExternalFilesDir("Caches") + "";
        return path;
    }


    /**
     * 根据文件路径获取文件流
     *
     * @param context
     * @param path
     * @return
     */
    public static FileInputStream getFileStream(Context context, String path) {
        String p = context.getExternalFilesDir("Caches") + "/" + path;
        try {
            FileInputStream is = new FileInputStream(new File(p));
            return is;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 被文件流转化为String
     *
     * @param inputStream
     * @return
     */
    public static String readStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = null;
        try {
            StringBuilder builder = new StringBuilder(inputStream.available() + 10);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] data = new char[4096];
            int len = -1;
            while ((len = bufferedReader.read(data)) > 0) {
                builder.append(data, 0, len);
            }

            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
            }
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
            }
        }
        return "";
    }


    /**
     * 根据文件路径获取文件里的String内容
     *
     * @param context
     * @param path
     * @return
     */
    public static String getString(Context context, String path) {
        FileInputStream fs = getFileStream(context, path);
        if (fs == null)
            return "";
        return readStreamToString(fs);
    }


    /**
     * 获取文件路径bitmap
     *
     * @param context
     * @param path
     * @return
     */
    public static Bitmap getBitMap(Context context, String path) {
        FileInputStream fs = getFileStream(context, path);

        Bitmap bitmap = BitmapFactory.decodeStream(fs);
        return bitmap;
    }





}
