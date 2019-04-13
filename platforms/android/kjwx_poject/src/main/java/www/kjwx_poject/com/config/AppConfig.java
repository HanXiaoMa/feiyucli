package www.kjwx_poject.com.config;

import android.content.Context;

import org.json.JSONObject;

import www.kjwx_poject.com.util.Local;

/**
 * 进行处理weex的app配置文件
 * <a>两个概念：assets 文件 和 磁盘缓存</a>
 * created at 2019/1/19 上午11:48
 */
public class AppConfig {


    /**
     * 获取配置文件 json对象
     *
     * @param ctx
     * @return
     */
    public static JSONObject config(Context ctx) {
        String s = Local.getString(ctx, "app/weexplus.json");
        try {
            JSONObject j = new JSONObject(s);
            return j;
        } catch (Exception var3) {
            return null;
        }
    }

    /**
     * 从assets 里获取 配置
     *
     * @param ctx
     * @return
     */
    public static JSONObject assetConfig(Context ctx) {
        String s = Local.getAssetManager().getString(ctx, "app/weexplus.json");
        try {
            JSONObject j = new JSONObject(s);
            return j;
        } catch (Exception var3) {
            return null;
        }
    }

    /**
     * 从沙盒磁盘读取配置
     *
     * @param ctx
     * @return
     */
    public static JSONObject diskConfig(Context ctx) {
        String s = Local.getDiskManager().getString(ctx, "app/weexplus.json");
        try {
            JSONObject j = new JSONObject(s);
            return j;
        } catch (Exception var3) {
            return null;
        }
    }


    /**
     * 获取 weex 版本号
     * <a>从 assets 或者 磁盘</a>
     *
     * @param ctx
     * @return
     */
    public static int diskJsVersion(Context ctx) {
        return diskConfig(ctx).optInt("jsVersion");
    }

    /**
     * asset 里的版本号
     *
     * @param ctx
     * @return
     */
    public static int assetJsVersion(Context ctx) {
        return assetConfig(ctx).optInt("jsVersion");
    }


    /**
     * 文件后缀配置
     * <a>从 assets 或者 磁盘</a>
     *
     * @param ctx
     * @return
     */
    public static String schema(Context ctx) {
        try {
            return config(ctx).optString("schema");
        } catch (Exception e) {
            return "";
        }

    }


    /**
     * 读取要合并的js
     *
     * @param c
     * @return
     */
    public static String appBoard(Context c) {
        return config(c).optString("appBoard");
    }

    /**
     * 获取首屏加载的js
     *
     * @param c
     * @return
     */
    public static String entry(Context c) {
        return config(c).optString("releaseEntry");
    }
}
