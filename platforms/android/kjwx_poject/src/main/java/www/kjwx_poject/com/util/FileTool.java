package www.kjwx_poject.com.util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class FileTool {


    /**
     * 获取 assets里的bitmap
     *
     * @param src
     * @param context
     * @return
     */
    public static Bitmap loadAssetImage(String src, Context context) {
        try {
            InputStream is = context.getAssets().open(src);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (IOException io) {
            return null;
        }
    }


    /**
     * 判断文件夹是否存在
     *
     * @param path
     * @return
     */
    public static boolean IsFileExist(String path) {
        File f_new = new File(path);
        return f_new.exists();
    }


    /**
     * 创建文件夹
     *
     * @param dir
     * @return
     */
    public static boolean makeDir(String dir) {
        File f = new File(dir);
        if (!f.exists()) {
            return f.mkdirs();
        }
        return true;
    }


    /**
     * 拷贝 assets 的内容到磁盘
     * @param context 上下文
     * @param oldPath 旧路径
     * @param newPath 新路径
     */
    public static void copyAssets(Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);// 获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {// 如果是目录
                File file = new File(newPath);
                file.mkdirs();// 如果文件夹不存在则创建
                for (String fileName : fileNames) { //递归拷贝
                    copyAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {// 如果是文件
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
