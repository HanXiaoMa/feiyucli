package www.kjwx_poject.com.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

/**
 * 加载图片
 * created at 2019/1/22 下午7:35
 */
public class ImageAdapter implements IWXImgLoaderAdapter {
    @Override
    public void setImage(String url, final ImageView view, WXImageQuality quality, final WXImageStrategy strategy) {
        if (url != null) {
            if (url.startsWith("file://")) {  //加载本地
                String name = url.split("file://")[1];
                int mipmapId = WXEnvironment.getApplication().getResources().getIdentifier(name, "mipmap", WXEnvironment.getApplication().getPackageName());
                Glide.with(WXEnvironment.getApplication())
                        .load(mipmapId)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(view);

            } else {
                Glide.with(WXEnvironment.getApplication())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(view);
            }
        }

    }
}
