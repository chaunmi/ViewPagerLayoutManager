package rouchuan.viewpagerlayoutmanager.carousel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.leochuan.CarouselLayoutManager;

import rouchuan.viewpagerlayoutmanager.BaseActivity;
import rouchuan.viewpagerlayoutmanager.DataAdapter;
import rouchuan.viewpagerlayoutmanager.Util;

import static com.leochuan.ViewPagerLayoutManager.LOG_PREFIX;

/**
 * Created by Dajavu on 27/10/2017.
 */

public class CarouselLayoutActivity extends BaseActivity<CarouselLayoutManager, CarouselPopUpWindow> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingPopUpWindow = createSettingPopUpWindow();
        dataAdapter.setViewPagerLayoutManager(viewPagerLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int currentIndex = getCenterOffsetChildIndex();
                int itemSpace = viewPagerLayoutManager.getItemSpace() / 2;
                int scrollOffset = Math.abs(viewPagerLayoutManager.getOffsetToCenter());
                float percent = scrollOffset * 1.0f / itemSpace;
                updateAlphas();
                Log.i(LOG_PREFIX, " onScrolled  dx: " + dx + ", currentIndex: " + currentIndex + ", percent: " + percent + ", offset: " + viewPagerLayoutManager.getOffset() + ", itemSpace: " + itemSpace);

            }
        });
    }
    final float[] alphas = new float[]{0f, 0.15f, 0.85f, 0.45f};

    public int getCenterOffsetChildIndex() {
        int childCount = recyclerView.getChildCount();
        View centerView = viewPagerLayoutManager.getCurrentFocusView();
        int centerLayoutPos = viewPagerLayoutManager.getLayoutPositionOfView(centerView);
        int layoutIndex = -10000;
        for(int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            if(centerView == childView) {
                layoutIndex = i;
            }
            int layoutPos = viewPagerLayoutManager.getLayoutPositionOfView(childView);
            float targetOffset = viewPagerLayoutManager.getProperty(layoutPos) - viewPagerLayoutManager.getOffset();
//            Log.i(LOG_PREFIX, " child index: " + i + ", layoutPos: " + layoutPos + ", targetOffset: " +
//                    targetOffset + ", centerView: " + (centerView == childView));
        }
        return layoutIndex;
    }

    public void updateAlphas() {
        int itemSpace = viewPagerLayoutManager.getItemSpace();
        int scrollOffset = Math.abs(viewPagerLayoutManager.getOffsetToCenter());

        int childCount = recyclerView.getChildCount();
        View centerView = viewPagerLayoutManager.getCurrentFocusView();
        int centerLayoutPos = viewPagerLayoutManager.getLayoutPositionOfView(centerView);

        float centerTargetOffset = viewPagerLayoutManager.getProperty(centerLayoutPos) - viewPagerLayoutManager.getOffset();

        for(int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            int layoutPos = viewPagerLayoutManager.getLayoutPositionOfView(childView);
            int alphaIndex = Math.abs(layoutPos - centerLayoutPos);
            float targetOffset = viewPagerLayoutManager.getProperty(layoutPos) - viewPagerLayoutManager.getOffset();
            float normalOffset = viewPagerLayoutManager.getProperty(layoutPos - centerLayoutPos);

            float alpha = alphas[alphaIndex];
            float percent = 0f;
            if(layoutPos == centerLayoutPos) {
                percent = Math.abs(targetOffset) / itemSpace;
                alpha = alphas[1] * percent;
            }else {
                percent = Math.abs(targetOffset) / Math.abs(normalOffset);
                alpha = alpha * percent;
            }
            RecyclerView.ViewHolder viewHolder = recyclerView.findContainingViewHolder(childView);
            if(viewHolder instanceof DataAdapter.ViewHolder) {
                ((DataAdapter.ViewHolder)viewHolder).iconBgView.setAlpha(alpha);
            }

            Log.i(LOG_PREFIX, " child index: " + i + ", layoutPos: " + layoutPos + ", alpha: " + alpha +
                    ", centerLayoutPos: " + centerLayoutPos + ", targetOffset: " + targetOffset + ", normalOffset: " + normalOffset);
        }
    }

    @Override
    protected CarouselLayoutManager createLayoutManager() {
        return new CarouselLayoutManager(this, 200);
    }

    @Override
    protected CarouselPopUpWindow createSettingPopUpWindow() {
        return new CarouselPopUpWindow(this, getViewPagerLayoutManager(), getRecyclerView());
    }
}
