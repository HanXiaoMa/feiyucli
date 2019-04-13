package www.kjwx_poject.com.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.feiyu.library.util.KDensityUtils;
import com.feiyu.library.util.KLoger;
import com.feiyu.library.util.StringUtils;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;

import java.util.HashMap;
import java.util.Random;

import www.kjwx_poject.com.config.WeexHelper;

/**
 * 页面预加载跳转处理
 * created at 2019/1/22 下午3:47
 */
public class WeexFactory {
    //存放weex对象的map
    private static HashMap<String, WeexPage> weexMap = new HashMap<>();
    private static WeexFactory instance = new WeexFactory();

    public static WeexFactory getInstance() {
        return instance;
    }

    public void jump(final Activity activity, String url, final Intent in, final boolean forResult) {
        KLoger.e("---跳转页面处理，包含预加载处理->>>"+url);
        if (hasCache(url)) {

            KLoger.e("---存在缓存直接跳转->>>");
            in.putExtra("url", url);
            if (!forResult) {
                activity.startActivity(in);
            } else {
                activity.startActivityForResult(in, 10001);
                return;
            }
        }
        final WeexPage page = new WeexPage();
        page.instance = new WXSDKInstance(activity);
        page.instance.setBundleUrl(url);
        String pageid = new Random().nextLong() + "";
        in.putExtra("url", url);
        final boolean ispotrait = in.getBooleanExtra("isPortrait", true);
        addCache(url, page);

        KLoger.e("---跳转页面处理，开始预加载->>>");
        page.id = pageid;
//        page.instance.param=(JSONObject)in.getSerializableExtra("param");
        page.instance.registerRenderListener(new IWXRenderListener() {
            @Override
            public void onViewCreated(WXSDKInstance instance, final View view) {
                KLoger.e("---预加载成功跳转->>>");
                page.v = view;
//                page.instance.hasInit = true;

                if (ispotrait) {
                    page.instance.setSize(KDensityUtils.getScreenW(activity), KDensityUtils.getScreenH(activity));
                } else {
                    page.instance.setSize(KDensityUtils.getScreenH(activity), KDensityUtils.getScreenW(activity));
                }
                if (!forResult) {
                    activity.startActivity(in);
                } else {
                    activity.startActivityForResult(in, 10001);
                }
            }
            @Override
            public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
//                ((WeexActivity)context).mask.removeAllViews();

            }
            @Override
            public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

            }
            @Override
            public void onException(WXSDKInstance instance, String errCode, String msg) {
                KLoger.e(errCode+" ----加载失败了原因是>>> " +msg);
            }
        });

        render(activity,page.instance, url);

    }

    public static void addCache(String id, WeexPage p) {
        if (weexMap == null) {
            weexMap = new HashMap<>();
        }
        weexMap.put(id, p);
    }

    public static boolean hasCache(String url) {
        return weexMap.containsKey(url);
    }

    //获取weex 封装的对象
    public WeexPage getPage(String url) {
        if (weexMap.containsKey(url)) {
            WeexPage p = weexMap.get(url);
            remove(url);
            return p;
        }
        return null;
    }

    public void remove(String id) {
        weexMap.remove(id);
    }

    //预加载首页
    public void preRender(final Context context, String indexUrl, final OnRenderFinishListener listener) {
        final WeexPage p = new WeexPage();
        p.instance = new WXSDKInstance(context);
//        p.instance.setBundleUrl(indexUrl);
        p.id = indexUrl;
        if (p.id == null)
            p.id = new Random().nextLong() + "";
        p.instance.registerRenderListener(new IWXRenderListener() {
            @Override
            public void onViewCreated(WXSDKInstance instance, View view) {
                p.v = view;
//                p.instance.hasInit = true;
                p.instance.setSize(KDensityUtils.getScreenW(context), KDensityUtils.getScreenH(context));
                weexMap.put(p.id, p);
                if (listener != null) {
                    listener.onRenderFinish(p);
                }
            }

            @Override
            public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
            }

            @Override
            public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
            }

            @Override
            public void onException(WXSDKInstance instance, String errCode, String msg) {
                KLoger.e(errCode+" ----加载失败了原因是>>> " +msg);

                if (listener != null) {
                    listener.onRenderFailed(p);
                }
            }
        });
        render(context,p.instance, indexUrl);
    }

    public void render(final Context context, WXSDKInstance instance, String url) {
        KLoger.e("---要加载weex代码是>>>" +url);
        if (StringUtils.isEmpty(url))
            return;
        if (url.startsWith("http")) {
            instance.renderByUrl("farwolf", url, null, null, WXRenderStrategy.APPEND_ASYNC);
        } else {
//            KLoger.e("---要加载weex代码是>>>" +WeexHelper.loadLocal(url, context));
            instance.render("farwolf", WeexHelper.loadLocal(url, context), null, null, WXRenderStrategy.APPEND_ASYNC);
        }
    }
    //加载weex 回调
    public interface OnRenderFinishListener {
        void onRenderFinish(WeexPage p);
        void onRenderFailed(WeexPage p);
    }
}
