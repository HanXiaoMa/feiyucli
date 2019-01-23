package www.kjwx_poject.com.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.feiyu.library.util.KDensityUtils;
import com.feiyu.library.util.KLoger;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import www.kjwx_base.com.base.BaseTitleActivity;
import www.kjwx_base.com.base.view.TitleBar;
import www.kjwx_poject.com.R;
import www.kjwx_poject.com.config.WeexHelper;
import www.kjwx_poject.com.core.WeexFactory;
import www.kjwx_poject.com.core.WeexPage;
import www.kjwx_poject.com.module.WXNavgationModule;

/**
 * weex基类
 * created at 2019/1/18 下午9:33
 */
public class WeexActivity extends BaseTitleActivity implements IWXRenderListener, View.OnClickListener {
    public WXSDKInstance mWXSDKInstance; //weex 实例对象
    public boolean isPageInit = false;//页面初始化是否完成
    public boolean isRoot = false;//是否是root界面
    public String pageid; //页面id
    public String rootid; // ？？
    public Map param;
    public String url;

    private RelativeLayout mContainer;
    private ImageView mLodingimg;
    private ImageView mFailImg;
    private RelativeLayout mFailLayout;
    private TextView mErr;
    private ScrollView mScroll;
    private Button mCloseErr;
    private LinearLayout mErrLayout;
    private RelativeLayout mRootContainer;
    private TitleBar mTitle;
    private RelativeLayout mMask;
    private RelativeLayout mRoot;

    @Override
    public int getViewId() {
        return R.layout.activity_weex;
    }

    //初始化weexActivity
    private void initWeexActivity() {
        //获取传递来的值
        String url = intent.getStringExtra("url");
        this.isRoot = intent.getBooleanExtra("isRoot", false);
        this.param = (Map) intent.getSerializableExtra("param");
        this.rootid = intent.getStringExtra("rootid");
        WXNavgationModule.addActivity(this.rootid, this); //把实例存到栈中
        resetFrame();
        render(url);
    }

    //加载weex URL 地址
    public void render(String url) {
        render(url, true);
    }

    public void render(String url, boolean showProgress) {
        if (url == null)
            return;
        isPageInit = false;
        //获取预加载的weex 实例对象
        WeexPage page = WeexFactory.getInstance().getPage(url);
        if (page != null) {
            if (page.v == null) {
                return;
            }
            RelativeLayout parent = (RelativeLayout) page.v.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            mContainer.addView(page.v);
            url = page.instance.getBundleUrl();
            mWXSDKInstance = page.instance;
            pageid = page.id;
            this.url = url;
            mWXSDKInstance.setContext(this);
            mWXSDKInstance.registerRenderListener(this);
            mWXSDKInstance.onActivityCreate();
//            mWXSDKInstance.param = (Map) getIntent().getSerializableExtra("param");
//            mWXSDKInstance.firePageInit();
            this.isPageInit = true;
            this.invokeRenderListener();
            ViewGroup.LayoutParams lp = this.mRoot.getLayoutParams();
            mWXSDKInstance.setSize(lp.width, lp.height);
        } else {
            if (showProgress) {
                showLoading();
            }
            mFailLayout.setVisibility(View.GONE);
            if (mWXSDKInstance != null) {
                mWXSDKInstance.registerRenderListener(null);
                mWXSDKInstance.destroy();
            }
            this.url = url;
            mWXSDKInstance = null;
            mWXSDKInstance = new WXSDKInstance(this);
            mWXSDKInstance.setSize(KDensityUtils.getScreenW(this), KDensityUtils.getScreenH(this));
            mWXSDKInstance.registerRenderListener(this);
//            mWXSDKInstance.param = (Map) getIntent().getSerializableExtra("param");
            mWXSDKInstance.setBundleUrl(url);
            if (url.startsWith("http")) {
                if (!url.contains("?"))
                    url += "?";
                url += "p=" + new Random(100000).nextInt();
                mWXSDKInstance.renderByUrl("farwolf", url, null, null, WXRenderStrategy.APPEND_ASYNC);
            } else {
                //进行合并js
                String s = WeexHelper.loadLocal(url, this);
                //加载weex的js代码
                this.renderPage(s, url);
            }

        }
    }

    //加载js代码
    protected void renderPage(String template, String source) {
        renderPage(template, source, null);
    }

