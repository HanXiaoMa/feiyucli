package www.kjwx_poject.com.module;

import android.content.Context;

import com.taobao.weex.common.WXModule;

import www.kjwx_poject.com.activity.WeexActivity;

public class BaseModule extends WXModule {

    public WeexActivity getActivity() {
        WeexActivity aty = (WeexActivity) mWXSDKInstance.getContext();
        return aty;
    }

    public Context getContext() {
        return mWXSDKInstance.getContext();
    }

}
