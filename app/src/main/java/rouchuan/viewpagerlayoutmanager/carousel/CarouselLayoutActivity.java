package rouchuan.viewpagerlayoutmanager.carousel;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.leochuan.CarouselLayoutManager;

import rouchuan.viewpagerlayoutmanager.BaseActivity;
import rouchuan.viewpagerlayoutmanager.Util;

/**
 * Created by Dajavu on 27/10/2017.
 */

public class CarouselLayoutActivity extends BaseActivity<CarouselLayoutManager, CarouselPopUpWindow> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingPopUpWindow = createSettingPopUpWindow();
    }

    @Override
    protected CarouselLayoutManager createLayoutManager() {
        return new CarouselLayoutManager(this, Util.Dp2px(this, 100));
    }

    @Override
    protected CarouselPopUpWindow createSettingPopUpWindow() {
        return new CarouselPopUpWindow(this, getViewPagerLayoutManager(), getRecyclerView());
    }
}