    protected void renderPage(String template, String source, String jsonInitData) {
        Map<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, source);
        try {
            String banner = WXUtils.getBundleBanner(template);
            JSONObject jsonObj = JSONObject.parseObject(banner);
            String digest = null;
            if (jsonObj != null) {
                digest = jsonObj.getString(com.taobao.weex.common.Constants.CodeCache.BANNER_DIGEST);
            }
            if (digest != null) {
                options.put(com.taobao.weex.common.Constants.CodeCache.DIGEST, digest);
            }
        } catch (Throwable t) {
        }
        String path = WXEnvironment.getFilesDir(getApplicationContext());
        path += File.separator;
        path += com.taobao.weex.common.Constants.CodeCache.SAVE_PATH;
        path += File.separator;
        options.put(com.taobao.weex.common.Constants.CodeCache.PATH, path);
        mWXSDKInstance.setTrackComponent(true);
        //进行加载js文件
        mWXSDKInstance.render(
                "farwolf",
                template,
                options,
                jsonInitData,
                KDensityUtils.getScreenW(this),
                KDensityUtils.getScreenH(this),
                WXRenderStrategy.APPEND_ASYNC);
    }

    public void invokeRenderListener() {
        // TODO: 2019/1/22  执行加载回调

    }

    public void showLoading() {
        mLodingimg.setVisibility(View.VISIBLE);
    }

    //重置布局
    public void resetFrame() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRootContainer.setLayoutParams(lp);
        makeHidden();
    }

    private void makeHidden() {
//        getTitleBar().setVisibility(View.GONE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 0);
        mRootContainer.setLayoutParams(lp);
        ViewGroup.LayoutParams lpx = mRootContainer.getLayoutParams();
        if (mWXSDKInstance != null)
            mWXSDKInstance.setSize(lpx.width, lpx.height);
    }

    public void hideLoading() {
        mLodingimg.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        mContainer.removeAllViews();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        mContainer.addView(view);
        mContainer.requestLayout();
//        mWXSDKInstance.hasInit = true;
//        mWXSDKInstance.firePageInit();
        mWXSDKInstance.onActivityCreate();
        this.invokeRenderListener();
        this.isPageInit = true;

    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        hideLoading();
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        hideLoading();
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        hideLoading();
    }
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        if (arg0 != null) {
//            String url = arg0.getString("url");
//            if (url != null) {
//                this.url = url;
//            }
//            HashMap m = (HashMap) arg0.getSerializable("static");
//            if (m != null) {
//                KLoger.e("---static变量--->>>>>");
//            }
//        }
//        super.onCreate(arg0);
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putString("url", this.url);
        }
    }

    public void initView() {
        mContainer = findViewById(R.id.container);
        mContainer.setOnClickListener(this);
        mLodingimg = findViewById(R.id.lodingimg);
        mLodingimg.setOnClickListener(this);
        mFailImg = findViewById(R.id.fail_img);
        mFailImg.setOnClickListener(this);
        mFailLayout = findViewById(R.id.fail_layout);
        mFailLayout.setOnClickListener(this);
        mErr = findViewById(R.id.err);
        mErr.setOnClickListener(this);
        mScroll = findViewById(R.id.scroll);
        mScroll.setOnClickListener(this);
        mCloseErr = findViewById(R.id.close_err);
        mCloseErr.setOnClickListener(this);
        mErrLayout = findViewById(R.id.err_layout);
        mErrLayout.setOnClickListener(this);
        mRootContainer = findViewById(R.id.rootContainer);
        mRootContainer.setOnClickListener(this);
        mTitle = findViewById(R.id.title);
        mTitle.setOnClickListener(this);
        mMask = findViewById(R.id.mask);
        mMask.setOnClickListener(this);
        mRoot = findViewById(R.id.root);
        mRoot.setOnClickListener(this);
        initWeexActivity();
    }


    //    生命周期其他的处理 +++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    protected void onPause() {
        super.onPause();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.fireGlobalEventCallback("onPause", null);
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.fireGlobalEventCallback("onStop", null);
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWXSDKInstance != null) {
            mWXSDKInstance.fireGlobalEventCallback("onDestory", null);
            super.onDestroy();
            mWXSDKInstance.onActivityDestroy();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
            mWXSDKInstance.fireGlobalEventCallback("onResume", null);
        }
    }

    @Override
    public void finish() {
        super.finish();
        WXNavgationModule.pop(this.rootid);
        if (mWXSDKInstance != null) {
            mWXSDKInstance.fireGlobalEventCallback("onDestory", null);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.close_err) {

        }
    }


}
