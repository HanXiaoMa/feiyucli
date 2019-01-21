package www.kjwx_poject.com.config;

import android.app.Application;

import com.kjwx_plugin.PluginManager;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;


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
        try {
            PluginManager.registerModule(application);
            PluginManager.registerComponent(application);
        } catch (WXException e) {
            e.printStackTrace();
        }

    }


}
