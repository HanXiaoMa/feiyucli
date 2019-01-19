package www.kjwx_base.com.base;

import android.view.View;

import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;

/**
 * weex基类
 * created at 2019/1/18 下午9:33
 */
public class BaseWeexActivity extends BaseTitleActivity implements IWXRenderListener {

    @Override
    public int getViewId() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {

    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }
}
