package www.kjwx_poject.com.config;

import android.app.Application;
import android.content.Context;

import com.kjwx_plugin.PluginManager;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXException;

import www.kjwx_poject.com.activity.WeexActivity;
import www.kjwx_poject.com.adapter.ImageAdapter;
import www.kjwx_poject.com.module.WXNavgationModule;
import www.kjwx_poject.com.util.Local;


/**
 * weex 帮助
 * <p>
 * created at 2019/1/19 下午5:14
 */
public class WeexHelper {

    public static String basedir;
    private static WeexHelper instance = new WeexHelper();

    public static WeexHelper getInstance() {
        return instance;
    }

    public void init(Application application, String name, String groupname, String dir) {
        basedir = dir;

        InitConfig config = new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(application, config);

        WXSDKEngine.addCustomOptions("appName", name);
        WXSDKEngine.addCustomOptions("appGroup", groupname);
        //注册插件
        try {
            WXSDKEngine.registerModule("navigator", WXNavgationModule.class);
            PluginManager.registerModule(application);
            PluginManager.registerComponent(application);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }


    // 跳转url处理
    public String getRelativeUrl(String url, WXSDKInstance mWXSDKInstance) {
        if (url.startsWith("sdcard:")) {
            return url;
        }
        if (url.startsWith("http")) {
            return url;
        }
        if (url.startsWith("root:")) {
            return url.replace("root:", getBaseUrl(mWXSDKInstance));
        }
        if (url.startsWith("./")) {
            url = url.substring(2);
        }
        String base = mWXSDKInstance.getBundleUrl();
        String q[] = url.split("\\.\\.\\/");
        String x[] = base.split("\\/");
        String p = "";
        for (int i = 0; i < x.length - q.length; i++) {
            p += x[i] + "/";
        }
        p += q[q.length - 1];
        return p;
    }

    public static String getBaseUrl(WXSDKInstance instance) {
        if (instance == null)
            return "";
        return getBaseUrl(instance.getBundleUrl());
    }

    public static String getBaseUrl(String url) {
        String baseurl = "";
        String s = url;
        if (s.startsWith("http")) {
            String x[] = url.split("\\/");
            if (x.length > 3) {
                String res = x[0] + "//" + x[2] + "/" + WeexHelper.basedir;
                if (!res.endsWith("/"))
                    res += "/";
                baseurl = res;
            }
        } else {
            baseurl = "app/";

        }
        return baseurl;
    }

    public static String loadLocal(String path, Context c) {
        String appboard = loadAppboard(c);
        String s = Local.getString(c, path);
        if (!s.startsWith("// { \"framework\": \"Vue\"}"))
            s = "// { \"framework\": \"Vue\"}\n" + s;
        return appboard + s;
    }

    public static String loadAppboard(Context c) {
        String path = AppConfig.appBoard(c);
        path = getLocalRootPath(path);
        String s = Local.getString(c, path);
        return s;
    }

    public static String getLocalRootPath(String path) {
        path = path.replace("root:", "app/");
        return path;
    }

}
