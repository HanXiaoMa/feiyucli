package www.kjwx_poject.com.util.local;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.InputStream;

import www.kjwx_poject.com.util.SDCard;


/**
 * 沙盒缓存获取
 * <p>
 * created at 2019/1/19 下午12:24
 */
public class Disk implements ILocal {

    @Override
    public String getString(Context c, String path) {
        return SDCard.getString(c, path);
    }

    @Override
    public Bitmap getBitmap(Context c, String path) {
        return SDCard.getBitMap(c, path);
    }

    @Override
    public InputStream getFileInputStream(Context c, String path) {
        return SDCard.getFileStream(c, path);
    }


}
