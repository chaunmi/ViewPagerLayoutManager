package rouchuan.viewpagerlayoutmanager.carousel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.leochuan.ViewPagerLayoutManager;

public class ArcRecyclerView extends RecyclerView {

    public ArcRecyclerView(Context context) {
        this(context, null);
    }

    public ArcRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.i(ViewPagerLayoutManager.LOG_PREFIX, " onLayout ");
    }
}