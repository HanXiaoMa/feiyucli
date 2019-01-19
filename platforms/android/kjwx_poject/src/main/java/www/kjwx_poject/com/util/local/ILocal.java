package www.kjwx_poject.com.util.local;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * 本地存储接口
 *
 * @author 韩伟光
 * created at 2019/1/19 下午12:19
 */
public interface ILocal {
    String getString(Context c, String path);
    Bitmap getBitmap(Context c, String path);
    InputStream getFileInputStream(Context c, String path);
}
