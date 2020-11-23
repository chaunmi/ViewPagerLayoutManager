package rouchuan.viewpagerlayoutmanager.carousel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.leochuan.BlurBitmapUtil;
import com.leochuan.CarouselLayoutManager;
import com.leochuan.ViewPagerLayoutManager;

import rouchuan.viewpagerlayoutmanager.BaseActivity;
import rouchuan.viewpagerlayoutmanager.DataAdapter;
import rouchuan.viewpagerlayoutmanager.Util;

import static com.leochuan.ViewPagerLayoutManager.LOG_PREFIX;

/**
 * Created by Dajavu on 27/10/2017.
 */

public class CarouselLayoutActivity extends BaseActivity<CarouselLayoutManager, CarouselPopUpWindow> {

    public static final int ITEM_SPACE = 400;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingPopUpWindow = createSettingPopUpWindow();
        dataAdapter.setViewPagerLayoutManager(viewPagerLayoutManager);
        dataAdapter.itemSpace = ITEM_SPACE;
        pageShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.requestLayout();
            }
        });
        viewPagerLayoutManager.setOnPageChangeListener(new ViewPagerLayoutManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                pageShow.setText((position + 1) + "/" + dataAdapter.getItemCount());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), dataAdapter.images[position]);
                Bitmap blurBitmap = BlurBitmapUtil.blurBitmap(CarouselLayoutActivity.this, bitmap, 15f);
                blurImg.setImageBitmap(blurBitmap);
                updateAlphas2();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                updateAlphas2();
                Log.i(LOG_PREFIX, " onScrolled  dx: " + dx + ", currentIndex: " + currentIndex + ", percent: " + percent + ", offset: " + viewPagerLayoutManager.getOffset() + ", itemSpace: " + itemSpace);

            }
        });
    }

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

    final float[] alphas = new float[]{0f, 0.15f, 0.3f, 0.45f};

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

    final float[] alphasImg = new float[]{1.0f, 0.05f, 0.01f};
    final float[] alphasFg = new float[]{0.0f, 0.05f, 0.0f};

    final int DIRECTION_LEFT = -1; //从右往左滑动
    final int DIRECTION_NO = 0;
    final int DIRECTION_RIGHT = 1;  //从左往右滑动

    public void updateAlphas2() {
        Log.i(LOG_PREFIX, " -------------------------- updateAlphas2 ---------------------");

        int itemSpace = viewPagerLayoutManager.getItemSpace();
        int scrollOffset = Math.abs(viewPagerLayoutManager.getOffsetToCenter());

        int childCount = recyclerView.getChildCount();
        View centerView = viewPagerLayoutManager.getCurrentFocusView();
        int centerLayoutPos = viewPagerLayoutManager.getLayoutPositionOfView(centerView);
        float offset = viewPagerLayoutManager.getOffset();

        float centerTargetOffset = viewPagerLayoutManager.getProperty(centerLayoutPos) - offset;
        int direction = DIRECTION_NO;
        int centerOffsetInt = (int)centerTargetOffset;
        if(centerOffsetInt < 0) {
            direction = DIRECTION_LEFT;
        }else if(centerOffsetInt > 0) {
            direction = DIRECTION_RIGHT;
        }

        for(int i = 0; i < childCount; i++) {
            View childView = recyclerView.getChildAt(i);
            int layoutPos = viewPagerLayoutManager.getLayoutPositionOfView(childView);
            int alphaIndex = Math.abs(layoutPos - centerLayoutPos);
            float targetOffset = viewPagerLayoutManager.getProperty(layoutPos) - offset;
            float normalOffset = viewPagerLayoutManager.getProperty(layoutPos - centerLayoutPos);
            float alphaImg;     //真实皮肤图片;
            float alphaFg;  //前景透明图
            if(alphaIndex >= alphasImg.length) {
                alphaImg = 0.01f;
                alphaFg = 0.0f;
            }else {
                alphaImg = alphasImg[alphaIndex];
                alphaFg = alphasFg[alphaIndex];
            }

            float percent = 0f;
            if(layoutPos == centerLayoutPos) {  //中间位置
                //img
                percent = Math.abs(targetOffset) / itemSpace;
                alphaImg =  alphasImg[0] * (1 -percent);
                //前景蒙层
                percent = (itemSpace - Math.abs(targetOffset)) / itemSpace;
                alphaFg = alphasFg[1] * (1 - percent);

            }else {
                if(layoutPos < centerLayoutPos) {  //左边部分
                    if(direction == DIRECTION_LEFT) { //从右向左移动
                        alphaImg = 0f;

                        if(alphaIndex == 1) {
                            alphaFg = 0.05f;
                            percent = Math.abs(targetOffset - normalOffset) / itemSpace;
                            alphaFg = alphaFg * (1 - percent);
                        }else {
                            alphaFg = 0.0f;
                        }
                    }else if(direction == DIRECTION_RIGHT) { //从左向右移动
                        if(alphaIndex == 1) {
                            alphaImg = alphasImg[alphaIndex - 1];
                            percent = Math.abs(targetOffset - normalOffset) / itemSpace;
                            alphaImg = alphaImg * percent;

                            alphaFg = 0.05f;
                            percent = Math.abs(targetOffset) / itemSpace;
                            alphaFg = alphaFg * percent;
                        }else {
                            alphaImg = 0f;

                            alphaFg = 0.05f;
                            percent = Math.abs(targetOffset - normalOffset) / itemSpace;
                            alphaFg = alphaFg * percent;
                        }
                    }else {  //未滑动
                        alphaImg = 0f;
                        if(alphaIndex == 1) {
                            alphaFg = 0.05f;
                        }else {
                            alphaFg = 0.0f;
                        }
                    }
                }else {  //右边部分
                    if(direction == DIRECTION_LEFT) { //从右向左边移动
                        if(alphaIndex == 1) {
                            alphaImg = alphasImg[alphaIndex - 1];
                            percent = Math.abs(targetOffset) / itemSpace;
                            alphaImg = alphaImg * (1- percent);

                            alphaFg = 0.05f;
                            alphaFg = alphaFg * percent;

                        }else {
                            alphaImg = 0f;

                            alphaFg = 0.05f;
                            percent = Math.abs(targetOffset - normalOffset) / itemSpace;
                            alphaFg = alphaFg * percent;
                        }
                    }else if(direction == DIRECTION_RIGHT) { //从左向右边移动
                        alphaImg = 0f;

                        if(alphaIndex == 1) {
                            alphaFg = 0.05f;
                            percent = Math.abs(targetOffset - normalOffset) / itemSpace;
                            alphaFg = alphaFg * (1 - percent);
                        }else {
                            alphaFg = 0.0f;
                        }
                    }else {
                        alphaImg = 0f;
                        if(alphaIndex == 1) {
                            alphaFg = 0.05f;
                        }else {
                            alphaFg = 0.0f;
                        }
                    }

                }
            }
            RecyclerView.ViewHolder viewHolder = recyclerView.findContainingViewHolder(childView);
            if(viewHolder instanceof DataAdapter.ViewHolder) {
                ((DataAdapter.ViewHolder)viewHolder).iconBgView.setAlpha(alphaFg);
                ((DataAdapter.ViewHolder)viewHolder).imageView.setAlpha(alphaImg);
            }

            Log.i(LOG_PREFIX, " child index: " + i + ", layoutPos: " + layoutPos +  ", direction: " + direction + ", centerLayoutPos: " +
                    centerLayoutPos + ", alphaImg: " + alphaImg + ", alphaFg: " + alphaFg +
                  ", targetOffset: " + targetOffset +
                    ", normalOffset: " + normalOffset + ", percent: " + percent);
        }
    }

    private boolean isFloatEqual(float x, float y) {
        return Math.abs(x - y) < 0.00000001;
    }

    @Override
    protected CarouselLayoutManager createLayoutManager() {
        return new CarouselLayoutManager(this, 400);
    }

    @Override
    protected CarouselPopUpWindow createSettingPopUpWindow() {
        return new CarouselPopUpWindow(this, getViewPagerLayoutManager(), getRecyclerView());
    }
}
