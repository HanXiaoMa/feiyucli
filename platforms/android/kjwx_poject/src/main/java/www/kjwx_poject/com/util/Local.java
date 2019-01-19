package www.kjwx_poject.com.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.InputStream;

import www.kjwx_poject.com.config.AppConfig;
import www.kjwx_poject.com.util.local.Asset;
import www.kjwx_poject.com.util.local.Disk;
import www.kjwx_poject.com.util.local.ILocal;

/**
 * 关于存储的公共类
 * <a>进行操作assets 和  沙盒存储 文件</a>
 * <p>
 * created at 2019/1/19 上午11:50
 */
public class Local {

    // TODO: 2019/1/19   进行修改获取静态对象创建


    /**
     * 判断zip文件是否存在
     *
     * @param ctx
     * @return
     */
    public static boolean isZipExist(Context ctx) {
        String path = SDCard.getBasePath(ctx) + "/zip/app.zip";
        File f = new File(path);
        return f.exists();
    }

    /**
     * 判断配置文件是否存在与 沙盒磁盘
     *
     * @param ctx
     * @return true存在 false 不存在
     */
    public static boolean isDiskExist(Context ctx) {
        String path = SDCard.getBasePath(ctx);
        path += "/app/weexplus.json";
        File f = new File(path);
        return f.exists();
    }

    public static String getString(Context c, String path) {
        return getLocalManager(c).getString(c, path);
    }

    public static ILocal getLocalManager(Context c) {
        if (isDiskExist(c)) {
            return new Disk();
        } else {
            return new Asset();
        }

    }

    public static ILocal getDiskManager() {
        return new Disk();
    }

    public static ILocal getAssetManager() {
        return new Asset();
    }


    /**
     * 把 assets配置文件拷贝到 沙盒磁盘 并且
     *
     * @param ctx
     */
    public static void copyAssetToDisk(Context ctx) {
        String newPath =  SDCard.getBasePath(ctx) + "/app";
        //磁盘不存在配置文件  或者  assets 版本号大于 磁盘版本号 在进行拷贝到磁盘缓存
        if (!isDiskExist(ctx) || AppConfig.assetJsVersion(ctx) > AppConfig.diskJsVersion(ctx)) {
            if (!FileTool.IsFileExist(newPath)) {  //不存在文件夹 进行创建
                FileTool.makeDir(newPath);
            }
            FileTool.copyAssets(ctx, "app", newPath);
        }
        //磁盘存在配置文件 就去对比 版本
        if (isDiskExist(ctx)) {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences("farwolf_weex", Context.MODE_PRIVATE); //私有数据
            int version = sharedPreferences.getInt("downloadJsVersion", -1);
            if (version > AppConfig.diskJsVersion(ctx)) { //进行对比磁盘缓存的版本和 sp 的版本 , sp 大于 磁盘缓存的 说明有新版本weex
                if (isZipExist(ctx)) { // 存在提前下载好的zip包
                    unzip(ctx); //解压
                    sharedPreferences.edit().remove("downloadJsVersion").apply();
                }
            }
        }

    }

    public static void unzip(Context c) {
        Asset a = new Asset();
        InputStream is = a.getFileInputStream(c, "app/app.zip");
        String to =  SDCard.getBasePath(c) + "";
        File f = new File(to + "/app");
        if (f.exists()) {
            f.delete();
        }
         ZipHelper.unZipFile(is, to);
    }


}
