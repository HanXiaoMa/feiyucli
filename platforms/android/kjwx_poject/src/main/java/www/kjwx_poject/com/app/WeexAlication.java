package www.kjwx_poject.com.app;
import www.kjwx_base.com.base.BaseApplication;
import www.kjwx_poject.com.config.AppConfig;
import www.kjwx_poject.com.config.WeexHelper;
import www.kjwx_poject.com.util.Local;

/**
 * weex 的 application 处理
 * <p>
 * created at 2019/1/19 下午1:51
 */
abstract  public class WeexAlication extends BaseApplication {

    private WeexHelper weexHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        //1、拷贝到磁盘
        Local.copyAssetToDisk(this);
        String schema = AppConfig.schema(this);
        //2、初始化weex
        weexHelper = WeexHelper.getInstance();
        weexHelper.init(this, "weex", "weex_kj", schema);
    }
}
