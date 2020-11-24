package rouchuan.viewpagerlayoutmanager.carousel;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.leochuan.CenterSnapHelper;
import com.leochuan.ScrollHelper;
import com.leochuan.ViewPagerLayoutManager;

public class SinglePageSnapHelper extends CenterSnapHelper {

    @Override
    public boolean onFling(int velocityX, int velocityY) {
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (!(layoutManager instanceof ViewPagerLayoutManager)) return false;
        final ViewPagerLayoutManager viewPagerLayoutManager = (ViewPagerLayoutManager) layoutManager;
        int targetPosition = lastPage + 1;
        if(viewPagerLayoutManager.getOffset() < lastOffset) {
            targetPosition = lastPage - 1;
        }
        Log.i("myTest",  "  onFling newPostion: " + targetPosition);
        ScrollHelper.smoothScrollToPosition(mRecyclerView, viewPagerLayoutManager, targetPosition);
        return true;

    }
}
