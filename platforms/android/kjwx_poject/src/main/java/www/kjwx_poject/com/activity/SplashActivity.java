package www.kjwx_poject.com.activity;

import com.feiyu.library.util.KLoger;

import www.kjwx_poject.com.R;
import www.kjwx_poject.com.config.AppConfig;
import www.kjwx_poject.com.core.WeexFactory;
import www.kjwx_poject.com.core.WeexPage;

/**
 * 启动页面
 * created at 2019/1/18 上午10:27
 */
public class SplashActivity extends WeexActivity {


    @Override
    public void initView() { //不调用父类

        // TOD 2019/1/22  进入预加载第一屏weex逻辑
        String indexUrl = url = AppConfig.entry(this);
//        //预加载weex
        WeexFactory.getInstance().preRender(this,indexUrl, new WeexFactory.OnRenderFinishListener() {
            @Override
            public void onRenderFinish(WeexPage p) {
                p.instance.fireGlobalEventCallback("onPageInit", null);
                p.instance.onActivityCreate();
            }
            @Override
            public void onRenderFailed(WeexPage p) {
                KLoger.e("---onRenderFailed>>> " + p);
            }
        });

        KLoger.e("---首屏url地址是>>> " + indexUrl);


    }

    @Override
    public int getViewId() {
        return R.layout.activity_splash;
    }

}
