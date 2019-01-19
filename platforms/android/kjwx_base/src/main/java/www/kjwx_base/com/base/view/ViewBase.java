package www.kjwx_base.com.base.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * view基类
 * created at 2019/1/18 下午9:16
 */
public abstract class ViewBase extends LinearLayout {

    public ViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public ViewBase(Context context) {
        super(context);
        initLayout();
    }

    protected void initLayout() {
        LayoutInflater.from(getContext()).inflate(getViewId(), this);
    }

    public abstract int getViewId();


    public Activity getActivity() {
        return (Activity) getContext();
    }


}

