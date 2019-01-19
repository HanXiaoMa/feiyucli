package www.kjwx_poject.com.config;

import android.app.Application;

import com.taobao.weex.WXSDKEngine;

import www.kjwx_plugin.manager.PluginManager;

/**
 * weex 帮助
 * <p>
 * created at 2019/1/19 下午5:14
 */
public class WeexHelper {


    private static WeexHelper instance = new WeexHelper();

    public static WeexHelper getInstance() {
        return instance;
    }

    public void init(Application application, String name, String groupname, String basedir) {
        WXSDKEngine.addCustomOptions("appName", name);
        WXSDKEngine.addCustomOptions("appGroup", groupname);


        //注册插件
        PluginManager.init(application);
    }


}
