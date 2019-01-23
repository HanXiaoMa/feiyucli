package www.kjwx_poject.com.core;

import android.view.View;

import com.taobao.weex.WXSDKInstance;


/**
 * weex 实体 用于存储取出对用的 weex 实例
 * created at 2019/1/22 下午2:51
 */
public class WeexPage {
    public String id;
    public View v;
    public WXSDKInstance instance;
    public String url;
    public boolean trigger = false;
}
