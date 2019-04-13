package www.kjwx_poject.com.activity;

import android.util.Log;

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


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("weex", "reload");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        prepareWeex();


                    }

                });
            }
        }).start();
    }

    private void prepareWeex() {

        // TOD 2019/1/22  进入预加载第一屏weex逻辑
        String indexUrl = url = AppConfig.entry(this);
        KLoger.e("---indexUrl>>> " + indexUrl);
//        //预加载weex
        WeexFactory.getInstance().preRender(this, indexUrl, new WeexFactory.OnRenderFinishListener() {
            @Override
            public void onRenderFinish(WeexPage p) {
                KLoger.e("---加载成功开始通知>>> " + p);
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
